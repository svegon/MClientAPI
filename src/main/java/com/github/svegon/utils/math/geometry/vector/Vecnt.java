package com.github.svegon.utils.math.geometry.vector;

import com.github.svegon.utils.fast.util.doubles.immutable.collection.ImmutableDoubleList;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleSpliterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.RandomAccess;

@Immutable
public abstract class Vecnt<A, T extends Vecnt<A, T>> extends AbstractDoubleList implements RandomAccess {
    Vecnt() {

    }

    @Override
    public final DoubleSpliterator spliterator() {
        return new ImmutableDoubleList.IndexBasedImmutableSpliterator(this);
    }

    /**
     * named for compatibility with {@linkplain DoubleList} methods
     * @return
     */
    @Contract(value = "-> new", pure = true)
    public abstract A toPrimitiveArray();

    public abstract A toPrimitiveArray(@Nullable A a);

    @Contract(value = "_ -> new", pure = true)
    public abstract T add(T other);

    @Contract(value = "_ -> new", pure = true)
    public abstract T substract(T other);

    public abstract T multiply(double multiplier);

    @Contract(value = "_ -> new", pure = true)
    public abstract T multiplyEach(T other);

    public T divide(double divider) {
        return multiply(1 / divider);
    }

    @Contract(value = "-> new", pure = true)
    public T neg() {
        return multiply(-1);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return scalarProduct(this);
    }

    public double distanceTo(T vector) {
        return Math.sqrt(squaredDistanceTo(vector));
    }

    public double squaredDistanceTo(T vector) {
        return substract(vector).lengthSquared();
    }

    public double scalarProduct(Vecnt<?, ?> multiplier) {
        if (size() != multiplier.size()) {
            throw new IllegalArgumentException();
        }

        double product = 0;

        for (int i = 0; i < size(); i++) {
            product += getDouble(i) * multiplier.getDouble(i);
        }

        return product;
    }
}
