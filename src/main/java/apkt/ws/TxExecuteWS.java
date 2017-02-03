package apkt.ws;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.blockcypher.service.TransactionService;
import apkt.blockcypher.utils.SignUtils;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.OrderJson;
import apkt.json.StringResultJson;
import apkt.mail.JavaMailThread;
import apkt.model.Address;
import apkt.model.IntermediaryTransaction;
import apkt.model.Login;
import apkt.model.Order;
import apkt.model.OrderPymntMthd;
import apkt.model.OrderStatus;
import apkt.model.OrderWallet;
import apkt.model.Tx;
import apkt.model.TxInput;
import apkt.model.TxOutput;
import apkt.model.TxPymntMthd;
import apkt.model.TxWallet;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.ProjService;
import apkt.service.CalcVarsService;
import apkt.service.StringVarsService;
import apkt.utils.CalcUtils;
import com.google.gson.Gson;
import apkt.utils.BlockCypherConstants;
import apkt.utils.StringUtil;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("txExecute")
public class TxExecuteWS {

    public TxExecuteWS() {
    }

//    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getText(String jsonclass) throws UnsupportedEncodingException {
        jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
        
        OrderJson orderJson = new Gson().fromJson(jsonclass, OrderJson.class);
        Order orderPost = orderJson.getOrder();
        OrderWallet orderWallet = null;
        OrderPymntMthd orderPymntMthd = null;
        Address multisigAddress = null;
        TxWallet txWallet;
        TxPymntMthd txPymntMthd;
        StringResultJson stringResultJson = new StringResultJson();
        Date date = new Date();
        
        try {
                
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            Login loginAuth = ServiceDaoJpa.authUser(em, orderJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            orderJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);
            
            TransactionService transactionService = blockCypherContext.getTransactionService();
            AddressService addressService = blockCypherContext.getAddressService();
            
            List<String> addressDest = new ArrayList<String>();
            List<String> pubKeyMultiSigList = new ArrayList<String>();
            List<String> privKeyMultiSigList = new ArrayList<String>();
            
            Order order = GenericDaoJpa.find(em, Order.class, new Long(orderPost.getId()));
            
//            if (order.getType().equals(Order.OrderType.ORDER_TYPE_SELL)) {
//                wallet = GenericDaoJpa.findByAttribute(em, Wallet.class, "loginId", order.getTakerLoginId());       
//                pymntMthd = GenericDaoJpa.findByAttribute(em, PymntMthd.class, "loginId", order.getMakerLoginId());
//            } else if (order.getType().equals(Order.OrderType.ORDER_TYPE_BUY)){
//                wallet = GenericDaoJpa.findByAttribute(em, Wallet.class, "loginId", order.getMakerLoginId());                
//                pymntMthd = GenericDaoJpa.findByAttribute(em, PymntMthd.class, "loginId", order.getTakerLoginId());       
//            }
//            addressDest.add(wallet.getAddress());
            
            for (OrderPymntMthd opm : order.getOrderPymntMthdList()){
                if (opm.isPymntMthdSelected()){
                    orderPymntMthd = opm;
                }
            }
            orderWallet = order.getOrderWalletId();
            addressDest.add(orderWallet.getAddress());
            
            List<Address> addressList = GenericDaoJpa.findListByAttribute(em, Address.class, "orderId", order.getId());
            
            // fill pubKeyMultiSigList and privKeyMultiSigList repeating three times "for" iteration.
            // It prevents the following exception from happening: "java.lang.IndexOutOfBoundsException: Index: 2, Size: 1".
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_MAKER)) {
                    pubKeyMultiSigList.add(address.getPublicAddress());
                    privKeyMultiSigList.add(address.getPrivateAddress());                    
                }
            }            
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_TAKER)) {
                    pubKeyMultiSigList.add(address.getPublicAddress());
                    privKeyMultiSigList.add(address.getPrivateAddress());                    
                }
            }            
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_ESCROW)) {
                    pubKeyMultiSigList.add(address.getPublicAddress()); 
                    privKeyMultiSigList.add(address.getPrivateAddress());
                }
            }
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_MULTISIG)) {
                    multisigAddress = address;
                }
            }
            
            Long amountNetBuy = CalcUtils.btcToSatoshis(order.getAmountNetBuy());
            Long amountNetSell = CalcUtils.btcToSatoshis(order.getAmountNetSell());
            
