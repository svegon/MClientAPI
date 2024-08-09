package io.github.svegon.utils.math.really_big_math;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.ListUtil;
import io.github.svegon.utils.collections.collecting.CollectingUtil;
import io.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMaps;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Immutable
public class InfiniFloat extends InfiniNumber {
    private static final Long2ObjectMap<InfiniFloat> LONG_VALUE_CACHE =
            Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
    private static final Double2ObjectMap<InfiniNumber> DOUBLE_VALUE_CACHE =
            Double2ObjectMaps.synchronize(new Double2ObjectOpenHashMap<>());
    /**
     * referencing InfiniMathUtil here would cause an infinite loop import
     */
    private static final int DOUBLE_NON_FRACTION_SIZE = Double.SIZE - 52;
    private static final int POSSIBLE_LEADING_ZEROES = DOUBLE_NON_FRACTION_SIZE - 1;

    public static final InfiniFloat NEGATIVE_ONE = valueOf(-1);
    public static final InfiniFloat ZERO = valueOf(0);
    public static final InfiniFloat ONE = valueOf(1);
    public static final InfiniFloat TWO = valueOf(2);
    public static final InfiniFloat TEN = valueOf(10);
    public static final InfiniFloat HALF = (InfiniFloat) valueOf(0.5);
    public static final InfiniFloat ONE_TENTH = (InfiniFloat) ONE.div(TEN);

    private static final long[] ZERO_ARRAY = ZERO.intBits;
    private static final Interval ZERO_TO_TWO = Interval.interval(ZERO, TWO, true, true);

    public static final InfiniFloat NEGATIVE_ZERO = new InfiniFloat(true, ZERO_ARRAY);
    public static final InfiniFloat DIV_PRECISION = ONE.rShift(9999);

    private final boolean sign;
    private final long[] intBits;
    private final long[] floatBits;
    private double doubleValue = Double.NaN;
    private int hashCode;
    private boolean initHashCode = true;
    private byte isPowOf2;

    InfiniFloat(boolean sign, long[] intBits, long[] floatBits) {
        this.sign = sign;
        this.intBits = intBits;
        this.floatBits = floatBits;
    }

    InfiniFloat(boolean sign, long[] intBits) {
        this(sign, intBits, LongArrays.EMPTY_ARRAY);
    }

    @Override
    public int compareTo(@NotNull Number o) {
        if (this == o) {
            return 0;
        }

        if (o == InfiniMathUtil.NaN) {
            return isNegative() ? 1 : -1;
        }

        if (o == InfiniMathUtil.NEGATIVE_INFINITY) {
            return 1;
        }

        if (o == InfiniMathUtil.POSITIVE_INFINITY) {
            return -1;
        }

        if (!(o instanceof InfiniFloat other)) {
            return -InfiniMathUtil.cast(o).compareTo(this);
        }

        if (other.isZero()) {
            if (isZero()) {
                return 0;
            }

            return isNegative() ? -1 : 1;
        }

        if (isNegative() != other.isNegative()) {
            return isNegative() ? -1 : 1;
        }

        if (intBits.length != other.intBits.length) {
            return intBits.length < other.intBits.length == isNegative() ? -1 : 1;
        }

        int length = intBits.length;
        int c;

        for (int i = 1; i < length; i++) {
            c = Long.compareUnsigned(intBits[i], other.intBits[i]);

            if (c != 0) {
                return c;
            }
        }

        length = Math.min(floatBits.length, other.floatBits.length);

        for (int i = 0; i < length; i++) {
            c = Long.compareUnsigned(floatBits[i], other.floatBits[i]);

            if (c != 0) {
                return c;
            }
        }

        if (floatBits.length == other.floatBits.length) {
            return 0;
        }

        return floatBits.length < other.floatBits.length == isNegative() ? -1 : 1;
    }

    @Override
    public long longValue() {
        return intBits[intBits.length - 1];
    }

    /**
     * Returns the closest possible doubles representation of this InfiniFloat
     * Due to difference in doubles and InfiniFloat arithmetics this method
     * is quiete ineffective and should be avoided where possible.
     * @return doubles with the same value at 52-bit precision
     */
    @Override
    public double doubleValue() {
        if (Double.isNaN(doubleValue)) {
            doubleValue = initDouble();
        }

        return doubleValue;
    }

