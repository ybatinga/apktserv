package apkt.opreturn;

import java.util.concurrent.CountDownLatch;

public class OpReturnRunnable {

    private static CountDownLatch startupLatch = new CountDownLatch(1);

    public static void runLater(final Runnable r) {
        waitForStart();
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