//            LinkedTreeMap<String, String> l = (LinkedTreeMap<String, String>) addressService.getAddress(multisigAddress.getAddress());
//            Thread.sleep(1000); // pause is necessary when linkedTreeMap is in use to prevent exception
            
            BigDecimal minerFees = new BigDecimal(CalcVarsService.MINING_FEES_SATOSHIS);
            
            Long proccessFees = (amountNetSell - CalcVarsService.MINING_FEES_SATOSHIS) - (amountNetBuy + CalcVarsService.MINING_FEES_SATOSHIS);
            
            IntermediaryTransaction intermTxSpend = transactionService.newFundingTransaction(
                    pubKeyMultiSigList,
                    addressDest,
                    amountNetBuy,
                    minerFees,
                    true);
            Thread.sleep(1000);
            intermTxSpend.addPubKeys(pubKeyMultiSigList.get(0));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend, privKeyMultiSigList.get(0));
            Thread.sleep(1000);
            intermTxSpend.addPubKeys(pubKeyMultiSigList.get(1));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend, privKeyMultiSigList.get(1));
            Thread.sleep(1000);
            
            Tx transaction = transactionService.sendTransaction(intermTxSpend);
            transaction.setAddressId(multisigAddress.getId());
            transaction.setAddressesJson(new Gson().toJson(transaction.getAddresses()));
            transaction.setInputsJson(new Gson().toJson(transaction.getInputs()));
            transaction.setOutputsJson(new Gson().toJson(transaction.getOutputs()));
            transaction.setOrderId(order.getId());
            transaction = GenericDaoJpa.insert(em, transaction);
            
            List<String> addressDest_2 = new ArrayList<String>();
            addressDest_2.add(ProjService.ADDRESS);            
            IntermediaryTransaction intermTxSpend_2 = transactionService.newFundingTransaction(
                    pubKeyMultiSigList,
                    addressDest_2,
                    proccessFees,
                    minerFees,
                    true);
            Thread.sleep(1000);
            intermTxSpend_2.addPubKeys(pubKeyMultiSigList.get(0));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend_2, privKeyMultiSigList.get(0));
            Thread.sleep(1000);
            intermTxSpend_2.addPubKeys(pubKeyMultiSigList.get(1));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend_2, privKeyMultiSigList.get(1));
            Thread.sleep(1000);
            Tx transaction_2 = transactionService.sendTransaction(intermTxSpend_2);
            
            
            
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            
                for (TxInput t : transaction.getInputs()){
                    t.setTxId(transaction.getId());
                    t.setOrderId(order.getId());
                    t.setAddressesJson(new Gson().toJson(t.getAddresses()));
                    String res_1 = GenericDaoJpa.insertWithoutTx(em, t);
                }
                for (TxOutput t : transaction.getOutputs()){
                    t.setTxId(transaction.getId());
                    t.setOrderId(order.getId());
                    t.setAddressesJson(new Gson().toJson(t.getAddresses()));
                    String res_1 = GenericDaoJpa.insertWithoutTx(em, t);
                }
                
                txWallet = new TxWallet(orderWallet);
                txWallet.setOrderId(order.getId());
                String result_1 = GenericDaoJpa.insertWithoutTx(em, txWallet);
                txPymntMthd = new TxPymntMthd(orderPymntMthd);
                txPymntMthd.setOrderId(order.getId());
                String result_2 = GenericDaoJpa.insertWithoutTx(em, txPymntMthd);
                
                order.setStatus(Order.OrderStatuses.ORDER_STATUS_TX_RELEASED);
                order.setUpdatedAt(date);
                String result_3 = GenericDaoJpa.updateWithoutTx(em, Order.class, order);
                
                OrderStatus orderStatus = new OrderStatus(
                        order.getStatus(),
                        date,
                        order.getId());
                GenericDaoJpa.insertWithoutTx(em, orderStatus);
                
                Login makerLogin = ServiceDaoJpa.getUser(em, order.getMakerLoginId());
                Login takerLogin = ServiceDaoJpa.getUser(em, order.getTakerLoginId());
//                multisigAddress = GenericDaoJpa.findByAttributeTwo(em, Address.class, "orderId", order.getId(), "type", Address.AddressType.ADDRESS_TYPE_MULTISIG);
                
