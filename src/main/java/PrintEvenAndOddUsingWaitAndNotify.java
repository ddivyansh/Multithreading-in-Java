/*
Doesn't work !!!
 */

public class PrintEvenAndOddUsingWaitAndNotify extends Thread {

    static int count = 1;
    final Object object; //lock

    public PrintEvenAndOddUsingWaitAndNotify(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        synchronized (object) {
            while (count <= 100) {
                if (count % 2 == 0 && Thread.currentThread().getName().equals("even")) {
                    System.out.println("Thread Name : " + Thread.currentThread().getName() + " value :" + count);
                    count++;
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (count % 2 != 0 && Thread.currentThread().getName().equals("odd")) {
                    System.out.println("Thread Name : " + Thread.currentThread().getName() + " value :" + count);
                    count++;
                    object.notify();
                }
            }
        }
    }

    public static void main(String[] args) {
        Object object = new Object(); //lock
        Thread evenThread = new Thread(new PrintEvenAndOddUsingWaitAndNotify(object), "Even");
        Thread oddThread = new Thread(new PrintEvenAndOddUsingWaitAndNotify(object), "Odd");
        evenThread.start();
        oddThread.start();
    }

}