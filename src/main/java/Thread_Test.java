public class Thread_Test {

    private int num = 1; // Follow Java naming conventions (camelCase)

    private static final int LIMIT = 10; // Use a constant for the upper limit

    private final Object lock = new Object(); // Common lock object for synchronization

    public void printEven() {

        synchronized (lock) {

            while (num <= LIMIT) {

                while (num % 2 != 0) { // Wait until it’s even

                    try {

                        lock.wait();

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt(); // Restore interrupt status

                        throw new RuntimeException(e);

                    }

                }

                System.out.println("Even Number: " + num);

                num++;

                lock.notifyAll(); // Notify other threads

            }

        }

    }

    public void printOdd() {

        synchronized (lock) {

            while (num <= LIMIT) {

                while (num % 2 == 0) { // Wait until it’s odd

                    try {

                        lock.wait();

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt(); // Restore interrupt status

                        throw new RuntimeException(e);

                    }

                }

                System.out.println("Odd Number: " + num);

                num++;

                lock.notifyAll(); // Notify other threads

            }

        }

    }

    public static void main(String[] args) {

        Thread_Test tt = new Thread_Test();

        Thread evenThread = new Thread(tt::printEven); // Using method reference

        Thread oddThread = new Thread(tt::printOdd);  // Using method reference

        evenThread.start();

        oddThread.start();

    }

}

