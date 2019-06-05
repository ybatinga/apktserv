/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.opreturn;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.mail.JavaMailThread;
import apkt.model.OpReturn;
import apkt.model.OpReturn.OpReturnType;
import apkt.model.TxOpReturn;
import apkt.service.ProjService;
import com.google.common.util.concurrent.Service;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.spongycastle.util.encoders.Hex;


public class OpReturnMain {

    public static NetworkParameters params = TestNet3Params.get();
//    public static NetworkParameters params = MainNetParams.get();
    public static final String APP_NAME = "Twinings";
    private static final String TWININGS = APP_NAME.replaceAll("[^a-zA-Z0-9.-]", "_") + "-" + params.getPaymentProtocolId();
    public static WalletAppKit bitcoin;

    public static void main(String[] args) {
        System.out.println("hello");

        setupWalletKit();

        bitcoin.addListener(new Service.Listener() {
            @Override
            public void starting() {
                super.starting();
                System.out.println("starting");
            }

            @Override
            public void running() {
                super.running();
                System.out.println("running: " + bitcoin.wallet().currentChangeAddress().toString());
            }

            @Override
            public void stopping(Service.State from) {
                super.stopping(from);
                System.out.println("stopping");
            }

            @Override
            public void terminated(Service.State from) {
                super.terminated(from);
                System.out.println("terminated");
            }

            @Override
            public void failed(Service.State from, Throwable failure) {
                super.failed(from, failure);
                System.out.println("failed");
            }

        }, Runnable::run);
        bitcoin.addListener(new Service.Listener() {
        }, OpReturnRunnable::runLater);
        bitcoin.startAsync();

    }

