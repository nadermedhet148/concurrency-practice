package ThreadSafety;

import java.util.concurrent.atomic.AtomicReference;

public class ThreadConfinement {

    public static void main(String[] args) {
        ThreadLocal<String> stringHolder = new ThreadLocal<>();
        AtomicReference<String> s = new AtomicReference<>("");

        Runnable runnable1 = () -> {
            stringHolder.set("Thread in runnable1");
            // this will be lost
            s.set("t1");
            try {
                Thread.sleep(5000);
                System.out.println(stringHolder.get());
                System.out.println(s.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable2 = () -> {
            stringHolder.set("Thread in runnable2");
            // this will be lost
            s.set("t2");
            try {
                Thread.sleep(2000);
                stringHolder.set("string in runnable2 changed");
                Thread.sleep(2000);
                System.out.println(stringHolder.get());
                System.out.println(s.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable3 = () -> {
            stringHolder.set("Thread in runnable3");
            s.set("t3");
            try {
                Thread.sleep(5000);
                System.out.println(stringHolder.get());
                System.out.println(s.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);

        thread1.start();
        thread2.start();
        thread3.start();

    }

}
