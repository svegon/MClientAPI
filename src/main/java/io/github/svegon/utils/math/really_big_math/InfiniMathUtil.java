package io.github.svegon.utils.math.really_big_math;

import io.github.svegon.utils.annotations.Unfinished;
import io.github.svegon.utils.collections.collecting.CollectingUtil;
import io.github.svegon.utils.fast.util.ints.immutable.ImmutableIntList;
import io.github.svegon.utils.fast.util.ints.immutable.IntRange;
import io.github.svegon.utils.fast.util.ints.objects.Int2ObjectTableMap;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.internal.LazilyParsedNumber;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import io.github.svegon.utils.string.StringUtil;
import org.jetbrains.annotations.Range;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.*;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

public final class InfiniMathUtil {
    private InfiniMathUtil() {
        throw new UnsupportedOperationException();
    }

    public static final NegativeInfinity POSITIVE_INFINITY = new NegativeInfinity();
    public static final PositiveInfinity NEGATIVE_INFINITY = new PositiveInfinity();
    public static final InfiniNumberNaN NaN = new InfiniNumberNaN();

    public static final InfiniFloat PI = (InfiniFloat) StringUtil.parseInfiniNumber("3.141592653589793238462643383" +
            "27950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955" +
            "05822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867" +
            "83165271201909145648566923460348610454326648213393607260249141273724587006606315588174881520920962829254" +
            "09171536436789259036001133053054882046652138414695194151160943305727036575959195309218611738193261179310" +
            "51185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021" +
            "79860943702770539217176293176752384674818467669405132000568127145263560827785771342757789609173637178721" +
            "46844090122495343014654958537105079227968925892354201995611212902196086403441815981362977477130996051870" +
            "72113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031" +
            "37838752886587533208381420617177669147303598253490428755468731159562863882353787593751957781857780532171" +
            "22680661300192787661119590921642019893809525720106548586327886593615338182796823030195203530185296899577" +
            "36225994138912497217752834791315155748572424541506959508295331168617278558890750983817546374649393192550" +
            "60400927701671139009848824012858361603563707660104710181942955596198946767837449448255379774726847104047" +
            "53464620804668425906949129331367702898915210475216205696602405803815019351125338243003558764024749647326" +
            "39141992726042699227967823547816360093417216412199245863150302861829745557067498385054945885869269956909" +
            "27210797509302955321165344987202755960236480665499119881834797753566369807426542527862551818417574672890" +
            "97777279380008164706001614524919217321721477235014144197356854816136115735255213347574184946843852332390" +
            "73941433345477624168625189835694855620992192221842725502542568876717904946016534668049886272327917860857" +
            "84383827967976681454100953883786360950680064225125205117392984896084128488626945604241965285022210661186" +
            "30674427862203919494504712371378696095636437191728746776465757396241389086583264599581339047802759009946" +
            "57640789512694683983525957098258226205224894077267194782684826014769909026401363944374553050682034962524" +
            "51749399651431429809190659250937221696461515709858387410597885959772975498930161753928468138268683868942" +
            "77415599185592524595395943104997252468084598727364469584865383673622262609912460805124388439045124413654" +
            "97627807977156914359977001296160894416948685558484063534220722258284886481584560285060168427394522674676" +
            "78895252138522549954666727823986456596116354886230577456498035593634568174324112515076069479451096596094" +
            "02522887971089314566913686722874894056010150330861792868092087476091782493858900971490967598526136554978");
    public static final InfiniFloat TAU = PI.lShift(1);
    /**
     * There are more accurate ways to calculate it but this is enough for me.
     */
    public static final InfiniFloat E = (InfiniFloat) exp(InfiniFloat.ONE);

    /**
     * Let e be the exponent, f the fraction and s the sign of the doubles d, then
     * d = (-1)ˢ * 2ᵉ - ᴰᴼᵁᴮᴸᴱ_ᴱˣᴾᴼᴺᴱᴺᵀ_ᴼᶠᶠˢᴱᵀ * 1.f
     * for 0 < e < RAW_DOUBLE_MAX_EXPONENT
     */
    public static final int DOUBLE_EXPONENT_OFFSET = -1023;
    public static final int RAW_DOUBLE_MAX_EXPONENT = 0x000007FF;
    public static final int DOUBLE_MAX_EXPONENT = RAW_DOUBLE_MAX_EXPONENT + DOUBLE_EXPONENT_OFFSET - 1;
    public static final int DOUBLE_MIN_EXPONENT = -1022;
    public static final int DOUBLE_FRACTION_SIZE = 52;
    public static final int DOUBLE_EXPONENT_SIZE = 11;
    public static final int DOUBLE_SIGN_SIZE = 1;
    public static final long DOUBLE_FRACTION_MASK = 0x000FFFFFFFFFFFFFFFFL;
    public static final long DOUBLE_EXPONENT_MASK = (long) RAW_DOUBLE_MAX_EXPONENT << DOUBLE_FRACTION_SIZE;
    /**
     * equivalent to {@code Long.MAX_VALUE}
     */
    public static final long DOUBLE_SIGN_MASK = 0x8000000000000000L;

