package io.github.svegon.utils.math.geometry.space;

import io.github.svegon.utils.math.geometry.space.shape.Shape;
import io.github.svegon.utils.math.geometry.vector.Vecnt;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public abstract class Blocknt<A, V extends Vecnt<A, V>, T extends Blocknt<A, V, T>> {
    Blocknt() {

    }

    public abstract V getMinPos();

    public abstract V getMaxPos();

    public V getCenter() {
        return getMaxPos().add(getMinPos()).multiply(0.5);
    }

    public V getSizes() {
        return getMaxPos().substract(getMinPos());
    }

    public abstract T stretch(@NotNull V sizes);

    public abstract T shrink(@NotNull V sizes);

    public abstract T expand(@NotNull V sizes);

    public T contract(@NotNull V sizes) {
        return expand(sizes.neg());
    }

    public abstract T offset(@NotNull V values);

    public abstract T union(@NotNull T other);

    /**
     * asserts this.interests(other)
     *
     * @param other
     * @return
     */
    public abstract T intersect(@NotNull T other);

    public abstract boolean intersects(@NotNull T other);

    public abstract boolean intersects(@NotNull V pos0, @NotNull V pos1);

    public abstract boolean contains(@NotNull V point);

    public abstract Shape getShape();

    /**
     * a special version of Math.min which supresses NaNs and flattens 0's
     *
     * @param a
     * @param b
     * @return
     */
    static double min(double a, double b) {
        if (Double.isNaN(a)) {
            if (Double.isNaN(b)) {
                throw new ArithmeticException();
            }

            return b;
        }

        return b < a ? b : a;
    }

    /**
     * a special version of Math.max which supresses NaNs and flattens 0's
     *
     * @param a
     * @param b
     * @return
     */
    static double max(double a, double b) {
        if (Double.isNaN(a)) {
            if (Double.isNaN(b)) {
                throw new ArithmeticException();
            }

            return b;
        }

        return b > a ? b : a;
    }
}
