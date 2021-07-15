package Tasks;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerThread implements Runnable {
    private String message;
    public WorkerThread(String s){
        this.message=s;
    }
    public void run() {
        System.out.println(Thread.currentThread().getName()+" (Start) message = "+message);
        processMessage();
        System.out.println(Thread.currentThread().getName()+" (End)  message = "+message);
    }
    private void processMessage() {
        try {  Thread.sleep(2000);  } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
public class ThreadPoolExample {

    public static void main(String[] args) {
        System.out.println("/////////////// FixedThreadPool ///////////////");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads in newFixedThreadPool");
        System.out.println("//////////////////////////////");

        System.out.println("/////////////// CachedThreadPool ///////////////");

        ExecutorService executorCache = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i);
            executorCache.execute(worker);
        }
        executorCache.shutdown();
        while (!executorCache.isTerminated()) {
        }
        System.out.println("Finished all threads in newCachedThreadPool");

        System.out.println("//////////////////////////////");

        System.out.println("/////////////// SingleThreadExecutor ///////////////");

        ExecutorService executorSingle = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i);
            executorSingle.execute(worker);
        }
        executorSingle.shutdown();
        while (!executorSingle.isTerminated()) {
        }
        System.out.println("Finished all threads in newSingleThreadExecutor");


    }
}