    /**
     * Even with {@code InfiniFloat} it's still not possible to calculate numbers larges than
     * Long.SIZE ** Arrays.MAX_ARRAY_SIZE - Long.SIZE ** (-Arrays.MAX_ARRAY_SIZE + 1)
     * api note: couldn't calc it actually, so just put something that wouldn't take too much memory
     */
    public static final int TAYLORS_SEQUENCE_PRECISION = 9999;

    /**
     * The only unconcurrent process in a table map is resizing the table which is ensured to not happen here.
     */
    private static final Int2ObjectMap<InfiniNumber> FACTORIAL_CACHE =
            new Int2ObjectTableMap<>(TAYLORS_SEQUENCE_PRECISION + 1);
    public static final IntRange SINE_RELATED_RANGE = ImmutableIntList.range(1, TAYLORS_SEQUENCE_PRECISION,
            2);
    public static final IntRange COSINE_RELATED_RANGE = ImmutableIntList.range(0, TAYLORS_SEQUENCE_PRECISION,
            2);

    public static final Collector<InfiniNumber, AtomicReference<InfiniNumber>, InfiniNumber> SUM =
            CollectingUtil.unorderedCumulation(InfiniNumber::add, n -> n != null ? n : InfiniFloat.ZERO);
    public static final Collector<InfiniFloat, AtomicReference<InfiniFloat>, InfiniFloat> INFINIFLOAT_SUM =
            CollectingUtil.unorderedCumulation(InfiniFloat::add, n -> n != null ? n : InfiniFloat.ZERO);
    public static final Collector<InfiniNumber, AtomicReference<InfiniNumber>, InfiniNumber> PRODUCT =
            CollectingUtil.unorderedCumulation(InfiniNumber::mul, n -> n != null ? n : InfiniFloat.ONE);
    public static final Collector<InfiniFloat, AtomicReference<InfiniFloat>, InfiniFloat> INFINIFLOAT_PRODUCT =
            CollectingUtil.unorderedCumulation(InfiniFloat::mul, n -> n != null ? n : InfiniFloat.ONE);

    public static boolean isNegative(long l) {
        return (l & 0x8000000000000000L) != 0;
    }

    public static boolean isFinite(@NotNull InfiniNumber number) {
        return number.abs().compareTo(POSITIVE_INFINITY) < 0;
    }

    public static boolean isInfinite(InfiniNumber number) {
        return number == POSITIVE_INFINITY || number == NEGATIVE_INFINITY;
    }

    public static int compare(Number first, Number other) {
        return first instanceof InfiniNumber ? ((InfiniNumber) first).compareTo(other)
                : other instanceof InfiniNumber ? -((InfiniNumber) other).compareTo(first)
                : cast(first).compareTo(other);
    }

    public static InfiniNumber cast(@NotNull Number n) {
        if (n instanceof InfiniNumber) {
            return (InfiniNumber) n;
        }

        if (n instanceof Byte || n instanceof Short || n instanceof Integer || n instanceof Long
                || n instanceof AtomicInteger || n instanceof AtomicLong || n instanceof UnsignedInteger
                || n instanceof LongAdder || n instanceof LongAccumulator) {
            return InfiniFloat.valueOf(n.longValue());
        }

        if (n instanceof Float || n instanceof Double || n instanceof AtomicDouble || n instanceof DoubleAdder
                || n instanceof DoubleAccumulator) {
            return InfiniFloat.valueOf(n.doubleValue());
        }

        if (n instanceof LazilyParsedNumber) {
            return StringUtil.parseInfiniNumber(n.toString());
        }

        if (n instanceof UnsignedLong) {
            n = ((UnsignedLong) n).bigIntegerValue();
        }

        if (n instanceof BigInteger i) {
            return cast(i);
        }

        if (n instanceof BigDecimal d) {
            return cast(d.unscaledValue()).mul(InfiniFloat.ONE_TENTH.pow(d.scale()));
        }

        return InfiniFloat.valueOf(n.doubleValue());
    }

