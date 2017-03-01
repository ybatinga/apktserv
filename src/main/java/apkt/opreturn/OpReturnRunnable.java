package apkt.opreturn;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class OpReturnRunnable {

    private static CountDownLatch startupLatch = new CountDownLatch(1);
    private static AtomicInteger pendingRunnables = new AtomicInteger(0);
    private final static Object runLaterLock = new Object();

    public static void runLater(final Runnable r) {
        runLater(r, false);
    }

    private static void runLater(final Runnable r, boolean exiting) {
        pendingRunnables.incrementAndGet();
        waitForStart();
        synchronized (runLaterLock) {

            final AccessControlContext acc = AccessController.getContext();
                try {
                    AccessController.doPrivileged((PrivilegedAction<Void>) () ->{
                        r.run();
                        return null;
                    }, acc);
                } finally {
                    pendingRunnables.decrementAndGet();
                }
        }
    }

    private static void waitForStart() {
        // If the startup runnable has not yet been called, then wait it.
        // Note that we check the count before calling await() to avoid
        // the try/catch which is unnecessary after startup.
        if (startupLatch.getCount() > 0) {
            try {
                startupLatch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}



