package Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SynchronizedList {


    public static void main(String[] args) throws InterruptedException {

        Collection<Integer> noSyncCollection = new ArrayList<>();
        Runnable listNonSyncOperations = () -> {
            noSyncCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        };

        Thread thread1 = new Thread(listNonSyncOperations);
        Thread thread2 = new Thread(listNonSyncOperations);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        // should be 12 but it will could be  overided by threads
        System.out.println("normal arraylist : " +  noSyncCollection.size());

        Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
        Runnable listOperations = () -> {
            syncCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        };

        Thread thread3 = new Thread(listOperations);
        Thread thread4 = new Thread(listOperations);
        thread3.start();
        thread4.start();
        thread3.join();
        thread4.join();
        // will be 12
        System.out.println("sync arraylist : " + syncCollection.size());

    }


}