    public static void setupWalletKit() {
        // If seed is non-null it means we are restoring from backup.
        bitcoin = new WalletAppKit(params, new File("."), TWININGS) {
            @Override
            protected void onSetupCompleted() {                
                // Don't make the user wait for confirmations for now, as the intention is they're sending it
                // their own money!
                bitcoin.wallet().allowSpendingUnconfirmedTransactions();
                System.out.println("WalletAppKit onSetupCompleted: " + bitcoin.wallet().currentChangeAddress().toString());
                System.out.println("port: " + params.getPort());
                
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
                EntityManager em = emf.createEntityManager();
                try {
                    // register invalid data when wallet is initialized and when wallet is changed
                    registerInvalidData(em);
                } catch (Exception ex) {
                    Logger.getLogger(OpReturnMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                em.close(); emf.close();
                
                walletListener();
            }
        };
//        bitcoin.setBlockingStartup(false);
    }

    public static void registerInvalidData(EntityManager em) throws Exception{
        List<OpReturn> opReturnList = GenericDaoJpa.findListByAttribute(
                em, 
                OpReturn.class, 
                "status", 
                OpReturn.OpReturnStatus.OP_RETURN_STATUS_INVALID_DATA);
        if (opReturnList.size() < OpReturn.OpReturnInvalidDataList.SIZE) {
            int newInvalidDataNum = OpReturn.OpReturnInvalidDataList.SIZE - opReturnList.size();
                if (newInvalidDataNum != 0){
                    for (int i = 0; i < newInvalidDataNum; i ++){
                    String freshReceiveAddress = bitcoin.wallet().freshReceiveAddress().toString();
                    System.out.println("freshReceiveAddress: " + freshReceiveAddress);
                    OpReturn opReturn = new OpReturn(
                            "", 
                            freshReceiveAddress, 
                            OpReturn.OpReturnStatus.OP_RETURN_STATUS_INVALID_DATA,
                            new Date());
                    GenericDaoJpa.insert(em, opReturn);
                }
            }
        }
    }
    
    public static void registerOpReturnData(EntityManager em) throws Exception{
        
        List<OpReturn> opReturnList = GenericDaoJpa.findListByAttribute(
                em, 
                OpReturn.class, 
                "status", 
                OpReturn.OpReturnStatus.OP_RETURN_STATUS_WAITING_TX);
        
        for (OpReturn opReturn : opReturnList){
            Set<Transaction> transactionSet = bitcoin.wallet().getTransactions(false);
            Iterator<Transaction> iterator = transactionSet.iterator();
            while(iterator.hasNext()) {
                Transaction transaction = iterator.next();
                List<TransactionOutput> transactionOutputs = transaction.getOutputs();
                for (TransactionOutput to : transactionOutputs){
                    Address addressFromP2PKHScript = to.getAddressFromP2PKHScript(params);
                    if (addressFromP2PKHScript != null){
                        if (addressFromP2PKHScript.toString().equals(opReturn.getAddress())){
                            System.out.println("getAddressFromP2PKHScript: " + addressFromP2PKHScript.toString());
                            timestampData(em, opReturn);
                        }
                    }

//                    Address addressFromP2SH = to.getAddressFromP2SH(params);
//                    if (addressFromP2SH != null){
//                        if (addressFromP2SH.toString().equals(opReturn.getAddress())){
//                            System.out.println("getAddressFromP2SH: " + addressFromP2SH.toString());
//                            timestampData(em, opReturn);
//                        }
//                    }
                }
            }
        }
    }
    
    public static void registerOpReturnData(EntityManager em, Transaction tx) throws Exception{
        
        List<OpReturn> opReturnList = GenericDaoJpa.findListByAttribute(
                em, 
                OpReturn.class, 
                "status", 
                OpReturn.OpReturnStatus.OP_RETURN_STATUS_WAITING_TX);
        
        for (OpReturn opReturn : opReturnList){
            
            for (TransactionOutput to : tx.getOutputs()){
                Address addressFromP2PKHScript = to.getAddressFromP2PKHScript(params);
                if (addressFromP2PKHScript != null){
                    if (addressFromP2PKHScript.toString().equals(opReturn.getAddress())){
                        System.out.println("getAddressFromP2PKHScript: " + addressFromP2PKHScript.toString());
                        timestampData(em, opReturn);
                    }
                }
            }
        }
    }
    
    public static void timestampData(EntityManager em, OpReturn opReturn) throws IOException, InsufficientMoneyException, Exception {
        
        byte[] opReturnBytes = null;
        if (opReturn.getType().endsWith(OpReturn.OpReturnType.OP_RETURN_TYPE_TEXT)){
            opReturnBytes = opReturn.getText().getBytes("UTF-8");
        } else if (opReturn.getType().endsWith(OpReturn.OpReturnType.OP_RETURN_TYPE_NOTARIZATION)){
            opReturnBytes = Hex.decode(opReturn.getText());
        }
        
        // Create a tx with an OP_RETURN output
        Transaction tx = new Transaction(params);
        tx.addOutput(Coin.ZERO, ScriptBuilder.createOpReturnScript(opReturnBytes));
        
        System.out.println("wallet before tx: " + bitcoin.wallet().getBalance().toString());

        SendRequest req = SendRequest.forTx(tx);
        BigDecimal sendfee = opReturn.getFee().setScale(5, RoundingMode.HALF_EVEN);
        req.feePerKb = Coin.parseCoin(sendfee.toString());
//        Coin c = req.feePerKb;
//        if (c.value < 15000) {
//            long add = 15000 - c.value;
//            req.feePerKb.add(Coin.valueOf(add));
//        } else if (c.value > 15000) {
//            long subtract = c.value - 15000;
//            req.feePerKb.add(Coin.valueOf(subtract));
//        }
//        Coin c2 = req.feePerKb;
        // Send it to the Bitcoin network
        Wallet.SendResult sendResult = bitcoin.wallet().sendCoins(req);
            
        long fee = sendResult.tx.getFee().longValue();
        System.out.println("getHashAsString: " + sendResult.tx.getHashAsString());
        if (sendResult.tx.getHashAsString() != null){
            opReturn.setStatus(OpReturn.OpReturnStatus.OP_RETURN_STATUS_REGISTERED);
            String ok = GenericDaoJpa.updateWithoutTx(em, OpReturn.class, opReturn);
            TxOpReturn txOpReturn = new TxOpReturn(
                opReturn.getText(),
                opReturn.getAddress(),
                opReturn.getStatus(),
                opReturn.getType(),
                opReturn.getEmail(),
                new Date(),
                sendResult.tx.getHashAsString(),
                opReturn.getFee(),
                opReturn.getLoginId());
            GenericDaoJpa.insert(em, txOpReturn);
            
            if (txOpReturn.getType().equals(OpReturnType.OP_RETURN_TYPE_NOTARIZATION)){
                File file = new File(ProjService.RBPATH);
                URL[] urls = {file.toURI().toURL()};
                ClassLoader loader = new URLClassLoader(urls);
                
                ResourceBundle rb;
                String language = opReturn.getLang();
                if (language.equals("pt")){
                    rb = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("pt"), loader);
                } else {
                    rb = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("en"), loader);
                }
                String emailSubject = rb.getString("email_body_op_return_subject_notarize");
                StringBuilder emailBodyOpReturn = new StringBuilder();
                emailBodyOpReturn.append(rb.getString("email_body_hi"));
                emailBodyOpReturn.append(",");
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_subject_notarize"));
                emailBodyOpReturn.append(": ");
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_transaction_id"));
                emailBodyOpReturn.append(" ");
                emailBodyOpReturn.append(txOpReturn.getTxId());
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_search"));
                emailBodyOpReturn.append(" ");
                
                if (ProjService.ADDRESS == ProjService.AddressType.MAIN){
                    emailBodyOpReturn.append("https://chain.so/tx/BTC/");
                }else if (ProjService.ADDRESS == ProjService.AddressType.TESTNET){
                    emailBodyOpReturn.append("https://chain.so/tx/BTCTEST/");
                }
                emailBodyOpReturn.append(txOpReturn.getTxId());
                
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_end"));
                
                JavaMailThread javaMailThread_1 = new JavaMailThread(opReturn.getEmail(), emailSubject, emailBodyOpReturn.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.shutdown();
            }else if (txOpReturn.getType().equals(OpReturnType.OP_RETURN_TYPE_TEXT)){
                File file = new File(ProjService.RBPATH);
                URL[] urls = {file.toURI().toURL()};
                ClassLoader loader = new URLClassLoader(urls);
                
                ResourceBundle rb;
                String language = opReturn.getLang();
                if (language.equals("pt")){
                    rb = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("pt"), loader);
                } else {
                    rb = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("en"), loader);
                }
                String emailSubject = rb.getString("email_body_op_return_subject_message");
                StringBuilder emailBodyOpReturn = new StringBuilder();
                emailBodyOpReturn.append(rb.getString("email_body_hi"));
                emailBodyOpReturn.append(",");
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_subject_message"));
                emailBodyOpReturn.append(": ");
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_transaction_id"));
                emailBodyOpReturn.append(" ");
                emailBodyOpReturn.append(txOpReturn.getTxId());
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_op_return_search"));
                emailBodyOpReturn.append(" ");
                
                if (ProjService.ADDRESS == ProjService.AddressType.MAIN){
                    emailBodyOpReturn.append("https://chain.so/tx/BTC/");
                }else if (ProjService.ADDRESS == ProjService.AddressType.TESTNET){
                    emailBodyOpReturn.append("https://chain.so/tx/BTCTEST/");
                }
                emailBodyOpReturn.append(txOpReturn.getTxId());
                
                emailBodyOpReturn.append("<br></br><br></br>");
                emailBodyOpReturn.append(rb.getString("email_body_end"));
                
                JavaMailThread javaMailThread_1 = new JavaMailThread(opReturn.getEmail(), emailSubject, emailBodyOpReturn.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.shutdown();
            }
            
        }
            
        System.out.println("wallet after tx: " + bitcoin.wallet().getBalance().toString());
        
    }
    
    public static void walletListener() {
//        bitcoin.wallet().addChangeEventListener(new WalletChangeEventListener() {
//            @Override
//            public void onWalletChanged(Wallet wallet) {
//                System.out.println("onWalletChanged");
//                
//                EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//                EntityManager em = emf.createEntityManager();
//                
//                try {                    
//                    registerOpReturnData(em);
//                    // register invalid data when wallet is initialized and when wallet is changed
//                    registerInvalidData(em);
//                } catch (Exception ex) {
//                    Logger.getLogger(OpReturnMain.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                em.close();
//                emf.close();
//            }
//        });
        bitcoin.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
            @Override
            public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("onCoinsReceived");
                
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
                EntityManager em = emf.createEntityManager();
                
                try {                    
                    registerOpReturnData(em, tx);
                    // register invalid data when wallet is initialized and when wallet is changed
                    registerInvalidData(em);
                } catch (Exception ex) {
                    Logger.getLogger(OpReturnMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                em.close();
                emf.close();            }
        });
    }
}
