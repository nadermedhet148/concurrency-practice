package ConcurrentTesting;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

class SimpleBlockingQueue<T> {
    private List<T> queue = new LinkedList<T>();

    public int getSize() {
        synchronized (queue) {
            return queue.size();
        }
    }

    public void put(T obj) {
        synchronized (queue) {
            queue.add(obj);
            queue.notify();
        }
    }

    public T get() throws InterruptedException {
        while (true) {
            synchronized (queue) {
                if (queue.isEmpty()) {
                    queue.wait();
                } else {
                    return queue.remove(0);
                }
            }
        }
    }
}

class BlockingThread extends Thread {
    private SimpleBlockingQueue queue;
    private boolean wasInterrupted = false;
    private boolean reachedAfterGet = false;
    private boolean throwableThrown;

    public BlockingThread(SimpleBlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            try {
                queue.get();
            } catch (InterruptedException e) {
                wasInterrupted = true;
            }
            reachedAfterGet = true;
        } catch (Throwable t) {
            throwableThrown = true;
        }
    }

    public boolean isWasInterrupted() {
        return wasInterrupted;
    }

    public boolean isReachedAfterGet() {
        return reachedAfterGet;
    }

    public boolean isThrowableThrown() {
        return throwableThrown;
    }
}

public class SimpleBlockingQueueTesting {

    @Test
    public void testPutOnEmptyQueueBlocks() throws InterruptedException {
        final SimpleBlockingQueue queue = new SimpleBlockingQueue();
        BlockingThread blockingThread = new BlockingThread(queue);
        blockingThread.start();
        Thread.sleep(5000);
        assertThat(blockingThread.isReachedAfterGet(), is(false));
        assertThat(blockingThread.isWasInterrupted(), is(false));
        assertThat(blockingThread.isThrowableThrown(), is(false));
        queue.put(new Object());
        Thread.sleep(1000);
        assertThat(blockingThread.isReachedAfterGet(), is(true));
        assertThat(blockingThread.isWasInterrupted(), is(false));
        assertThat(blockingThread.isThrowableThrown(), is(false));
        blockingThread.join();
    }


    @Test
    public void testParallelInsertionAndConsumption() throws InterruptedException, ExecutionException {
        Integer NUM_THREADS = 10;
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<Integer>();
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        List<Future<Integer>> futuresPut = new ArrayList<Future<Integer>>();
        for (int i = 0; i < 3; i++) {
            Future<Integer> submit = threadPool.submit(new Callable<Integer>() {
                public Integer call() {
                    int sum = 0;
                    for (int i = 0; i < 1000; i++) {
                        int nextInt = ThreadLocalRandom.current().nextInt(100);
                        queue.put(nextInt);
                        sum += nextInt;
                    }
                    latch.countDown();
                    return sum;
                }
            });
            futuresPut.add(submit);
        }
        List<Future<Integer>> futuresGet = new ArrayList<Future<Integer>>();
        for (int i = 0; i < 3; i++) {
            Future<Integer> submit = threadPool.submit(new Callable<Integer>() {
                public Integer call() {
                    int count = 0;
                    try {
                        for (int i = 0; i < 1000; i++) {
                            Integer got = queue.get();
                            count += got;
                        }
                    } catch (InterruptedException e) {

                    }
                    latch.countDown();
                    return count;
                }
            });
            futuresGet.add(submit);
        }
        latch.await();
        int sumPut = 0;
        for (Future<Integer> future : futuresPut) {
            sumPut += future.get();
        }
        int sumGet = 0;
        for (Future<Integer> future : futuresGet) {
            sumGet += future.get();
        }
        assertThat(sumPut, is(sumGet));
    }

    @Test
    public void testPerformance() throws InterruptedException {
        Integer THREADS_MAX = 10;
        final Integer ITERATIONS = 1;
        for (int numThreads = 1; numThreads < THREADS_MAX; numThreads++) {
            long startMillis = System.currentTimeMillis();
            final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<Integer>();
            ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
            for (int i = 0; i < numThreads; i++) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        for (long i = 0; i < ITERATIONS; i++) {
                            int nextInt = ThreadLocalRandom.current().nextInt(100);
                            try {
                                queue.put(nextInt);
                                nextInt = queue.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.MINUTES);
            long totalMillis = System.currentTimeMillis() - startMillis;
            double throughput = (double)(numThreads * ITERATIONS * 2) / (double) totalMillis;
            System.out.println(String.format("%s with %d threads: %dms (throughput: %.1f ops/s)", LinkedBlockingQueue.class.getSimpleName(), numThreads, totalMillis, throughput));
        }
    }

}