//                PaymentForward paymentForward = GenericDaoJpa.findByAttribute(em, PaymentForward.class, "orderId", order.getId());
//                paymentForwardService.deletePayment(paymentForward.getId());
                
            tx.commit();
            em.close(); emf.close();
            
            if (order.getType().equals(Order.OrderType.ORDER_TYPE_BUY)){
                
                StringUtil.setLanguageResource(makerLogin.getLang());
                String emailSubjectMaker = ProjService.RB.getString("email_subject_order_executed_tx_finalized");
                StringBuilder emailBodyMaker = new StringBuilder();
                emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyMaker.append(makerLogin.getUsername() + ",");
                emailBodyMaker.append("<br></br><br></br>");                
                emailBodyMaker.append(ProjService.RB.getString("email_body_tx_finalized_buyer_notify_seller") + " ");
                emailBodyMaker.append(takerLogin.getUsername());
                emailBodyMaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released"));
                emailBodyMaker.append(order.getAmountNetBuy());
                emailBodyMaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released_btc"));
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));

                StringUtil.setLanguageResource(takerLogin.getLang());
                String emailSubjectTaker = ProjService.RB.getString("email_subject_order_executed_tx_finalized");
                StringBuilder emailBodyTaker = new StringBuilder();
                emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyTaker.append(takerLogin.getUsername() + ",");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_tx_finalized_seller_notify"));
                emailBodyTaker.append(order.getAmountNetBuy());
                emailBodyTaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_seller_notify_funds") + " ");
                emailBodyTaker.append(makerLogin.getUsername());
                emailBodyTaker.append(".");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_end"));

                JavaMailThread javaMailThread_1 = new JavaMailThread(
                    makerLogin.getEmail(), 
                    emailSubjectMaker, 
                    emailBodyMaker.toString());
                JavaMailThread javaMailThread_2 = new JavaMailThread(
                    takerLogin.getEmail(), 
                    emailSubjectTaker,
                    emailBodyTaker.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.execute(javaMailThread_2);
                threadExecutor.shutdown();
            } else if (order.getType().equals(Order.OrderType.ORDER_TYPE_SELL)){           
                
                StringUtil.setLanguageResource(takerLogin.getLang());
                String emailSubjectTaker = ProjService.RB.getString("email_subject_order_executed_tx_finalized");
                StringBuilder emailBodyTaker = new StringBuilder();
                emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyTaker.append(takerLogin.getUsername() + ",");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_tx_finalized_buyer_notify_seller") + " ");
                emailBodyTaker.append(makerLogin.getUsername());
                emailBodyTaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released"));
                emailBodyTaker.append(order.getAmountNetBuy());
                emailBodyTaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released_btc"));
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_end"));

                StringUtil.setLanguageResource(makerLogin.getLang());
                String emailSubjectMaker = ProjService.RB.getString("email_subject_order_executed_tx_finalized");
                StringBuilder emailBodyMaker = new StringBuilder();
                emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyMaker.append(makerLogin.getUsername() + ",");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_tx_finalized_seller_notify"));
                emailBodyMaker.append(order.getAmountNetBuy());
                emailBodyMaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_seller_notify_funds") + " ");
                emailBodyMaker.append(makerLogin.getUsername());
                emailBodyMaker.append(".");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));

                JavaMailThread javaMailThread_1 = new JavaMailThread(
                    takerLogin.getEmail(), 
                    emailSubjectTaker, 
                    emailBodyTaker.toString());
                JavaMailThread javaMailThread_2 = new JavaMailThread(
                    makerLogin.getEmail(), 
                    emailSubjectMaker,
                    emailBodyMaker.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.execute(javaMailThread_2);
                threadExecutor.shutdown();
            }
            
//            StringBuilder emailBodyMaker = new StringBuilder();
//            emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
//            emailBodyMaker.append("<br></br><br></br>");
//            emailBodyMaker.append(ProjService.RB.getString("email_body_tx_finalized_buyer_notify_seller") + " ");
//            emailBodyMaker.append(makerLogin.getUsername());
//            emailBodyMaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released") + " ");
//            emailBodyMaker.append(order.getAmountNetBuy());
//            emailBodyMaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_buyer_released_btc"));
//            emailBodyMaker.append("<br></br><br></br>");
//            emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
//            
//            StringBuilder emailBodyTaker = new StringBuilder();
//            emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
//            emailBodyMaker.append("<br></br><br></br>");
//            emailBodyTaker.append(ProjService.RB.getString("email_body_tx_finalized_seller_notify"));
//            emailBodyTaker.append(takerLogin.getUsername());
//            emailBodyTaker.append(" " + ProjService.RB.getString("email_body_tx_finalized_seller_notify_funds") + " ");
//            emailBodyTaker.append(order.getAmountNetBuy());
//            emailBodyTaker.append(ProjService.RB.getString("email_body_tx_finalized_seller_notify_sent"));
//            emailBodyMaker.append("<br></br><br></br>");
//            emailBodyTaker.append(ProjService.RB.getString("email_body_end"));
//            
//            JavaMailThread javaMailThread_1 = new JavaMailThread(
//                makerLogin.getEmail(), 
//                ProjService.RB.getString("email_subject_order_executed_tx_finalized").concat(takerLogin.getUsername()), 
//                emailBodyTaker.toString());
//            JavaMailThread javaMailThread_2 = new JavaMailThread(
//                takerLogin.getEmail(), 
//                ProjService.RB.getString("email_subject_order_executed_tx_finalized").concat(makerLogin.getUsername()),
//                emailBodyMaker.toString());
//            ExecutorService threadExecutor = Executors.newCachedThreadPool();
//            threadExecutor.execute(javaMailThread_1);
//            threadExecutor.execute(javaMailThread_2);
//            threadExecutor.shutdown();
            
            stringResultJson.setResult(StringVarsService.OK);
            String res = new Gson().toJson(stringResultJson);        
            return res; 
            
        } catch (Exception exception) {
            String e = exception.toString();
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName() + Order.OrderStatuses.ORDER_STATUS_TX_DEPOSITED, exception.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
    }

}
