package apkt.service;

import apkt.opreturn.OpReturnRunnable;
import com.google.common.util.concurrent.Service;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletChangeEventListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpReturnService {

//    public static NetworkParameters params = MainNetParams.get();
    public static NetworkParameters params = TestNet3Params.get();
    private static final String TWININGS = "Twinings".replaceAll("[^a-zA-Z0-9.-]", "_") + "-"
            + params.getPaymentProtocolId();

    public static WalletAppKit bitcoin;

    public static void main(String[] args) throws IOException {

        // Make log output concise.
        BriefLogFormatter.init();
        // Create the app kit. It won't do any heavyweight initialization until after we start it.
        setupWalletKit(null);

        if (!bitcoin.isChainFileLocked()) {
            bitcoin.addListener(new Service.Listener() {

                @Override
                public void running() {
                    super.running();
                    try {
                        writeTx();
                    } catch (IOException ex) {
                        Logger.getLogger(OpReturnService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InsufficientMoneyException ex) {
                        Logger.getLogger(OpReturnService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }, Runnable::run);
            bitcoin.addListener(new Service.Listener() {
                @Override
                public void running() {
                    super.running();

                }
            }, OpReturnRunnable::runLater);
            bitcoin.startAsync();
        }
        
    }

    public static void setupWalletKit(DeterministicSeed seed) {
        // If seed is non-null it means we are restoring from backup.
        bitcoin = new WalletAppKit(params, new File("."), TWININGS) {
            @Override
            protected void onSetupCompleted() {
                // Don't make the user wait for confirmations for now, as the intention is they're sending it
                // their own money!
                bitcoin.wallet().allowSpendingUnconfirmedTransactions();

            }
        };

        bitcoin.setBlockingStartup(false);

    }

    private static void writeTx() throws IOException, InsufficientMoneyException {

        Address address = bitcoin.wallet().currentReceiveAddress();
        System.out.println("after: " + address.toString());
//        byte[] b = "17-01-2017 Mane, dobrodosao! vole te tata i mama. Gospodi pomiluj nas.".getBytes("UTF-8");
//        // Create a tx with an OP_RETURN output
//        Transaction tx = new Transaction(params);
//        tx.addOutput(Coin.ZERO, ScriptBuilder.createOpReturnScript(b));
//
//        System.out.println("before: " + bitcoin.wallet().getBalance().toString());
//
//        // Send it to the Bitcoin network
//        bitcoin.wallet().sendCoins(SendRequest.forTx(tx));
//
//        System.out.println("after: " + bitcoin.wallet().getBalance().toString());

        bitcoin.wallet().addChangeEventListener(Runnable::run, new WalletChangeEventListener() {
            @Override
            public void onWalletChanged(Wallet wallet) {
                update(wallet);
            }
        });

    }

    public static void update(Wallet wallet) {
        Coin coin = wallet.getBalance();
        System.out.println("balance: " + wallet.getBalance().toString());
        Address currentReceiveAddress = wallet.currentReceiveAddress();
        System.out.println("currentReceiveAddress_1: " + currentReceiveAddress.toString());
        Address currentReceiveAddress_2 = wallet.currentReceiveAddress();
        System.out.println("currentReceiveAddress_2: " + currentReceiveAddress_2.toString());
        Address freshReceiveAddress = wallet.freshReceiveAddress();
        System.out.println("freshReceiveAddress_1: " + freshReceiveAddress.toString());
        Address freshReceiveAddress_2 = wallet.freshReceiveAddress();
        System.out.println("freshReceiveAddress_2: " + freshReceiveAddress.toString());
        Address currentChangeAddress = wallet.currentChangeAddress();
        System.out.println("currentChangeAddress_1: " + currentChangeAddress.toString());
        Address currentChangeAddress_2 = wallet.currentChangeAddress();
        System.out.println("currentChangeAddress_2: " + currentChangeAddress.toString());
        Address getChangeAddress = wallet.getChangeAddress();
        System.out.println("getChangeAddress: " + getChangeAddress.toString());
        List<Address> addressList = bitcoin.wallet().getIssuedReceiveAddresses();
        boolean isRunning = bitcoin.isRunning();
        Set<Transaction> transactionSet = bitcoin.wallet().getTransactions(false);
        Iterator<Transaction> iterator = transactionSet.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            List<TransactionOutput> transactionOutputs = transaction.getOutputs();
            for (TransactionOutput to : transactionOutputs) {
                Address address = to.getAddressFromP2PKHScript(params);
                if (address != null) {
                    if (address.toString().equals("msZCnzzqqstKP5JRixNxPxw3oTgVuWxaVm")) {
                        System.out.println("getAddressFromP2PKHScript: " + address.toString());
                    }
                }

                Address address1 = to.getAddressFromP2SH(params);
                if (address1 != null) {
                    if (address1.toString().equals("msZCnzzqqstKP5JRixNxPxw3oTgVuWxaVm")) {
                        System.out.println("getAddressFromP2SH: " + address1.toString());
                    }
                }
            }
            String text = transaction.getHashAsString();
            System.out.println("opreturn: " + text);
        }

    }

}
