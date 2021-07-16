package ThreadCancelation;

class MyThread implements Runnable {

    Thread t;

    MyThread()
    {
        t = new Thread(this);
        System.out.println("New thread: " + t);
        t.start();
    }

    public void run()
    {
        while (!Thread.interrupted()) {
            System.out.println("Thread is running");
        }
        System.out.println("Thread has stopped.");
    }
}


public class killingThreads {

    public static void main(String args[])
    {
        MyThread t1 = new MyThread();

        try {
            Thread.sleep(1);

            t1.t.interrupt();

            Thread.sleep(5);
        }
        catch (InterruptedException e) {
            System.out.println("Caught:" + e);
        }
        System.out.println("Exiting the main Thread");
    }

}





