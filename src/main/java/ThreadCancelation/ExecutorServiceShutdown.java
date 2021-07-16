package ThreadCancelation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ExecutorServiceShutdown {
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();

        System.out.println("WorkerTasks scheduled at : "+ LocalDateTime.now());

        ScheduledFuture<String> result1 = executor.schedule(new WorkerTask("WorkerTask-1"),
                Duration.between(now, now.plusSeconds(10)).toMillis(),
                TimeUnit.MILLISECONDS);

        ScheduledFuture<String> result2 = executor.schedule(new WorkerTask("WorkerTask-2"),
                Duration.between(now, now.plusSeconds(20)).toMillis(),
                TimeUnit.MILLISECONDS);

        ScheduledFuture<String> result3 = executor.schedule(new WorkerTask("WorkerTask-3"),
                Duration.between(now, now.plusSeconds(30)).toMillis(),
                TimeUnit.MILLISECONDS);

        Thread.sleep(15_000);

        executor.shutdown();

        System.out.println("Task-1 is done : " + result1.isDone());
        System.out.println("Task-2 is done : " + result2.isDone());
        System.out.println("Task-3 is done : " + result3.isDone());

        System.out.println("***********Waiting for tasks to be complete*********");

        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("***********All tasks are completed nows*********");

        System.out.println("Task-1 is done : " + result1.isDone());
        System.out.println("Task-2 is done : " + result2.isDone());
        System.out.println("Task-3 is done : " + result3.isDone());
    }

    public static void main2(String[] args) throws InterruptedException, ExecutionException
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();

        System.out.println("WorkerTasks scheduled at : "+ LocalDateTime.now());

        ScheduledFuture<String> result1 = executor.schedule(new WorkerTask("WorkerTask-1"),
                Duration.between(now, now.plusSeconds(10)).toMillis(),
                TimeUnit.MILLISECONDS);

        ScheduledFuture<String> result2 = executor.schedule(new WorkerTask("WorkerTask-2"),
                Duration.between(now, now.plusSeconds(20)).toMillis(),
                TimeUnit.MILLISECONDS);

        ScheduledFuture<String> result3 = executor.schedule(new WorkerTask("WorkerTask-3"),
                Duration.between(now, now.plusSeconds(30)).toMillis(),
                TimeUnit.MILLISECONDS);

        Thread.sleep(15_000);

        executor.shutdownNow();

        System.out.println("Task-1 is done : " + result1.isDone());
        System.out.println("Task-2 is done : " + result2.isDone());
        System.out.println("Task-3 is done : " + result3.isDone());

        System.out.println("***********Waiting for tasks to be complete*********");
        // will not effect
        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("***********All tasks are completed nows*********");

        System.out.println("Task-1 is done : " + result1.isDone());
        System.out.println("Task-2 is done : " + result2.isDone());
        System.out.println("Task-3 is done : " + result3.isDone());
    }
}
class WorkerTask implements Callable<String>
{
    private final String name;

    public WorkerTask(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.println("WorkerTask [" + name + "] executed on : " + LocalDateTime.now().toString());
        return "WorkerTask [" + name + "] is SUCCESS !!";
    }
}