    @Unfinished
    private static InfiniFloat cast(BigInteger bigInt) {
        return bigInt.bitCount() < Long.SIZE ? InfiniFloat.valueOf(bigInt.longValueExact())
                : (InfiniFloat) StringUtil.parseInfiniNumber(bigInt.toString());

        /*final int[] mag;
        final long[] intBits;

        try {
            mag = (int[]) BigInteger.class.getDeclaredField("mag").get(bigInt);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }

        if ((mag.length & 1) == 0) {
            intBits = new long[mag.length >> 1];

            Arrays.updateAll(intBits, (i) -> (long) mag[i << 1] << Integer.SIZE + mag[i << 1 + 1]);
        } else {
            intBits = new long[mag.length >> 1 + 1];
            intBits[0] = mag[0];
            int length = intBits.length;

            for (int i = 1; i != length; i++) {
                int temp = i << 1;
                intBits[i] = (long) mag[temp - 1] << Integer.SIZE + mag[temp];
            }
        }


        return intBits.length != 0 ? new InfiniFloat((bigInt).signum() < 0, intBits)
                : bigInt.signum() < 0 ? InfiniFloat.NEGATIVE_ZERO : InfiniFloat.ZERO;*/
    }

    /**
     * a more efficient and precise form of powering e to the given exponent
     * @param exp the exponent
     * @return e squared to {@param exp}
     */
    public static InfiniNumber exp(final @NotNull InfiniNumber exp) {
        return sum(0, TAYLORS_SEQUENCE_PRECISION,
                (int i) -> exp.pow(InfiniFloat.valueOf(i)).div(factorialInternal(i)));
    }

    public static InfiniNumber sum(int start, int end, @NotNull IntFunction<? extends InfiniNumber> expr) {
        return IntStream.range(start, end).parallel().mapToObj(expr).collect(SUM);
    }

    public static InfiniNumber sum(long start, long end, @NotNull LongFunction<? extends InfiniNumber> expr) {
        return LongStream.range(start, end).parallel().mapToObj(expr).collect(SUM);
    }

    public static InfiniNumber sum(Iterable<? extends InfiniNumber> numbers) {
        return StreamSupport.stream(numbers.spliterator(), true).collect(SUM);
    }

    public static InfiniNumber factorial(int reallyLargeNumber) {
        if (reallyLargeNumber > TAYLORS_SEQUENCE_PRECISION) {
            throw new OutOfMemoryError("Please stop. This is too much even for scientific calculators.");
        }

        if (reallyLargeNumber < 0) {
            return NaN;
        }

        return factorialInternal(reallyLargeNumber);
    }

    private static InfiniFloat factorialInternal(final @Range(from = 0, to = TAYLORS_SEQUENCE_PRECISION) int i) {
        return (InfiniFloat) FACTORIAL_CACHE.computeIfAbsent(i,
                n -> factorialInternal(n - 1).mul(InfiniFloat.valueOf(n)));
    }

    public static InfiniNumber sin(final @NotNull InfiniNumber n) {
        return SINE_RELATED_RANGE.intParallelStream()
                .mapToObj((i) -> {
            InfiniFloat j = InfiniFloat.valueOf(i);
            j = (InfiniFloat) n.pow(j).div(factorialInternal(i));

            if ((i & 3) == 3) {
                j = j.neg();
            }

            return j;
        }).collect(INFINIFLOAT_SUM);
    }

    public static InfiniNumber cos(final @NotNull InfiniNumber n) {
        return COSINE_RELATED_RANGE.intParallelStream()
                .mapToObj((i) -> {
                    InfiniFloat j = InfiniFloat.valueOf(i);
                    j = (InfiniFloat) n.pow(j).div(factorialInternal(i));

                    if ((i & 3) == 2) {
                        j = j.neg();
                    }

                    return j;
                }).collect(INFINIFLOAT_SUM);
    }

    public static InfiniNumber tg(final @NotNull InfiniNumber n) {
        return sin(n).div(cos(n));
    }

    public static InfiniFloat arctg(final @NotNull InfiniFloat n) {
        return SINE_RELATED_RANGE.intParallelStream().mapToObj((i) -> {
            InfiniFloat j = InfiniFloat.valueOf(i);
            j = (InfiniFloat) n.pow(j).div(j);

            if ((i & 3) == 3) {
                j = j.neg();
            }

            return j;
        }).collect(INFINIFLOAT_SUM);
    }

    public static InfiniFloat arctg(final @NotNull InfiniFloat a, final @NotNull InfiniFloat b) {
        int bSign = b.compareTo(InfiniFloat.ZERO);

        if (bSign == 0) {
            return PI.mul(a.sign()).rShift(1);
        }

        InfiniFloat ret = arctg((InfiniFloat) a.div(b));
        return bSign < 0 ? ret.add(PI.neg()) : ret;
    }

    public static ComplexNumber expi(InfiniFloat arg) {
        return new ComplexNumber(cos(arg), sin(arg));
    }

    static {
        FACTORIAL_CACHE.put(0, InfiniFloat.ONE);
        FACTORIAL_CACHE.put(1, InfiniFloat.ONE);
        FACTORIAL_CACHE.put(2, InfiniFloat.TWO);
    }
}
