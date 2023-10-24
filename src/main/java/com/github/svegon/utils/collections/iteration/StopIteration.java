package com.github.svegon.utils.collections.iteration;

import com.github.svegon.utils.multithreading.CallTerminater;

import java.util.NoSuchElementException;

public final class StopIteration extends NoSuchElementException {
    public static final StopIteration INSTANCE = new StopIteration();

    @Override
    public synchronized Throwable fillInStackTrace() {
        setStackTrace(CallTerminater.EMPTY_STACK_TRACE);
        return this;
    }
}
