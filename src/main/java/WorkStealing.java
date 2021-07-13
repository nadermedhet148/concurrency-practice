import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


class DirSize {


    public static long sizeOf(final File file) {

        long size = 0;

        /* Ignore files which are not files and dirs */
        if (file.isFile()) {
            size = file.length();
        } else {
            final File[] children = file.listFiles();
            if (children != null) {
                for (final File child : children) {
                    size += DirSize.sizeOf(child);
                }
            }
        }

        return size;
    }
}

class CouncurentDirSize {


    private static class SizeOfFileTask extends RecursiveTask<Long> {

        private static final long serialVersionUID = -196522408291343951L;

        private final File file;

        public SizeOfFileTask(final File file) {
            this.file = Objects.requireNonNull(file);
        }

        @Override
        protected Long compute() {

            if (file.isFile()) {
                return file.length();
            }

            final List<SizeOfFileTask> tasks = new ArrayList<>();
            final File[] children = file.listFiles();
            if (children != null) {
                for (final File child : children) {
                    final SizeOfFileTask task = new SizeOfFileTask(child);
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

    public static long sizeOf(final File file) {
        final ForkJoinPool pool = new ForkJoinPool();
        try {
            return pool.invoke(new SizeOfFileTask(file));
        } finally {
            pool.shutdown();
        }
    }

    private CouncurentDirSize() {}

}

public class WorkStealing {
    public static void main(String[] args) {
        long start = System.nanoTime();
        final long size = DirSize.sizeOf(new File("/home/nader/Downloads"));
        long end = System.nanoTime();
        System.out.println("Single thread : " +  (end - start));

        long start2 = System.nanoTime();
        final long size2 = CouncurentDirSize.sizeOf(new File("/home/nader/Downloads"));
        long end2 = System.nanoTime();
        System.out.println("with work steel thread : " +  (end2 - start2));

    }
}
