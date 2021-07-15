package Examples;



import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

class CouncurentDirNamesCollector {


    private static class SizeOfFileTask extends RecursiveTask<Long> {

        private static final long serialVersionUID = -196522408291343951L;

        private final File file;
        private  final Collection<String> names;

        public SizeOfFileTask(final File file , Collection<String> names ) {
            this.file = Objects.requireNonNull(file);
            this.names = names;
        }

        @Override
        protected Long compute() {

            if (file.isFile() && file.getName().contains("txt")) {
                this.names.add(file.getPath());
            }

            final List<SizeOfFileTask> tasks = new ArrayList<>();
            final File[] children = file.listFiles();
            if (children != null) {
                for (final File child : children) {
                    final SizeOfFileTask task = new SizeOfFileTask(child , names);
                    task.fork();
                    tasks.add(task);
                }
            }

            long size = 0;
            for (final SizeOfFileTask task : tasks) {
                size += task.join();
            }

            return size;
        }
    }

    public static long sizeOf(final File file , Collection<String> names ) {
        final ForkJoinPool pool = new ForkJoinPool();
        try {
            return pool.invoke(new SizeOfFileTask(file,names));
        } finally {
            pool.shutdown();
        }
    }


}


public class MultipleFilesManger {

    public static void main(String[] args) {
        Collection<String> txtFiles = Collections.synchronizedCollection(new ArrayList<>());
        ConcurrentHashMap<String , List<String>> filesReads = new ConcurrentHashMap<>();
        File file = new File("/home/nader/Downloads");

        ExecutorService executorCache = Executors.newCachedThreadPool();
         CompletionService completionService = new ExecutorCompletionService(executorCache);

        CouncurentDirNamesCollector.sizeOf(new File("/home/nader/Downloads"),txtFiles);


        for ( String filePath : txtFiles) {
            Runnable run = ()->{
                try {
                    Future<List<String>> fp = completionService.take();
                    filesReads.put(filePath , fp.get());

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            };

            Callable call = ()-> Files.readAllLines(Paths.get(filePath) , StandardCharsets.UTF_8);

            completionService.submit(call);



            new Thread(run).start();
        }


    }

}
