package executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolDemo {
    public static void main(String[] args) {
        // Here we don't provide the number of threads, this is auto-scaling in nature.
        // The max life of thread once its idle is 60 seconds, after which its killed.
        try (ExecutorService service = Executors.newCachedThreadPool()) {
            for (int i = 0; i < 10000; i++) {
                service.execute(new TaskOne(i));
            }
        }
    }
}

class TaskOne implements Runnable {
    private final int taskId;

    public TaskOne(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Task : " + taskId + " being executed by " + Thread.currentThread().getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
