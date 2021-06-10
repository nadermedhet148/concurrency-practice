package ThreadSafety;

class  ImmutableString {

    private final String string;

    ImmutableString(String string) {
        this.string = string;
    }

    public ImmutableString set(String string){
        return new ImmutableString(string);
    }

    public String get(){
        return this.string;
    }
}

public class Immutability {
    public static void main(String[] args) {

        ImmutableString immutableString = new ImmutableString("");

        Runnable runnable1 = () -> {
            ImmutableString string = immutableString.set("Thread in runnable1");
            try {
                Thread.sleep(5000);
                System.out.println(string.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable2 = () -> {

            try {
                Thread.sleep(2000);
                ImmutableString string = immutableString.set("Thread in runnable2");
                Thread.sleep(2000);
                System.out.println(string.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable3 = () -> {
            ImmutableString string = immutableString.set("Thread in runnable3");
            try {
                Thread.sleep(5000);
                System.out.println(string.get());
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
