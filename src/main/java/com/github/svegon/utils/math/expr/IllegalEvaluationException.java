package com.github.svegon.utils.math.expr;

public class IllegalEvaluationException extends RuntimeException {
    public IllegalEvaluationException() {
        super();
    }

    public IllegalEvaluationException(String s) {
        super(s);
    }

    public IllegalEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalEvaluationException(Throwable cause) {
        super(cause);
    }
}