    private double initDouble() {
        int intBitsLength = intBits.length;
        int floatBitsLength = floatBits.length;
        long firstBits = intBits[0];

        long longBits;

        if (firstBits != 0) {
            int leadingZeroes = Long.numberOfLeadingZeros(firstBits);
            long exponent = (long) intBitsLength * Long.SIZE - leadingZeroes;

            if (exponent > InfiniMathUtil.DOUBLE_MAX_EXPONENT) {
                return isNegative() ? Double.MIN_VALUE : Double.MAX_VALUE;
            }

            longBits = (firstBits << (leadingZeroes - POSSIBLE_LEADING_ZEROES))
                    & InfiniMathUtil.DOUBLE_FRACTION_MASK;

            if (leadingZeroes > POSSIBLE_LEADING_ZEROES) {
                if (intBitsLength > 1) {
                    longBits |= intBits[1] >> POSSIBLE_LEADING_ZEROES + Long.SIZE - leadingZeroes;
                } else if (!isInteger()) {
                    longBits |= floatBits[0] >> POSSIBLE_LEADING_ZEROES + Long.SIZE - leadingZeroes;
                }
            }

            longBits |= (exponent - InfiniMathUtil.DOUBLE_EXPONENT_OFFSET)
                    << InfiniMathUtil.DOUBLE_FRACTION_SIZE;
        } else {
            int index = 0;

            while (floatBits[index++] == 0);

            long firstFloatBits = floatBits[index];
            int leadingZeroes = Long.numberOfLeadingZeros(firstFloatBits);
            long exponent = -((long) index * Long.SIZE + leadingZeroes);

            if (exponent < InfiniMathUtil.DOUBLE_MIN_EXPONENT) {
                if (exponent < InfiniMathUtil.DOUBLE_MIN_EXPONENT - InfiniMathUtil.DOUBLE_FRACTION_SIZE) {
                    return 0;
                }

                leadingZeroes += InfiniMathUtil.DOUBLE_EXPONENT_OFFSET - exponent;
                exponent = InfiniMathUtil.DOUBLE_EXPONENT_OFFSET;
            }

            longBits = (firstFloatBits << (leadingZeroes - POSSIBLE_LEADING_ZEROES))
                    & InfiniMathUtil.DOUBLE_FRACTION_MASK;

            if (leadingZeroes > POSSIBLE_LEADING_ZEROES && floatBitsLength > ++index) {
                longBits |= floatBits[index] >>> POSSIBLE_LEADING_ZEROES + Long.SIZE - leadingZeroes;
            }

            longBits |= (exponent - InfiniMathUtil.DOUBLE_EXPONENT_OFFSET)
                    << InfiniMathUtil.DOUBLE_FRACTION_SIZE;
        }

        if (isNegative()) {
            longBits |= InfiniMathUtil.DOUBLE_SIGN_MASK;
        }

        return Double.longBitsToDouble(longBits);
    }

    @Override
    public int hashCode() {
        if (initHashCode) {
            hashCode = 31 * Arrays.hashCode(intBits) + Arrays.hashCode(floatBits);
            initHashCode = false;
        }

        return hashCode;
    }

    @Override
    public @NotNull InfiniNumber add(@NotNull Number other) {
        if (other instanceof Infinity) {
            return (InfiniNumber) other;
        }

        if (other instanceof ComplexNumber) {
            return new ComplexNumber(this).add(other);
        }

        if (!(other instanceof InfiniFloat infiniFloat)) {
            return add(InfiniMathUtil.cast(other));
        }

        return add(infiniFloat);
    }

    @Override
    public @NotNull InfiniNumber mul(@NotNull Number other) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(other);

        if (!(other instanceof InfiniFloat infiniFloat)) {
            return infiniNumber.mul(this);
        }

