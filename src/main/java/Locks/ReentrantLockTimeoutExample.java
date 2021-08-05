package Locks;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

 class Shared{
    static int count = 0;
}

class CounterWorker implements Runnable {
    private String threadName;
    ReentrantLock lock;
    CounterWorker(String threadName, ReentrantLock lock){
        this.threadName = threadName;
        this.lock = lock;
    }
    @Override
    public void run() {
        System.out.println("In Counter run method, thread " + threadName
                + " is waiting to get lock");
        // acquiring the lock
        try {
            if(lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    Thread.sleep(300);
                    System.out.println("Thread " + threadName + " has got lock");
                    Shared.count++;
                    System.out.println("Thread " + threadName +
                            " Count " + Shared.count);
                } finally {
                    System.out.println("Thread " + threadName
                            + " releasing lock");
                    // releasing the lock
                    lock.unlock();
                }
            }else {
                System.out.println("Thread " + threadName +
                        " timeout ");
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName +
                    " interrupted");        }

    }
}

public class ReentrantLockTimeoutExample {



    public static void main(String[] args) {
        ReentrantLock rLock = new ReentrantLock(true);
        System.out.println("starting threads ");

        for (int i = 0; i < 10 ; i++) {
            Thread t1 = new Thread(new CounterWorker("Thread-" + (i + 1), rLock));
            t1.start();
        }


    }



}
