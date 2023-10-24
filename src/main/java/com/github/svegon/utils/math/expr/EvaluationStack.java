package com.github.svegon.utils.math.expr;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

final class EvaluationStack {
    private final MathContext mc;
    private final List<Entry> deque = Lists.newArrayList(new Entry(this));

    public EvaluationStack(MathContext mc) {
        this.mc = mc;
    }

    public EvaluationStack() {
        this(MathContext.UNLIMITED);
    }

    public Entry peek() {
        return deque.get(deque.size() - 1);
    }

    public Entry push() {
        Entry entry = new Entry(this);
        deque.add(entry);
        return entry;
    }

    public Entry push(BigDecimal value) {
        Entry entry = peek();
        entry.push(value);
        return entry;
    }

    public Entry push(Operation operation) {
        Entry entry = peek();

        switch (operation.position()) {
            case BEFORE -> {
                if (entry.operation() == Operation.NONE) {
                    entry.operation(Operation.MUL);
                }

                push().operation(operation);
            }
            case BETWEEN -> {
                if (entry.operation().priority() > operation.priority()) {
                    push();
                    push(entry.pop()).operation(operation);
                } else {
                    entry.eval();
                    entry.operation(operation);
                }
            }
            case AFTER -> {

            }
        }

        return peek();
    }

    public Entry pop() {
        return push(deque.remove(deque.size() - 1).eval());
    }

    public BigDecimal eval() {
        while (deque.size() > 1) {
            pop();
        }

        return deque.get(0).eval();
    }

    public MathContext context() {
        return mc;
    }

    public static final class Entry {
        private final EvaluationStack stack;
        private final List<BigDecimal> operands = Lists.newArrayListWithCapacity(2);
        private Operation operation = Operation.NONE;

        private Entry(EvaluationStack stack) {
            this.stack = stack;
        }

        public void push(BigDecimal n) {
            operands.add(n);
        }

        public BigDecimal pop() {
            return operands.remove(operands.size() - 1);
        }

        public BigDecimal eval() {
            BigDecimal ret = operation.eval(operands, stack().context());

            operands.clear();
            operands.add(ret);
            operation = Operation.NONE;

            return operands.get(0);
        }

        public Operation operation() {
            return operation;
        }

        public EvaluationStack stack() {
            return stack;
        }

        public void operation(Operation operation) {
            if (this.operation != Operation.NONE) {
                throw new IllegalEvaluationException("operation already set");
            }

            this.operation = Preconditions.checkNotNull(operation);
        }
    }

    public enum OperatorPosition {
        BEFORE,
        BETWEEN,
        AFTER
    }

    public interface Operation {
        Operation NONE = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext context) {
                throw new IllegalEvaluationException("no operation selected");
            }

            @Override
            public int priority() {
                return Integer.MIN_VALUE;
            }

            @Override
            public OperatorPosition position() {
                throw new IllegalEvaluationException("no operation selected");
            }
        };
        Operation SUM = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count for sum");
                }

                return args.get(0).add(args.get(1), mc);
            }

            @Override
            public int priority() {
                return 0;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation SUB = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count for substract");
                }

                return args.get(0).subtract(args.get(1), mc);
            }

            @Override
            public int priority() {
                return 0;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation MUL = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count for multiplication");
                }

                return args.get(0).multiply(args.get(1), mc);
            }

            @Override
            public int priority() {
                return -1000;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation DIV = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count for division");
                }

                return args.get(0).divide(args.get(1), mc);
            }

            @Override
            public int priority() {
                return -1000;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation POW = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count");
                }

                return args.get(0).pow(args.get(1).intValueExact(), mc);
            }

            @Override
            public int priority() {
                return -5000;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation FLOOR_DIV = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException("incorrect argument count");
                }

                return args.get(0).divideToIntegralValue(args.get(1), mc);
            }

            @Override
            public int priority() {
                return -1000;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };
        Operation MOD = new Operation() {
            @Override
            public BigDecimal eval(List<BigDecimal> args, MathContext mc) {
                if (args.size() != 2) {
                    throw new IllegalEvaluationException();
                }

                return args.get(0).remainder(args.get(1), mc);
            }

            @Override
            public int priority() {
                return -1000;
            }

            @Override
            public OperatorPosition position() {
                return OperatorPosition.BETWEEN;
            }
        };

        BigDecimal eval(List<BigDecimal> args, MathContext context);

        int priority();

        OperatorPosition position();
    }
}
