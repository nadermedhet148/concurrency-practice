public class ReorderExample {
    private int a = 2;
    private boolean flg = false;

    public void method1() {
        a = 1;
        flg = true;
    }

    public void method2() {
        if (flg) {
            System.out.println("a = " + a);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ReorderExample reorderExample = new ReorderExample();
            Thread thread1 = new Thread(() -> {
                reorderExample.method1();
            });
            Thread thread2 = new Thread(() -> {
                reorderExample.method2();
            });
            thread1.start();
            thread2.start();
        }
    }
}

class ReorderPrevention {
    private int a = 2;
    private boolean flg = false;

    public synchronized void method1() {
        a = 1;
        flg = true;
    }

    public synchronized void method2() {
        if (flg) {
            System.out.println("a = " + a);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ReorderPrevention reorderExample = new ReorderPrevention();
            Thread thread1 = new Thread(() -> {
                reorderExample.method1();
            });
            Thread thread2 = new Thread(() -> {
                reorderExample.method2();
            });
            thread1.start();
            thread2.start();
        }
    }
}