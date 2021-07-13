import java.util.concurrent.*;

class Shared
{
    static int count = 0;
}

class SemWorkerA extends Thread
{
    Semaphore sem;
    String threadName;
    public SemWorkerA(Semaphore sem, String threadName)
    {
        super(threadName);
        this.sem = sem;
        this.threadName = threadName;
    }

    @Override
    public void run() {


            System.out.println("Starting " + threadName);
            try
            {
                System.out.println(threadName + " is waiting for a permit.");

                sem.acquire();

                System.out.println(threadName + " gets a permit.");


                for(int i=0; i < 5; i++)
                {
                    Shared.count++;
                    System.out.println(threadName + ": " + Shared.count);


                    Thread.sleep(10);
                }
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }

            System.out.println(threadName + " releases the permit.");
            sem.release();

    }
}

class SemWorkerB extends Thread
{
    Semaphore sem;
    String threadName;
    public SemWorkerB(Semaphore sem, String threadName)
    {
        super(threadName);
        this.sem = sem;
        this.threadName = threadName;
    }

    @Override
    public void run() {



            System.out.println("Starting " + threadName);
            try
            {
                System.out.println(threadName + " is waiting for a permit.");

                sem.acquire();

                System.out.println(threadName + " gets a permit.");


                for(int i=0; i < 5; i++)
                {
                    Shared.count--;
                    System.out.println(threadName + ": " + Shared.count);

                    Thread.sleep(10);
                }
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }
            System.out.println(threadName + " releases the permit.");
            sem.release();
    }
}


public class Semaphores {
    public static void main(String args[]) throws InterruptedException
    {

        Semaphore sem = new Semaphore(1);


        SemWorkerA mt1 = new SemWorkerA(sem, "A");
        SemWorkerB mt2 = new SemWorkerB(sem, "B");

        mt1.start();
        mt2.start();

        mt1.join();
        mt2.join();

        System.out.println("count: " + Shared.count);
    }
}
