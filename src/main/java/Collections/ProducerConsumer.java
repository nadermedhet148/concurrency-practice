package Collections;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {

    public static void main(String args[]){

        // will block after 5 then wait till consume
        BlockingQueue sharedQueue = new LinkedBlockingQueue<>(5);

        Thread prodThread = new Thread(new Producer(sharedQueue , "p1"));
        Thread prodThread2 = new Thread(new Producer(sharedQueue, "p2"));
        Thread consThread = new Thread(new Consumer(sharedQueue));

        prodThread.start();
        prodThread2.start();
        consThread.start();
    }

}

class Producer implements Runnable {

    private final BlockingQueue sharedQueue;
    private  final String id;
    public Producer(BlockingQueue sharedQueue, String id) {
        this.sharedQueue = sharedQueue;
        this.id = id;
    }

    @Override
    public void run() {
        for(int i=0; i<10; i++){
            try {
                System.out.println( id + " Produced: " + i);
                sharedQueue.put(i);
            } catch (InterruptedException ex) {
            }
        }
    }

}

class Consumer implements Runnable{

    private final BlockingQueue sharedQueue;

    public Consumer (BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("Consumed: "+ sharedQueue.take());
            } catch (InterruptedException ex) {
            }
        }
    }


}


