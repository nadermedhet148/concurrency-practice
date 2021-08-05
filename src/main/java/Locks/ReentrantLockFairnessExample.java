package Locks;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class SharedResource{
    static int count = 0;
}

class Counter implements Runnable {
    private String threadName;
    ReentrantLock lock;
    Counter(String threadName, ReentrantLock lock){
        this.threadName = threadName;
        this.lock = lock;
    }
    @Override
    public void run() {
        System.out.println("In Counter run method, thread " + threadName
                + " is waiting to get lock");
        // acquiring the lock
        lock.lock();
        try {
            System.out.println("Thread " + threadName + " has got lock");
            SharedResource.count++;
            System.out.println("Thread " + threadName +
                    " Count " + SharedResource.count);
        } finally{
            System.out.println("Thread " + threadName
                    + " releasing lock");
            // releasing the lock
            lock.unlock();
        }
    }
}

public class ReentrantLockFairnessExample {



    public static void main(String[] args) {
        ReentrantLock rLock = new ReentrantLock(true);
        System.out.println("starting threads ");

        for (int i = 0; i < 100 ; i++) {
            Thread t1 = new Thread(new Counter("Thread-" + (i + 1), rLock));
            t1.start();
        }


    }



}
