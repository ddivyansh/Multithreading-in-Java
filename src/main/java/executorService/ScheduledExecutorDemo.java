package executorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorDemo {
    public static void main(String[] args) {
        // scheduled thread pool executor performs the same task at a given interval (2000) after initial delay of 1000 ms.
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new ProbeTask(), 1000, 2000, TimeUnit.MILLISECONDS);

        try {
            //This is the logic to shut-down the scheduled thread pool executor. If it doesnt get shutdown in this time, it would call the shutdown()
            // of the executor service interface & it would perform the graceful shutdown
            if (!executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow(); 
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}

class ProbeTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Probing end point for updates...");
    }
}