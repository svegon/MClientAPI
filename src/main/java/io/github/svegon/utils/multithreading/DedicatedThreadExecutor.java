package io.github.svegon.utils.multithreading;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

public abstract class DedicatedThreadExecutor<R extends Runnable> extends ReentrantThreadExecutor<R> {
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = Executors.defaultThreadFactory();

    private final Thread thread;
    private final Semaphore s = new Semaphore(0);

    public DedicatedThreadExecutor(final String name, final ThreadFactory factory) {
        super(name);

        thread = factory.newThread(() -> {
            for (;;) {
                DedicatedThreadExecutor.this.runTasks();
                s.acquireUninterruptibly();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public DedicatedThreadExecutor(final String name) {
        this(name, DEFAULT_THREAD_FACTORY);
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public void send(R runnable) {
        super.send(runnable);

        s.release();
    }
}
