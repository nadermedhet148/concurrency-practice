class Barrier
{
    public synchronized void block() throws InterruptedException
    {
        wait();
    }

    public synchronized void release() throws InterruptedException
    {
        notify();
    }

    public synchronized void releaseAll() throws InterruptedException
    {
        notifyAll();
    }

}
 class MyThread1 implements Runnable
{
    public MyThread1(Barrier barrier)
    {
        this.barrier = barrier;
    }

    public void run()
    {
        try
        {
            Thread.sleep(1000);
            System.out.println("MyThread1 waiting on barrier");
            barrier.block();
            System.out.println("MyThread1 has been relea sed");
        } catch (InterruptedException ie)
        {
            System.out.println(ie);
        }
    }

    private Barrier barrier;

}

 class MyThread2 implements Runnable
{
    Barrier barrier;

    public MyThread2(Barrier barrier)
    {
        this.barrier = barrier;
    }

    public void run()
    {
        try
        {
            Thread.sleep(3000);
            System.out.println("MyThread2 releasing blocked threads\n");
            barrier.release();
            System.out.println("MyThread1 releasing blocked threads\n");
        } catch (InterruptedException ie)
        {
            System.out.println(ie);
        }
    }
}

public class BarrierTest {

    public static void main(String[] args) throws InterruptedException
    {
        Barrier BR = new Barrier();
        Thread t1 = new Thread(new MyThread1(BR));
        Thread t2 = new Thread(new MyThread2(BR));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
