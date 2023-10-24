package com.github.svegon.utils.multithreading;

import java.util.concurrent.ThreadFactory;

public final class SimpleSyncedExecutor extends DedicatedThreadExecutor<Runnable> {
    public SimpleSyncedExecutor(String name, ThreadFactory factory) {
        super(name, factory);
    }

    public SimpleSyncedExecutor(String name) {
        super(name);
    }

    @Override
    protected Runnable createTask(Runnable runnable) {
        return runnable;
    }
}
