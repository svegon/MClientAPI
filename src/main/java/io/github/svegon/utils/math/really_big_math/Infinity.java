package io.github.svegon.utils.math.really_big_math;

import io.github.svegon.utils.annotations.Unfinished;
import io.github.svegon.utils.interfaces.IdentityComparable;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public abstract class Infinity extends InfiniNumber implements IdentityComparable {
    private final long asLong;
    private final double asDouble;
    private final int hashCode;
    private final InfiniFloat bitRepresentation;

    Infinity(double asDouble) {
        this.asLong = (long) asDouble;
        this.asDouble = asDouble;
        this.hashCode = Double.hashCode(asDouble);
        this.bitRepresentation = InfiniFloat.valueOf(Double.doubleToLongBits(asDouble));
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public Infinity clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public @NotNull InfiniNumber add(@NotNull Number other) {
        return other == InfiniMathUtil.NaN || other == neg() ? InfiniMathUtil.NaN : this;
    }

    @Override
    public @NotNull InfiniNumber substract(@NotNull Number other) {
        return other == this || other == InfiniMathUtil.NaN ? InfiniMathUtil.NaN : this;
    }

    @Override
    public @NotNull InfiniNumber mul(@NotNull Number other) {
        if (other == InfiniMathUtil.NaN || other.equals(0)) {
            return InfiniMathUtil.NaN;
        }

        if (InfiniMathUtil.compare(other, 0) < 0) {
            return neg();
        }

        return this;
    }

    @Override
    public @NotNull InfiniNumber div(@NotNull Number divider) {
        if (divider instanceof Infinity || divider == InfiniMathUtil.NaN) {
            return InfiniMathUtil.NaN;
        }

        if (InfiniMathUtil.compare(divider, 0) < 0) {
            return neg();
        }

        return this;
    }

    @Override
    public @NotNull InfiniNumber floorDiv(@NotNull Number other) {
        return div(other);
    }

    @Override
    public @NotNull InfiniNumber pow(@NotNull Number exp) {
        if (exp == InfiniMathUtil.NaN || exp == InfiniMathUtil.POSITIVE_INFINITY) {
            return (InfiniNumber) exp;
        }

        if (exp.equals(0)) {
            return InfiniFloat.valueOf(1);
        }

        return InfiniMathUtil.compare(exp, 0) < 0 ? InfiniFloat.ZERO : this;
    }

    @Override
    public @NotNull InfiniNumber log() {
        return InfiniMathUtil.NaN;
    }

    @Override
    public @NotNull InfiniNumber log(@NotNull Number base) {
        return InfiniMathUtil.NaN;
    }

    @Override
    public @NotNull InfiniNumber mod(@NotNull Number other) {
        return other == this ? InfiniFloat.valueOf(0) : this;
    }

    @Override
    public @Unfinished @NotNull Pair<? extends InfiniNumber, ? extends InfiniNumber>
    divMod(@NotNull Number other) {
        return new ObjectObjectImmutablePair<>(floorDiv(other), mod(other));
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber lShift(long by) {
        return bitRepresentation.lShift(by);
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber rShift(long by) {
        return bitRepresentation.rShift(by);
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber and(@NotNull Number other) {
        return bitRepresentation.and(other);
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber or(@NotNull Number other) {
        return bitRepresentation.or(other);
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber xor(@NotNull Number other) {
        return bitRepresentation.xor(other);
    }

    @Deprecated
    @Override
    public @NotNull InfiniNumber invert() {
        return bitRepresentation.invert();
    }

    @Override
    public @NotNull InfiniNumber abs() {
        return InfiniMathUtil.POSITIVE_INFINITY;
    }

    @Override
    public @NotNull InfiniNumber floor() {
        return this;
    }

    @Override
    public @NotNull InfiniNumber ceil() {
        return this;
    }

    @Override
    public @NotNull InfiniNumber round(long decimalLength) {
        return this;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public long longValue() {
        return asLong;
    }

    @Override
    public double doubleValue() {
        return asDouble;
    }
}
