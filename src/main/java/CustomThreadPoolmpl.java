import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CustomThreadPoolmpl {
}

class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<PoolThreadRunnable> runnables = new ArrayList<>();
    private volatile boolean isStopped = false;

    public ThreadPool(int noOfThreads, int maxNoOfTasks) {
        taskQueue = new ArrayBlockingQueue<>(maxNoOfTasks); //10

        for (int i = 0; i < noOfThreads; i++) {
            PoolThreadRunnable poolThreadRunnable =
                    new PoolThreadRunnable(taskQueue);

            runnables.add(poolThreadRunnable);
        }
        for (PoolThreadRunnable runnable : runnables) {
            new Thread(runnable).start();
        }
    }

    public synchronized void execute(Runnable task) throws Exception {
        if (this.isStopped) throw
                new IllegalStateException("ThreadPool is stopped");
        if (task != null) {
            this.taskQueue.put(task);
        } else
            System.out.println("Null task not permitted. So skipping this task.");
    }

    public void stop() {
        this.isStopped = true;
        for (PoolThreadRunnable runnable : runnables) {
            runnable.doStop();
        }
    }

    public synchronized void waitUntilAllTasksFinished() {
        while (!this.taskQueue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PoolThreadRunnable implements Runnable {
    private final BlockingQueue<Runnable> taskQueue;
    private boolean isStopped = false;
    private Thread thread = null;

    public PoolThreadRunnable(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        this.thread = Thread.currentThread();
        while (!isStopped()) {
            try {
                Runnable task = taskQueue.take();
                Optional.ofNullable(task).ifPresentOrElse(Runnable::run, () -> {
                    System.out.println("Null task is encountered");
                });
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted");
            }
        }

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void doStop() {
        System.out.println(String.format("Thread %s is stopped", thread.getName()));
        this.isStopped = true;
        this.thread.interrupt();
    }
}

class Demo {
    public static void main(String[] args) throws Exception {
        ThreadPool threadPool = new ThreadPool(2, 3);

        for (int i = 0; i < 7; i++) {

            int taskNo = i;
            threadPool.execute(() -> {
                String message =
                        Thread.currentThread().getName()
                                + ": Task " + taskNo;
                System.out.println(message);
            });
        }
        threadPool.waitUntilAllTasksFinished();
        threadPool.execute(null);
        threadPool.stop();
    }
}
