package ThreadSafety;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ImprovedList<T> {
    private final List<T> list;

    public ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}

public class CompositionSafety {

    public static void main(String[] args) {
        ImprovedList<Integer> list = new  ImprovedList(new ArrayList());
        Runnable runnable1 = () -> {
            boolean added = list.putIfAbsent(new Integer(1));
            System.out.println(added);

        };

        Runnable runnable2 = () -> {

                boolean added = list.putIfAbsent(new Integer(1));
                System.out.println(added);
        };

        Runnable runnable3 = () -> {
                boolean added = list.putIfAbsent(new Integer(1));
                System.out.println(added);
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);

        thread1.start();
        thread2.start();
        thread3.start();


    }
}
