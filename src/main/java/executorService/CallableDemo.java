package executorService;

import java.util.concurrent.*;

public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            //submit() takes --- runnable or callable, for callable the return is a future, now here future is a blocking operation
            Future<Integer> result = executorService.submit(new ReturnValueTask());
            executorService.submit(new RunnableImplementation()); //Here it doesn't expect any return type.

            /*result.cancel(true);

            boolean cancelled = result.isCancelled();
            boolean done = result.isDone();*/

            System.out.println(result.get(6, TimeUnit.SECONDS));
            System.out.println("Main thread execution completed!");
        }
    }
}

class ReturnValueTask implements Callable<Integer> {

    //Callable is useful for the cases where we want to the thread to perform some task
    //And the thread should return something.
    @Override
    public Integer call() throws Exception {
        Thread.sleep(5000);
        return 12;
    }
}

class RunnableImplementation implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}
