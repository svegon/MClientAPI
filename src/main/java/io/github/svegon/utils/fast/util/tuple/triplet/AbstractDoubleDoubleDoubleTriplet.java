package io.github.svegon.utils.fast.util.tuple.triplet;

public abstract class AbstractDoubleDoubleDoubleTriplet extends AbstractTriplet<Double, Double, Double>
        implements DoubleDoubleDoubleTriplet {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DoubleDoubleDoubleTriplet triplet)) {
            return super.equals(obj);
        }

        return Double.doubleToLongBits(firstDouble()) == Double.doubleToLongBits(triplet.firstDouble())
                && Double.doubleToLongBits(secondDouble()) == Double.doubleToLongBits(triplet.secondDouble())
                && Double.doubleToLongBits(thirdDouble()) == Double.doubleToLongBits(triplet.thirdDouble());
    }

    @Override
    public int hashCode() {
        return 31 * (31 * Double.hashCode(firstDouble()) + Double.hashCode(secondDouble()))
                + Double.hashCode(thirdDouble());
    }

    @Override
    public String toString() {
        return "<" + firstDouble() + ", " + secondDouble() + ", " + thirdDouble() + ">";
    }
}
