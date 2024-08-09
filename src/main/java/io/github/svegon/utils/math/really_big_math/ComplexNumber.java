package io.github.svegon.utils.math.really_big_math;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public final class ComplexNumber extends InfiniNumber {
    public static final ComplexNumber NEGATIVE_ONE = new ComplexNumber(InfiniFloat.NEGATIVE_ONE, InfiniFloat.ZERO);
    public static final ComplexNumber ZERO = new ComplexNumber(InfiniFloat.ZERO, InfiniFloat.ZERO);
    public static final ComplexNumber ONE = new ComplexNumber(InfiniFloat.ONE, InfiniFloat.ZERO);

    private final InfiniFloat realPart;
    private final InfiniFloat imagPart;
    private InfiniFloat abs;
    private ComplexNumber sign;
    private InfiniFloat arg;

    ComplexNumber(InfiniFloat realPart, InfiniFloat imagPart) {
        this.realPart = realPart;
        this.imagPart = imagPart;
    }

    ComplexNumber(InfiniFloat realPart) {
        this(realPart, InfiniFloat.ZERO);
        abs = realPart.abs();
        sign = realPart.isInteger() ? NEGATIVE_ONE : ONE;
        arg = realPart.isNegative() ? InfiniMathUtil.PI.neg() : InfiniFloat.ZERO;
    }

    ComplexNumber(InfiniNumber uncastReal, InfiniNumber uncastImag) {
        this((InfiniFloat) uncastReal, (InfiniFloat) uncastImag);
    }

    ComplexNumber(InfiniNumber uncastReal) {
        this((InfiniFloat) uncastReal);
    }

    @Override
    public int hashCode() {
        return 31 * real().hashCode() + imag().hashCode();
    }

    @Override
    public @NotNull InfiniNumber add(@NotNull Number other) {
        InfiniNumber n = InfiniMathUtil.cast(other);

        if (n instanceof Infinity) {
            return n;
        }

        ComplexNumber o = cast(n);

        return new ComplexNumber(real().add(o.real()), imag().add(o.imag()));
    }

    @Override
    public @NotNull InfiniNumber mul(@NotNull Number other) {
        InfiniNumber n = InfiniMathUtil.cast(other);

        if (n instanceof Infinity) {
            return imag().isZero() ? n.mul(real()) : InfiniMathUtil.NaN;
        }

        ComplexNumber o = cast(n);

        return new ComplexNumber(real().mul(o.real()).substract(imag().mul(o.imag())),
                real().mul(o.imag()).add(imag().mul(o.real())));
    }

    @Override
    public @NotNull InfiniNumber div(@NotNull Number divider) {
        InfiniNumber n = InfiniMathUtil.cast(divider);

        if (n instanceof Infinity) {
            return n;
        }

        ComplexNumber o = cast(n);
        InfiniFloat d = o.absSquared();

        return new ComplexNumber(real().mul(o.real()).add(imag().mul(o.imag())).div(d),
                real().mul(o.imag()).substract(imag().mul(o.real())).div(d));
    }

    @Override
    public @NotNull InfiniNumber  floorDiv(@NotNull Number other) {
        return abs().floorDiv(InfiniMathUtil.cast(other).abs());
    }

    @Override
    public @NotNull InfiniNumber pow(@NotNull Number exp) {
        InfiniNumber n = InfiniMathUtil.cast(exp);

        if (exp instanceof Infinity) {
            if (exp == InfiniMathUtil.NaN) {
                return InfiniMathUtil.NaN;
            }

            int cmp = abs().compareTo(ONE);

            if (cmp == 0) {
                return InfiniMathUtil.NaN;
            }

            return (cmp < 0) != (exp == InfiniMathUtil.NEGATIVE_INFINITY)
                    ? ZERO : InfiniMathUtil.POSITIVE_INFINITY;
        }

        ComplexNumber power = cast(n);

        if (power.imag().isZero()) {
            return new ComplexNumber(abs().pow(power.real())).mul(InfiniMathUtil.expi(arg().mul(power.real())));
        }

        InfiniFloat absSquared = absSquared();
        InfiniFloat arg = arg();

        return absSquared.pow(power.real().mul(InfiniFloat.HALF)).mul(InfiniMathUtil.exp(power.imag().neg().mul(arg)))
                .mul(InfiniMathUtil.expi((InfiniFloat) power.real().mul(arg).add(InfiniFloat.HALF.mul(power.imag())
                        .mul(absSquared.log()))));
    }

    @Override
    public @NotNull InfiniNumber log() {
        return new ComplexNumber(abs().log(), arg());
    }

    @Override
    public @NotNull Pair<InfiniNumber, InfiniNumber> divMod(@NotNull Number other) {
        InfiniNumber floor = floorDiv(other);
        return new ObjectObjectImmutablePair<>(floor, substract(floor.mul(other)));
    }

    @Override
    public @NotNull InfiniNumber lShift(long by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull InfiniNumber rShift(long by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ComplexNumber and(@NotNull Number other) {
        if (other instanceof ComplexNumber n) {
            return new ComplexNumber(real().and(n.real()), imag().and(n.imag()));
        }

        return new ComplexNumber(real().and(other));
    }

    @Override
    public @NotNull ComplexNumber or(@NotNull Number other) {
        if (other instanceof ComplexNumber n) {
            return new ComplexNumber(real().or(n.real()), imag().or(n.imag()));
        }

        return new ComplexNumber(real().or(other), imag());
    }

    @Override
    public @NotNull ComplexNumber xor(@NotNull Number other) {
        if (other instanceof ComplexNumber n) {
            return new ComplexNumber(real().xor(n.real()), imag().xor(n.imag()));
        }

        return new ComplexNumber(real().xor(other), imag());
    }

    @Override
    public @NotNull ComplexNumber invert() {
        return new ComplexNumber(real().invert(), imag().invert());
    }

    @Override
    public @NotNull ComplexNumber sign() {
        if (sign == null) {
            sign = compareTo(ZERO) == 0 ? ZERO : arg != null ? (ComplexNumber) ONE.div(InfiniMathUtil.expi(arg))
                    : (ComplexNumber) abs().div(this);
        }

        return sign;
    }

    @Override
    public @NotNull InfiniFloat abs() {
        if (abs == null) {
            abs = (InfiniFloat) absSquared().pow(InfiniFloat.HALF);
        }

        return abs;
    }

    @Override
    public @NotNull ComplexNumber neg() {
        return new ComplexNumber(real().neg(), imag().neg());
    }

    @Override
    public @NotNull InfiniNumber floor() {
        return new ComplexNumber(real().floor(), imag().floor());
    }

    @Override
    public @NotNull ComplexNumber ceil() {
        return new ComplexNumber(real().ceil(), imag().ceil());
    }

    @Override
    public @NotNull ComplexNumber round(long decimalLength) {
        return new ComplexNumber(real().round(decimalLength), imag().round(decimalLength));
    }

    @Override
    public long size() {
        return real().size() + imag().size();
    }

    @Override
    public int compareTo(@NotNull Number o) {
        if (o instanceof ComplexNumber complex) {
            InfiniFloat real = complex.real();
            int c = real().compareTo(real);

            if (c == 0) {
                c = imag().compareTo(complex.real());
            }

            return c;
        }

        return real().compareTo(o);
    }

    @Override
    public long longValue() {
        return abs().longValue();
    }

    @Override
    public double doubleValue() {
        return abs().doubleValue();
    }

    public InfiniFloat real() {
        return realPart;
    }

    public InfiniFloat imag() {
        return imagPart;
    }

    public InfiniFloat arg() {
        if (arg == null) {
            arg = InfiniMathUtil.arctg(imag(), real());
        }

        return arg;
    }

    public InfiniFloat absSquared() {
        return real().mul(real()).add(imag().mul(imag()));
    }

    private static ComplexNumber cast(InfiniNumber n) {
        if (n instanceof ComplexNumber) {
            return (ComplexNumber) n;
        } else if (n instanceof InfiniFloat) {
            return new ComplexNumber(n);
        } else {
            throw new IllegalStateException();
        }
    }

    public static ComplexNumber valueOf(InfiniFloat real, InfiniFloat imag) {
        return new ComplexNumber(Preconditions.checkNotNull(real), Preconditions.checkNotNull(imag));
    }

    public static ComplexNumber valueOf(InfiniFloat real) {
        return new ComplexNumber(Preconditions.checkNotNull(real), InfiniFloat.ZERO);
    }
}
