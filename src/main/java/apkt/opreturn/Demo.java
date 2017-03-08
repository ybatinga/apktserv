//package apkt.opreturn;
//
//import apkt.dao.jpa.GenericDaoJpa;
//import apkt.model.OpReturn;
//import com.google.common.util.concurrent.Service;
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.annotation.PostConstruct;
//import javax.ejb.Asynchronous;
//import javax.ejb.EJB;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
////import javax.enterprise.concurrent.ManagedThreadFactory;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import org.bitcoinj.core.InsufficientMoneyException;
//import org.bitcoinj.core.NetworkParameters;
//import org.bitcoinj.kits.WalletAppKit;
//import org.bitcoinj.params.TestNet3Params;
//import org.bitcoinj.wallet.DeterministicSeed;
//import org.bitcoinj.wallet.Wallet;
//import org.bitcoinj.wallet.listeners.WalletChangeEventListener;
//
////code source: http://stackoverflow.com/questions/23900826/glassfish-4-using-concurrency-api-to-create-managed-threads
//@Singleton
//@Startup
//public class Demo {
//
////    @Resource(name="java:comp/DefaultManagedThreadFactory") ManagedThreadFactory threadFactory;
////    @EJB private ConcurrencyInitializer concurrencyInitializer;
//    @EJB private Demo self;
//    
//    private static NetworkParameters params = TestNet3Params.get();
//    private static final String TWININGS = "Twinings".replaceAll("[^a-zA-Z0-9.-]", "_") + "-"
//            + params.getPaymentProtocolId();
//    private WalletAppKit bitcoin;
//
//
//    @PostConstruct
//    public void startup() {
//        self.startThread();
//    }
//
//    @Asynchronous
//    public void startThread() {
//        //This line applies the workaround
////        concurrencyInitializer.init();
//
////        //Everything beyond this point is my original application logic
////        threadFactory.newThread(
////            new Runnable() {
////                @Override
////                public void run() {
////                    System.out.println("Do something.");
//                    try {
//                        write(null);
//                    } catch (IOException ex) {
//                        Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
//                    }
////                    OpReturnService opReturnService = OpReturnService.getInstance();
////                }
////            }
////        ).start();            
//    }
//
//    public void write(String msg) throws IOException {
//
////        // Make log output concise.
////        BriefLogFormatter.init();
//        // Create the app kit. It won't do any heavyweight initialization until after we start it.
//        setupWalletKit(null);
////        Address address = getBitcoin().wallet().currentReceiveAddress();
//
//        bitcoin.addListener(new Service.Listener() {
//
//            @Override
//            public void running() {
//                super.running();
//                try {
//                    writeTx();
//                } catch (IOException ex) {
//        //                        Logger.getLogger(OpReturnService.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InsufficientMoneyException ex) {
//        //                        Logger.getLogger(OpReturnService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//        }, Runnable::run);
//        bitcoin.addListener(new Service.Listener() {}, OpReturnRunnable::runLater);
//        bitcoin.startAsync();
//        //            bitcoin.awaitRunning();
//            
//        
//    }
//    
//    private void writeTx() throws IOException, InsufficientMoneyException {
//
//        
//
//    }
//
//    public void setupWalletKit(DeterministicSeed seed) {
//        // If seed is non-null it means we are restoring from backup.
//        if (bitcoin == null)
//        bitcoin = new WalletAppKit(params, new File("."), TWININGS) {
//            @Override
//            protected void onSetupCompleted() {
//                // Don't make the user wait for confirmations for now, as the intention is they're sending it
//                // their own money!
//                bitcoin.wallet().allowSpendingUnconfirmedTransactions();
//                bitcoin.wallet().addChangeEventListener(Runnable::run, new WalletChangeEventListener() {
//                    @Override
//                    public void onWalletChanged(Wallet wallet) {
//                    OpReturn opReturn = new OpReturn();
//                    opReturn.setDateOpReturn(new Date());
//                    opReturn.setText("test");
//                    opReturn.setAddress("address");
//
//                    EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//                    EntityManager em = emf.createEntityManager();
//
//                        try {
//                            GenericDaoJpa.insert(em, opReturn);
//                        } catch (Exception ex) {
//                            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                    em.close(); emf.close();
//                    }
//                });
//            }
//        };
////        getBitcoin().setBlockingStartup(false);
//    }
//}