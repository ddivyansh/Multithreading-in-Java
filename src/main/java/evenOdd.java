public class evenOdd {
    private static int number = 1;
    //To prevent the lost of notification from notify, we'll use guard condtion
    private static boolean guard = true;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("printing even and odd numbers");
        Thread odd = new Thread(() -> {
            try {
                printOdd();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread even = new Thread(() -> {
            try {
                printEven();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        odd.start();
        even.start();
        odd.join(); //one is execution is joined with the main thread,
        // so main has to effectively wait for one to complete its executions
        even.join();
        System.out.println("Main thread is done executing");
    }

    private static void printOdd() throws InterruptedException {
        synchronized (LOCK) {
            while (number <= 101) {
                if (number % 2 != 0) {
                    System.out.println("odd : " + number);
                    number++;
                    if (guard) {
                        guard = false;
                        LOCK.notify();
                        LOCK.wait();
                    }
                }
            }
            LOCK.notify();
        }

    }

    private static void printEven() throws InterruptedException {
        synchronized (LOCK) {
            while (number <= 101) {
                if (number % 2 == 0) {
                    System.out.println("even : " + number);
                    number++;
                    if (!guard) {
                        // guard false means the other thread is waiting.
                        // So wake that up, and send the current thread to waiting
                        LOCK.notify();
                        guard = true;
                        LOCK.wait();
                    }
                }
            }
            //Once its done executing, make sure to wake the other thread up to prevent deadlock
            LOCK.notify();
        }
    }
}