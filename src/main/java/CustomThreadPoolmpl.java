import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
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

        this.taskQueue.put(task);
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
            Runnable task = null;
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted");
            }
            task.run();
        }

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void doStop() {
        this.isStopped = true;
        thread.interrupt();
    }
}

class Demo {
    public static void main(String[] args) throws Exception {
        ThreadPool threadPool = new ThreadPool(2, 3);

        for (int i = 0; i < 6; i++) {

            int taskNo = i;
            threadPool.execute(() -> {
                String message =
                        Thread.currentThread().getName()
                                + ": Task " + taskNo;
                System.out.println(message);
            });
        }
        threadPool.waitUntilAllTasksFinished();
        threadPool.stop();
    }
}
