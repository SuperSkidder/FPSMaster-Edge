package top.fpsmaster.modules.client;

import java.util.concurrent.*;

public class ClientThreadPool {
    private final ExecutorService executorService;

    public ClientThreadPool(int threadCount) {
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public <T> Future<T> execute(Callable<T> task) {
        return executorService.submit(task);
    }

    public Future<?> runnable(Runnable task) {
        return executorService.submit(task);
    }

    public void close() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
