package executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CpuIntensiveTask {
    public static void main(String[] args) {
        // in java one thread takes one processor.
        int cores = Runtime.getRuntime().availableProcessors(); //way to get the number of cores or processors
        ExecutorService service = Executors.newFixedThreadPool(cores);
        System.out.println("Created thread pool with " + cores + " cores");
        for (int i = 0; i < 20; i++) {
            service.execute(new CpuTask());
        }
    }
}

class CpuTask implements Runnable {

    @Override
    public void run() {
        System.out.println("Some CPU intensive task being done by :" + Thread.currentThread().getName());
    }
}
