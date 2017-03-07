//package apkt.opreturn;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//import javax.annotation.Resource;
//import javax.ejb.Singleton;
//import javax.enterprise.concurrent.ManagedExecutorService;
//
//@Singleton
//public class ConcurrencyInitializer {
//    /**
//     * The number of milliseconds to wait before try to 
//     */
//    public static final long RETRY_DELAY = 500L;
//
//    /**
//     * The maximum number of concurrency attempts to make before failing
//     */
//    public static final int MAX_RETRIES = 20;
//
//    @Resource
//    ManagedExecutorService executorService;
//    
//    /**
//     * Repeatedly attempts to submit a Runnable task to an injected ManagedExecutorService
//     * to trigger the readying of the Concurrency resources.
//     * 
//     * @return true if successful (Concurrency resources are now ready for use),
//     *         false if timed out instead
//     */
//    public boolean init() {
//        final AtomicBoolean done = new AtomicBoolean(false);
//        int i = 0;
//
//        try {
//            while (!done.get() && i++ < MAX_RETRIES) {
//                executorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        done.set(true);
//                    }
//                });
//                Thread.sleep(RETRY_DELAY);
//            }
//        } catch(InterruptedException e) {
//            //Do nothing.
//        } 
//
//        return done.get();
//    }
//}