        return mul(infiniFloat);
    }

    @Override
    public @NotNull InfiniNumber div(@NotNull Number divider) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(divider);

        if (infiniNumber instanceof Infinity) {
            return infiniNumber != InfiniMathUtil.NaN ? ZERO : InfiniMathUtil.NaN;
        }

        if (!(infiniNumber instanceof InfiniFloat n)) {
            return new ComplexNumber(this).div(infiniNumber);
        }

        if (n.compareTo(ONE) == 0) {
            return this;
        }

        if (ZERO_TO_TWO.contains(n)) {
            final InfiniFloat div = (InfiniFloat) n.substract(ONE);

            return InfiniMathUtil.sum(0, InfiniMathUtil.TAYLORS_SEQUENCE_PRECISION,
                    (int i) -> (i & 1) == 0 ? div.pow(i) : div.pow(i).neg()).mul(this);
        }

        return divAndRemainder(n, DIV_PRECISION).first();
    }

    @Override
    public @NotNull InfiniNumber pow(@NotNull Number exp) {
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

        if (!(exp instanceof InfiniNumber n)) {
            return pow(InfiniMathUtil.cast(exp));
        }

        if (isZero()) {
            return n.compareTo(ZERO) == 0 ? InfiniMathUtil.NaN : this;
        }

        if (n instanceof InfiniFloat other && other.isInteger()) {
            if (other.isZero()) {
                return ONE;
            }

            if (other.compareTo(ONE) == 0) {
                return this;
            }

            if (other.isNegative()) {
                return ONE.div(pow(other.neg()));
            }

            if (other.compareTo(valueOf(Long.MAX_VALUE)) <= 0) {
                return ListUtil.repeat(this, other.longValue()).parallelStream()
                        .collect(InfiniMathUtil.INFINIFLOAT_PRODUCT);
            }
        }

        return InfiniMathUtil.exp(log().mul(exp));
    }

    @Override
    public @NotNull InfiniNumber pow(@NotNull Number exp, @NotNull Number mod) {
        if (!(exp instanceof InfiniFloat other)) {
            if (exp instanceof Infinity) {
                if (exp == InfiniMathUtil.NaN) {
                    return InfiniMathUtil.NaN;
                }

                return pow(exp).mod(mod);
            }

            if (mod instanceof Infinity) {
                if (mod == InfiniMathUtil.NaN) {
                    return InfiniMathUtil.NaN;
                }

                return ZERO;
            }

            if (exp instanceof ComplexNumber) {
                return new ComplexNumber(this).pow(exp);
            }

            return pow(InfiniMathUtil.cast(exp), mod);
        }

        if (other.isInteger()) {
            if (other.isZero()) {
                return ONE.mod(mod);
            }

            if (other.compareTo(ONE) == 0) {
                return mod(mod);
            }

            if (ONE.compareTo(mod) == 0) {
                InfiniFloat result = new InfiniFloat(isNegative(), ZERO_ARRAY, floatBits);

                return result.isNegative() ? ONE.add(result) : result;
            }

            if (mod instanceof InfiniFloat infiniFloat) {
                InfiniNumber var = infiniFloat.substract(ONE);

                if (var.mul(var).compareTo(this) > 0) {
                    return pow(exp).mod(mod); // using right-to-left method would incorrectly result in 0
                }
            }

            InfiniNumber result = ONE;
            InfiniNumber base = mod(mod);

            while (other.compareTo(ZERO) > 0) {
                if ((other.intBits[other.intBits.length - 1] & 1) == 1) {
                    result = result.mul(base).mod(mod);
                }

                other = other.rShift(1).floor();
                base = base.mul(base).mod(mod);
            }

            return result;
        }

        return pow(exp).mod(mod);
    }

    @Override
    public @NotNull InfiniNumber log() {
        if (isNegative()) {
            return new ComplexNumber((InfiniFloat) neg().log(), InfiniMathUtil.PI);
        }

        final InfiniFloat base = (InfiniFloat) substract(ONE);

        return InfiniMathUtil.sum(1, InfiniMathUtil.TAYLORS_SEQUENCE_PRECISION, (int i) -> {
            InfiniNumber result = InfiniMathUtil.factorial(i).div(base.pow(-i));

            if ((i & 1) == 0) {
                result = result.neg();
            }

            return result;
        });
    }

    @Override
    public @NotNull InfiniNumber floorDiv(@NotNull Number other) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(other);

        if (infiniNumber instanceof Infinity) {
            if (infiniNumber == InfiniMathUtil.NaN) {
                return InfiniMathUtil.NaN;
            }

            return ZERO;
        }

        return divMod(infiniNumber).first();
    }

    @Override
    public @NotNull InfiniNumber mod(@NotNull Number other) {
        if (other instanceof Infinity) {
            if (other == InfiniMathUtil.NaN) {
                return InfiniMathUtil.NaN;
            }

            return this;
        }

        if (other instanceof ComplexNumber) {
            return new ComplexNumber(this).mod(other);
        }

        if (!(other instanceof InfiniFloat infiniFloat)) {
            return mod(InfiniMathUtil.cast(other));
        }

        infiniFloat = infiniFloat.abs();
        int cmp = abs().compareTo(infiniFloat);

        // check if the this and modulo equal and thus the result has to be 0
        if (cmp == 0) {
            return ZERO;
        }

        // check if modulo is this objects (possibly negative and if so convert to positive)
        if (cmp < 0) {
            return isNegative() ? infiniFloat.add(this) : this;
        }

        if (infiniFloat.isZero()) {
            throw new ArithmeticException("modulo by 0");
        }

        if (infiniFloat.isInteger() && infiniFloat.isPowerOf2()) {
            return and(infiniFloat.substract(ONE).neg());
        }

        return divMod(other).right();
    }

    @Override
    public @NotNull Pair<? extends InfiniNumber, ? extends InfiniNumber> divMod(@NotNull Number other) {
        if (other instanceof Infinity) {
            return new ObjectObjectImmutablePair<>(floorDiv(other), InfiniMathUtil.NaN);
        }

        if (other instanceof ComplexNumber) {
            return new ComplexNumber(this).divMod(other);
        }

        if (!(other instanceof InfiniFloat infiniFloat)) {
            return divMod(InfiniMathUtil.cast(other));
        }

        ObjectObjectImmutablePair<InfiniFloat, InfiniFloat> pair = divAndRemainder(infiniFloat, infiniFloat);
        InfiniFloat intPart = pair.first().floor();

        return new ObjectObjectImmutablePair<>(intPart, pair.second().add(pair.first().substract(intPart)));
    }

    @Override
    public @NotNull InfiniFloat lShift(long by) {
        if (by == 0 || isZero()) {
            return this;
        }

        if (InfiniMathUtil.isNegative(by)) {
            long newSize = (((long) floatBits.length + 1) * Long.SIZE
                    - Long.numberOfTrailingZeros(floatBits[floatBits.length - 1]) - by - 1) / Long.SIZE;

            if (newSize > it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new OutOfMemoryError("InfiniFloat float bits size out of range");
            }

            long bitROffset = by & (Long.SIZE - 1); // a programming expression for modulo by a power of 2
            long bitLOffset = Long.SIZE - bitROffset;
            long[] resultIntBits = ZERO_ARRAY;
            long[] resultFloatBits = LongArrays.EMPTY_ARRAY;
            int i = 0;
            int index = 0;
            int length = (int) (((long) intBits.length * Long.SIZE - Long.numberOfLeadingZeros(intBits[0]) + by
                    + Long.SIZE - 1) / Long.SIZE);

            if (bitROffset == 0) {
                if (newSize != 0) {
                    resultFloatBits = ArrayUtil.copyOfTrimmedFromEnd(floatBits, (int) newSize);
                }

                if (length != 0) {
                    resultIntBits = Arrays.copyOf(intBits, length);

                    System.arraycopy(intBits, length, resultFloatBits, 0, intBits.length - length);
                }

                return new InfiniFloat(isNegative(), resultIntBits, resultFloatBits);
            }

            if (length != 0) {
                resultIntBits = new long[length--];

                if (intBits[0] >> bitROffset == 0) {
                    resultIntBits[i++] = intBits[0] << bitLOffset;
                }

                while (i < length) {
                    long var = intBits[index++];
                    resultIntBits[i++] |= var >> bitROffset;
                    resultIntBits[i] = var << bitLOffset;
                }

                if (index < intBits.length) {
                    resultIntBits[i] |= intBits[index] >> bitROffset;
                } else {
                    resultIntBits[i] |= floatBits[0] >> bitROffset;
                }
            }

            if (newSize != 0) {
                resultFloatBits = new long[(int) newSize];
                i = 0;
                length = intBits.length;

                while (index < length) {
                    long var = intBits[index++];
                    resultFloatBits[i++] |= var >> bitROffset;
                    resultFloatBits[i] = var << bitLOffset;
                }

                length = floatBits.length;
                index = 0;

                while (index < length) {
                    long var = floatBits[index++];
                    resultFloatBits[i++] |= var >> bitROffset;
                    resultFloatBits[i] = var << bitLOffset;
                }
            }

            return new InfiniFloat(isNegative(), resultIntBits, resultFloatBits);
        }

        int leadingZeroes = Long.numberOfLeadingZeros(intBits[0]);
        long newSize = (((long) intBits.length + 1) * Long.SIZE - leadingZeroes + by - 1)/ Long.SIZE;

        if (newSize > it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
            throw new OutOfMemoryError("InfiniFloat ints bits size out of range");
        }

        int length = intBits.length;
        long bitLOffset = by & (Long.SIZE - 1); // a programming expression for modulo by a power of 2
        long bitROffset = Long.SIZE - bitLOffset;
        long[] resultIntBits = ZERO_ARRAY;
        long[] resultFloatBits = LongArrays.EMPTY_ARRAY;
        int i = 0;

        if (bitLOffset == 0) {
            if (newSize != 0) {
                resultIntBits = Arrays.copyOf(intBits, (int) newSize);
            }

            resultFloatBits = ArrayUtil.copyOfTrimmedFromEnd(floatBits, floatBits.length - ((int) newSize)
                    + intBits.length);

            System.arraycopy(floatBits, 0, resultIntBits, length, floatBits.length);

            return new InfiniFloat(isNegative(), resultIntBits, resultFloatBits);
        }

        if (newSize != 0) {
            resultIntBits = new long[(int) newSize];

            if (intBits[0] >> bitROffset != 0) {
                int maxIndex = length - 1;

                while (i < maxIndex) {
                    long var = intBits[i];
                    resultIntBits[i] |= var >> bitROffset;
                    resultIntBits[++i] = var << bitLOffset;
                }
            } else {
                resultIntBits[i++] = intBits[0] << bitLOffset;

                while (i < length) {
                    long var = intBits[i];
                    resultIntBits[i - 1] |= var >> bitROffset;
                    resultIntBits[i++] = var << bitLOffset;
                }
            }
        }

        length = floatBits.length;

        if (length != 0) {
            int maxIndex = resultIntBits.length - 1;
            int fi = 0;

            while (i < maxIndex && fi < length) {
                long var = floatBits[fi++];
                resultIntBits[i] |= var >> bitROffset;
                resultIntBits[++i] = var << bitLOffset;
            }

            if (fi < maxIndex) {
                resultIntBits[resultIntBits.length - 1] |= floatBits[fi] >> bitROffset;
                length = floatBits.length - fi;
                resultFloatBits = new long[length--];
                resultFloatBits[0] = floatBits[fi++] << bitLOffset;
                i = 0;

                while (i < length) {
                    long var = floatBits[fi++];
                    resultIntBits[i] |= var >> bitROffset;
                    resultIntBits[++i] = var << bitLOffset;
                }

                if (resultFloatBits[length] == 0) {
                    resultFloatBits = Arrays.copyOf(resultFloatBits, length);
                }
            }
        }

        return new InfiniFloat(isNegative(), resultIntBits, resultFloatBits);
    }

    @Override
    public @NotNull InfiniFloat rShift(long by) {
        return lShift(-by);
    }

    @Override
    public @NotNull InfiniNumber and(@NotNull Number other) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(other);

        if (!(infiniNumber instanceof InfiniFloat n)) {
            return infiniNumber.and(this);
        }

        long[] shorterIntBits = intBits;
        long[] longerIntBits = n.intBits;
        long[] shorterFloatBits = floatBits;
        long[] longerFloatBits = n.floatBits;

        if (shorterIntBits.length > longerIntBits.length) {
            shorterIntBits = longerIntBits;
            longerIntBits = intBits;
        }

        if (shorterFloatBits.length > longerFloatBits.length) {
            shorterFloatBits = longerFloatBits;
            longerFloatBits = floatBits;
        }

        long[] resultIntBits = new long[shorterIntBits.length];
        long[] resultFloatBits = shorterFloatBits.length != 0 ? new long[shorterFloatBits.length]
                : shorterFloatBits;
        int i = shorterIntBits.length;
        int j = longerIntBits.length;
        int length;

        while (i-- != 0) {
            j--;
            resultIntBits[i] = shorterIntBits[i] & longerIntBits[j];
        }

        for (length = resultFloatBits.length; i != length; i++) {
            resultFloatBits[i] = shorterFloatBits[i] & longerFloatBits[i];
        }

        return new InfiniFloat(isNegative() && n.isNegative(), trimIntBits(resultIntBits),
                trimFloatbits(resultFloatBits));
    }

    @Override
    public @NotNull InfiniNumber or(@NotNull Number other) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(other);

        if (!(infiniNumber instanceof InfiniFloat n)) {
            return infiniNumber.or(this);
        }

        long[] shorterIntBits = intBits;
        long[] longerIntBits = n.intBits;
        long[] shorterFloatBits = floatBits;
        long[] longerFloatBits = n.floatBits;

        if (shorterIntBits.length > longerIntBits.length) {
            shorterIntBits = longerIntBits;
            longerIntBits = intBits;
        }

        if (shorterFloatBits.length > longerFloatBits.length) {
            shorterFloatBits = longerFloatBits;
            longerFloatBits = floatBits;
        }

        long[] resultIntBits = new long[longerIntBits.length];
        long[] resultFloatBits = longerFloatBits.length != 0 ? new long[longerFloatBits.length]
                : longerFloatBits;
        int i = shorterIntBits.length;
        int j = longerIntBits.length;
        int length;

        while (i-- != 0) {
            j--;
            resultIntBits[i] = shorterIntBits[i] | longerIntBits[j];
        }

        for (length = shorterFloatBits.length; i != length; i++) {
            resultFloatBits[i] = shorterFloatBits[i] | longerFloatBits[i];
        }

        System.arraycopy(longerIntBits, 0, resultIntBits, 0, j);
        System.arraycopy(longerFloatBits, i, resultFloatBits, i, resultFloatBits.length - i);

        return new InfiniFloat(isNegative() || n.isNegative(), resultIntBits, resultFloatBits);
    }

    @Override
    public @NotNull InfiniNumber xor(@NotNull Number other) {
        InfiniNumber infiniNumber = InfiniMathUtil.cast(other);

        if (!(infiniNumber instanceof InfiniFloat n)) {
            return infiniNumber.or(this);
        }

        long[] shorterIntBits = intBits;
        long[] longerIntBits = n.intBits;
        long[] shorterFloatBits = floatBits;
        long[] longerFloatBits = n.floatBits;

        if (shorterIntBits.length > longerIntBits.length) {
            shorterIntBits = longerIntBits;
            longerIntBits = intBits;
        }

        if (shorterFloatBits.length > longerFloatBits.length) {
            shorterFloatBits = longerFloatBits;
            longerFloatBits = floatBits;
        }

        long[] resultIntBits = new long[longerIntBits.length];
        long[] resultFloatBits = longerFloatBits.length != 0 ? new long[longerFloatBits.length]
                : longerFloatBits;
        int i = shorterIntBits.length;
        int j = longerIntBits.length;
        int length;

        while (i-- != 0) {
            j--;
            resultIntBits[i] = shorterIntBits[i] ^ longerIntBits[j];
        }

        for (length = shorterFloatBits.length; i != length; i++) {
            resultFloatBits[i] = shorterFloatBits[i] ^ longerFloatBits[i];
        }

        System.arraycopy(longerIntBits, 0, resultIntBits, 0, j);
        System.arraycopy(longerFloatBits, i, resultFloatBits, i, resultFloatBits.length - i);

        return new InfiniFloat(isNegative() ^ n.isNegative(), resultIntBits, resultFloatBits);
    }

    @Override
    public @NotNull InfiniFloat invert() {
        long[] resultIntBits = new long[intBits.length];
        long[] resultFloatBits = new long[floatBits.length];
        int length = resultIntBits.length;

        for (int i = 0; i != length; i++) {
            resultIntBits[i] = ~intBits[i];
        }

        length = resultFloatBits.length;

        for (int i = 0; i != length; i++) {
            resultFloatBits[i] = ~floatBits[i];
        }

        return new InfiniFloat(!isNegative(), trimIntBits(resultIntBits), trimFloatbits(resultFloatBits));
    }

    @Override
    public @NotNull InfiniFloat sign() {
        if (isZero()) {
            return ZERO;
        }

        return isNegative() ? NEGATIVE_ONE : ONE;
    }

    @Override
    public @NotNull InfiniFloat abs() {
        return isNegative() ? new InfiniFloat(false, intBits, floatBits) : this;
    }

    @Override
    public @NotNull InfiniFloat neg() {
        return new InfiniFloat(!isNegative(), intBits, floatBits);
    }

    @Override
    public @NotNull InfiniFloat floor() {
        return new InfiniFloat(isNegative(), intBits);
    }

    @Override
    public @NotNull InfiniFloat ceil() {
        return !isInteger() ? isNegative() ?  (InfiniFloat) floor().substract(ONE) : (InfiniFloat) floor().add(ONE)
                : this;
    }

    @Override
    public @NotNull InfiniFloat round() {
        if (!isInteger() && (floatBits[0] & 0x8000000000000000L) != 0) {
            if (isNegative()) {
                return (InfiniFloat) floor().substract(ONE);
            } else {
                return (InfiniFloat) floor().add(ONE);
            }
        } else {
            return floor();
        }
    }

    @Override
    public @NotNull InfiniFloat round(long decimalDigits) {
        InfiniNumber exponent = TEN.pow(valueOf(decimalDigits));
        return (InfiniFloat) mul(exponent).round().div(exponent);
    }

    @Override
    public long size() {
        return isInteger() ? (long) intBits.length * Long.SIZE - Long.numberOfLeadingZeros(intBits[0])
                : ((long) intBits.length) * Long.SIZE - Long.numberOfLeadingZeros(intBits[0])
                - Long.numberOfTrailingZeros(floatBits[floatBits.length - 1]);
    }

    public InfiniFloat add(InfiniFloat n) {
        if (n.isZero()) {
            return this;
        }

        if (isZero()) {
            return n;
        }

        if (n.isNegative()) {
            if (isNegative()) {
                return neg().add(n.neg()).neg();
            }

            long[] resultIntBits;
            long[] resultFloatBits;
            boolean resultSign = false;

            boolean bit64 = false;

            int thisBitsIndex = floatBits.length;
            int otherBitsIndex = n.floatBits.length;

            if (thisBitsIndex > otherBitsIndex) {
                resultFloatBits = new long[thisBitsIndex];

                System.arraycopy(floatBits, otherBitsIndex, resultFloatBits, otherBitsIndex,
                        thisBitsIndex - otherBitsIndex);
            } else {
                resultFloatBits = new long[otherBitsIndex];

                if (otherBitsIndex != thisBitsIndex) {
                    while (otherBitsIndex-- > thisBitsIndex) {
                        long second = n.floatBits[otherBitsIndex];

                        if (second == 0) {
                            if (bit64) {
                                resultFloatBits[otherBitsIndex] = -1;
                            }
                        } else {
                            if (bit64) {
                                resultFloatBits[otherBitsIndex] = -second - 1;
                            } else {
                                resultFloatBits[otherBitsIndex] = -second;
                                bit64 = true;
                            }
                        }
                    }
                }
            }

            while (otherBitsIndex-- > 0) {
                long first = floatBits[otherBitsIndex];
                long second = n.floatBits[otherBitsIndex];

                if (bit64) {
                    resultFloatBits[thisBitsIndex] = first - second - 1;
                } else {
                    resultFloatBits[thisBitsIndex] = first - second;
                }

                bit64 = Long.compareUnsigned(first, second) < 0;
            }

            thisBitsIndex = intBits.length;
            otherBitsIndex = n.intBits.length;

            if (thisBitsIndex > otherBitsIndex) {
                resultIntBits = new long[thisBitsIndex];

                while (otherBitsIndex-- > 0) {
                    thisBitsIndex--;

                    long first = intBits[thisBitsIndex];
                    long second = n.intBits[otherBitsIndex];

                    if (bit64) {
                        resultIntBits[thisBitsIndex] = first - second - 1;
                    } else {
                        resultIntBits[thisBitsIndex] = first - second;
                    }

                    bit64 = Long.compareUnsigned(first, second) < 0;
                }

                while (bit64) {
                    long first = intBits[thisBitsIndex];

                    resultIntBits[thisBitsIndex--] = first - 1;
                    bit64 = first == 0;
                }

                System.arraycopy(intBits, 0, resultIntBits, 0, thisBitsIndex);
            } else {
                resultIntBits = new long[otherBitsIndex];

                while (thisBitsIndex-- > 0) {
                    otherBitsIndex--;

                    long first = intBits[thisBitsIndex];
                    long second = n.intBits[otherBitsIndex];

                    if (bit64) {
                        resultIntBits[thisBitsIndex] = first - second - 1;
                    } else {
                        resultIntBits[thisBitsIndex] = first - second;
                    }

                    bit64 = Long.compareUnsigned(first, second) < 0;
                }

                while (otherBitsIndex-- > 0) {
                    long temp = n.intBits[otherBitsIndex];

                    if (bit64) {
                        resultIntBits[otherBitsIndex] = -temp - 1;
                        bit64 = false;
                    } else {
                        resultIntBits[otherBitsIndex] = -temp;
                    }
                }

                resultSign = bit64 || n.intBits.length > intBits.length;
            }

            return new InfiniFloat(resultSign, resultIntBits, resultFloatBits);
        }

        if (isNegative()) {
            return n.add(this);
        }

        long[] shorterIntBits = intBits;
        long[] longerIntBits = n.intBits;
        long[] shorterFloatBits = floatBits;
        long[] longerFloatBits = n.floatBits;

        if (longerIntBits.length < shorterIntBits.length) {
            shorterIntBits = longerIntBits;
            longerIntBits = floatBits;
        }

        if (longerFloatBits.length < shorterFloatBits.length) {
            shorterFloatBits = longerFloatBits;
            longerFloatBits = floatBits;
        }

        long[] resultIntBits;
        long[] resultFloatBits;

        long temp;
        boolean bit64 = false;

        int longerBitsIndex = longerFloatBits.length;
        int shorterBitsIndex = shorterFloatBits.length;

        if (shorterBitsIndex != 0) {
            resultFloatBits = new long[longerBitsIndex];

            System.arraycopy(longerFloatBits, shorterBitsIndex, resultFloatBits, shorterBitsIndex,
                    longerBitsIndex - shorterBitsIndex);

            while (shorterBitsIndex-- > 0) {
                long first = longerFloatBits[shorterBitsIndex];
                long second = shorterFloatBits[shorterBitsIndex];

                if (bit64) {
                    resultFloatBits[shorterBitsIndex] = temp = first + second + 1;
                } else {
                    resultFloatBits[shorterBitsIndex] = temp = first + second;
                }

                bit64 = InfiniMathUtil.isNegative(first) && InfiniMathUtil.isNegative(second)
                        && !InfiniMathUtil.isNegative(temp);
            }
        } else {
            resultFloatBits = longerFloatBits;
        }

        longerBitsIndex = longerIntBits.length;
        shorterBitsIndex = shorterIntBits.length;

        if (shorterBitsIndex != 1 || shorterIntBits[0] != 0) {
            resultIntBits = new long[longerBitsIndex];

            while (longerBitsIndex-- > 0 && shorterBitsIndex-- > 0) {
                long first = longerIntBits[longerBitsIndex];
                long second = shorterIntBits[shorterBitsIndex];

                if (bit64) {
                    resultIntBits[longerBitsIndex] = temp = first + second + 1;
                } else {
                    resultIntBits[longerBitsIndex] = temp = first + second;
                }

                bit64 = InfiniMathUtil.isNegative(first) && InfiniMathUtil.isNegative(second)
                        && !InfiniMathUtil.isNegative(temp);
            }

            while (bit64) {
                if (longerBitsIndex < 0) {
                    if (resultIntBits.length + 1 > it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError("InfiniFloat size out of range");
                    }

                    resultIntBits = ArrayUtil.concat(new long[]{1}, resultIntBits);
                    break;
                }

                temp = longerIntBits[longerBitsIndex];
                resultIntBits[longerBitsIndex] = temp + 1;
                bit64 = temp == Long.MAX_VALUE;
                longerBitsIndex--;
            }

            System.arraycopy(longerIntBits, 0, resultIntBits, 0, longerBitsIndex);
        } else {
            resultIntBits = longerIntBits;
        }

        return new InfiniFloat(false, resultIntBits, resultFloatBits);
    }

    public InfiniFloat mul(InfiniFloat n) {
        final InfiniFloat mult;
        final InfiniFloat multiplier;

        if (intBits.length + floatBits.length < n.intBits.length + n.floatBits.length) {
            multiplier = abs();
            mult = n.abs();
        } else {
            multiplier = n.abs();
            mult = abs();
        }

        final int intLength = multiplier.intBits.length;
        final int floatLength = multiplier.floatBits.length;

        return LongStream.concat(StreamUtil.flatMapToLong(
                IntStream.range(0, intLength).parallel(), i -> {
            long setBits = multiplier.intBits[i];

            if (setBits == 0) {
                return null;
            }

            LongStream.Builder builder = LongStream.builder();
            int offset = (intLength - i) * Long.SIZE;
            int setBit;

            while ((setBit = Long.numberOfTrailingZeros(setBits)) != 64) {
                builder.add(offset + setBit);
                setBits &= ~(1L << setBit);
            }

            return builder.build();
        }), StreamUtil.flatMapToLong(IntStream.range(0, floatLength).parallel(), i -> {
            long setBits = multiplier.floatBits[i];

            if (setBits == 0) {
                return null;
            }

            LongStream.Builder builder = LongStream.builder();
            int offset = i * Long.SIZE;
            int setBit;

            while ((setBit = Long.numberOfLeadingZeros(setBits)) != 64) {
                builder.add(-offset - setBit);
                setBits &= ~(0x8000000000000000L >> setBit);
            }

            return builder.build();
        })).mapToObj(mult::lShift).collect(CollectingUtil.unorderedCumulation(InfiniFloat::add,
                (i) -> i == null ? ZERO : (isNegative() == n.isNegative() ? i : i.neg())));
    }

    public boolean isNegative() {
        return sign;
    }

    public boolean isZero() {
        return isInteger() && intBits[0] == 0;
    }

    public boolean isInteger() {
        return floatBits.length == 0;
    }

    public boolean isPowerOf2() {
        if (isPowOf2 == 0) {
            isPowOf2 = initIsPowerOf2() ? (byte) 1 : (byte) 2;
        }

        return isPowOf2 == 1;
    }

    private boolean initIsPowerOf2() {
        return intBits[0] == 0 ? !isInteger() && Long.bitCount(floatBits[floatBits.length - 1]) == 1
                && Arrays.stream(floatBits, 0, floatBits.length - 1).allMatch(i -> i == 0)
                : isInteger() && Long.bitCount(intBits[0]) == 1 && Arrays.stream(intBits, 1, intBits.length)
                .allMatch(i -> i == 0);
    }

    private ObjectObjectImmutablePair<InfiniFloat, InfiniFloat> divAndRemainder(@NotNull InfiniFloat divider,
                                                                               @NotNull InfiniFloat precision) {

        InfiniFloat result = ZERO;
        InfiniFloat rest = this;
        long pow2 = roundDownLog2Abs();

        while (rest.abs().compareTo(precision) > 0) {
            InfiniFloat part = rest.rShift(pow2);
            result = result.add(part);
            rest = rest.add(mul(part).neg());
        }

        return new ObjectObjectImmutablePair<>(result, rest);
    }

    /**
     * Find the first set bit in this InfiniFloat and returns its index.
     * That makes it quiete time efficient and also used by other methods.
     *
     * @return the logarithm with base 2 of the absolute value of this InfiniFloat
     * rounded down
     */
    public long roundDownLog2Abs() {
        if (intBits[0] != 0) {
            return (long) intBits.length * Long.SIZE - Long.numberOfLeadingZeros(intBits[0]) - 1;
        }

        for (int i = 0; i != floatBits.length; i++) {
            if (floatBits[i] != 0) {
                return (long) -i * Long.SIZE + Long.numberOfLeadingZeros(floatBits[i]);
            }
        }

        return Integer.MIN_VALUE; // this is zero
    }

    /**
     * analogy to roundDownLog2Abs
     *
     * @return
     */
    public long leastImportantPrecisionBitIndex() {
        if (floatBits.length != 0) {
            return (long) -intBits.length * Long.SIZE - Long.numberOfTrailingZeros(floatBits[floatBits.length - 1]);
        }

        int i = intBits.length;

        while (i-- != 0) {
            if (intBits[i] != 0) {
                return (long) i * Long.SIZE + Long.numberOfTrailingZeros(floatBits[i]) - 1;
            }
        }

        return Integer.MAX_VALUE; // this is a zero
    }

    private static long[] trimIntBits(long[] bits) {
        int i = 0;
        int length = bits.length;

        while (i != length && bits[i] == 0) {
            i++;
        }

        if (i == length) {
            return ZERO_ARRAY;
        } else if (i != 0) {
            return ArrayUtil.copyOfTrimmedFromEnd(bits, length - i);
        } else {
            return bits;
        }
    }

    private static long[] trimFloatbits(long[] bits) {
        int length = bits.length;
        int i = length;

        while (i != 0 && bits[--i] == 0);

        if (i == 0) {
            return LongArrays.EMPTY_ARRAY;
        } else if (++i != length) {
            return Arrays.copyOf(bits, i);
        } else {
            return bits;
        }
    }

    public static InfiniFloat valueOf(boolean sign, long unsignedLong) {
        return unsignedLong < 0 ? new InfiniFloat(sign, new long[]{unsignedLong})
                : valueOf(sign ? -unsignedLong : unsignedLong);
    }

    public static InfiniFloat valueOf(long i) {
        return LONG_VALUE_CACHE.computeIfAbsent(i, (j) -> {
            if (InfiniMathUtil.isNegative(j)) {
                return valueOf(-j).neg();
            } else {
                return new InfiniFloat(false, new long[]{j});
            }
        });
    }

    /**
     * Returns a InfiniNumber representation of the given doubles.
     * It is guaranteed that the result is equal to argument unless it NaN.
     *
     * <p>
     * Special cases:
     * <ul><li>If the argument is NaN, the result is InfiniMathUtil.NaN.
     * <li>If the argument is negative infinity, the result is InfiniMathUtil.NEGATIVE_INFINITY
     * <li>If the argument is positive infinity, the result is InfiniMathUtil.POSITIVE_INFINITY</ul>
     *
     * @param d the number as a doubles
     * @return the number as a InfiniNumber
     */
    public static InfiniNumber valueOf(double d) {
        return DOUBLE_VALUE_CACHE.computeIfAbsent(d, (e) -> {
            long bits = Double.doubleToRawLongBits(e);
            long fraction = bits & InfiniMathUtil.DOUBLE_FRACTION_MASK;
            boolean sign = (bits & InfiniMathUtil.DOUBLE_SIGN_MASK) != 0;
            int exponent = (int) ((bits & InfiniMathUtil.DOUBLE_EXPONENT_MASK)
                    >>> InfiniMathUtil.DOUBLE_FRACTION_SIZE);
            InfiniFloat value;

            if (exponent == 0) {
                value = valueOf(sign, fraction).lShift(-1022);
            } else if (exponent == InfiniMathUtil.RAW_DOUBLE_MAX_EXPONENT) {
                if (fraction == 0) {
                    return sign ? InfiniMathUtil.NEGATIVE_INFINITY : InfiniMathUtil.POSITIVE_INFINITY;
                } else {
                    return InfiniMathUtil.NaN;
                }
            } else {
                value = valueOf(sign, fraction | (1L << InfiniMathUtil.DOUBLE_FRACTION_SIZE))
                        .lShift(exponent + InfiniMathUtil.DOUBLE_EXPONENT_OFFSET);
            }

            value.doubleValue = e;
            value.isPowOf2 = (byte) (fraction == 0 ? 1 : 2);

            return value;
        });
    }

    /**
     * Creates a new instance of {@code InfiniFloat} array specifying the instance.
     * The result with have 0 float bits (will be an integer).
     * For access safety copies of the actual inputted array may be used in result's
     * fields.
     *
     * @param sign true if the result is supposed to be negative
     * @param intBits the array of longs signifying the integer part of the InfiniFloat
     * @return a new instance of {@code InfiniFloat} array specifying the instance
     */
    public static InfiniFloat ofBits(boolean sign, long @NotNull [] intBits) {
        return ofBits(sign, intBits, LongArrays.EMPTY_ARRAY);
    }

    /**
     * Creates a new instance of {@code InfiniFloat} arrays specifying the instance.
     * For access safety copies of the actual inputted arrays may be used in result's
     * fields.
     *
     * @param sign true if the result is supposed to be negative
     * @param intBits the array of longs signifying the integer part of the InfiniFloat
     * @param floatBits the array of longs signifying the decimal part of the InfiniFloat
     * @return a new instance of {@code InfiniFloat} arrays specifying the instance
     */
    public static InfiniFloat ofBits(boolean sign, long @NotNull [] intBits, long @NotNull [] floatBits) {
        return new InfiniFloat(sign, intBits.length != 0 ? trimIntBits(intBits.clone()) : ZERO_ARRAY,
                floatBits.length != 0 ? trimFloatbits(floatBits.clone()) : LongArrays.EMPTY_ARRAY);
    }

    static {
        DOUBLE_VALUE_CACHE.put(0.0D, ZERO);
        DOUBLE_VALUE_CACHE.put(-0.0D, NEGATIVE_ZERO);
    }
}
