package ThreadCancelation;

import java.time.Duration;
        import java.time.LocalDateTime;
        import java.util.concurrent.Callable;
        import java.util.concurrent.ExecutionException;
        import java.util.concurrent.Executors;
        import java.util.concurrent.ScheduledExecutorService;
        import java.util.concurrent.ScheduledFuture;
        import java.util.concurrent.TimeUnit;

public class CancelTaskInExecutor
{
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterOneMinute = now.plusMinutes(1);

        Duration duration = Duration.between(now, afterOneMinute);
        long delay = Math.abs(duration.toMillis());

        System.out.println("Task scheduled at : "+ LocalDateTime.now());

        ScheduledFuture<String> result = executor.schedule(new Task("Task-1"), delay, TimeUnit.MILLISECONDS);
        ScheduledFuture<String> result2 = executor.schedule(new Task("Task-2"), delay, TimeUnit.MILLISECONDS);

        System.out.println("Task is done : " + result.isDone());

        if(result.isDone() == false)
        {
            System.out.println("====Cancelling the task====");

            result.cancel(false);
        }

        System.out.println("Task is cancelled : " + result.isCancelled());

        System.out.println("Task is done : " + result.isDone());

        System.out.println("Task2 is done : " +  result2.isDone());

        executor.shutdown();
    }
}

class Task implements Callable<String>
{
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Task [" + name + "] executed on : " + LocalDateTime.now().toString());
        return "Task [" + name + "] is SUCCESS !!";
    }
}