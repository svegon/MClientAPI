package io.github.svegon.utils.multithreading;

public final class CallTerminater extends RuntimeException {
    public static final StackTraceElement[] EMPTY_STACK_TRACE = {};
    public static final CallTerminater INSTANCE = new CallTerminater();

    private CallTerminater() {

    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        setStackTrace(EMPTY_STACK_TRACE);
        return this;
    }
}
