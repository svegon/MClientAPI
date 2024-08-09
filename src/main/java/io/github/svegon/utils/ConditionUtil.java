package io.github.svegon.utils;

import io.github.svegon.mclientapi.util.collections.chars.CharCompressedTableSet;
import com.google.common.collect.Sets;
import io.github.svegon.utils.collections.SetUtil;
import it.unimi.dsi.fastutil.booleans.BooleanPredicate;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.floats.FloatOpenHashSet;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import it.unimi.dsi.fastutil.shorts.ShortSet;

import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public final class ConditionUtil {
    private ConditionUtil() {
        throw new UnsupportedOperationException();
    }

    private static final CharPredicate DIGIT_PREDICATE = c -> '0' <= c && c <= '9';
    private static final Predicate<String> INTEGER_PREDICATE = s -> {
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);

            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    };

    public static final int ENUM_MODIFIER = 0x00004000;

    public static <T> Predicate<T> uniquenessPredicate() {
        return new Predicate<>() {
            private final Set<T> presentElements = Sets.newHashSet();

            @Override
            public boolean test(T o) {
                return presentElements.add(o);
            }
        };
    }

    public static BooleanPredicate booleanUniquenessPredicate() {
        return new BooleanPredicate() {
            boolean _false;
            boolean _true;

            @Override
            public boolean test(boolean bl) {
                if (bl) {
                    if (_true) {
                        return false;
                    } else {
                        return _true = true;
                    }
                } else {
                    if (_false) {
                        return false;
                    } else {
                        return _false = true;
                    }
                }
            }
        };
    }

    public static BytePredicate byteUniquenessPredicate() {
        return new BytePredicate() {
            private final ByteSet presentElements = new ByteArraySet(256);

            @Override
            public boolean test(byte o) {
                return presentElements.add(o);
            }
        };
    }

    public static ShortPredicate shortUniquenessPredicate() {
        return new ShortPredicate() {
            private final ShortSet presentElements = SetUtil.mapToShort(new CharCompressedTableSet(),
                    c -> (short) c, s -> (char) s);

            @Override
            public boolean test(short o) {
                return !presentElements.add(o);
            }
        };
    }

    public static CharPredicate charUniquenessPredicate() {
        return new CharPredicate() {
            private final CharSet presentElements = new CharCompressedTableSet();

            @Override
            public boolean test(char o) {
                return presentElements.add(o);
            }
        };
    }

    public static IntPredicate intUniquenessPredicate() {
        return new IntPredicate() {
            private final IntSet presentElements = new IntOpenHashSet();

            @Override
            public boolean test(int o) {
                return presentElements.add(o);
            }
        };
    }

    public static LongPredicate longUniquenessPredicate() {
        return new LongPredicate() {
            private final LongSet presentElements = new LongOpenHashSet();

            @Override
            public boolean test(long o) {
                return presentElements.add(o);
            }
        };
    }

    public static FloatPredicate floatUniquenessPredicate() {
        return new FloatPredicate() {
            private final FloatSet presentElements = new FloatOpenHashSet();

            @Override
            public boolean test(float o) {
                return presentElements.add(o);
            }
        };
    }

    public static DoublePredicate doubleUniquenessPredicate() {
        return new DoublePredicate() {
            private final DoubleSet presentElements = new DoubleOpenHashSet();

            @Override
            public boolean test(double o) {
                return presentElements.add(o);
            }
        };
    }

    public static <T> Predicate<T> duplicationPredicate() {
        return ConditionUtil.<T>duplicationPredicate().negate();
    }

    public static BooleanPredicate booleanDuplicationPredicate() {
        return new BooleanPredicate() {
            boolean _false;
            boolean _true;

            @Override
            public boolean test(boolean bl) {
                if (bl) {
                    if (_true) {
                        return true;
                    } else {
                        _true = true;
                        return false;
                    }
                } else {
                    if (_false) {
                        return true;
                    } else {
                        _false = true;
                        return false;
                    }
                }
            }
        };
    }

    public static BytePredicate byteDuplicationPredicate() {
        return byteUniquenessPredicate().negate();
    }

    public static CharPredicate charDuplicationPredicate() {
        return charUniquenessPredicate().negate();
    }

    public static ShortPredicate shortDuplicationPredicate() {
        return shortUniquenessPredicate().negate();
    }

    public static IntPredicate intDuplicationPredicate() {
        return intUniquenessPredicate().negate();
    }

    public static LongPredicate longDuplicationPredicate() {
        return longUniquenessPredicate().negate();
    }

    public static FloatPredicate floatDuplicationPredicate() {
        return floatUniquenessPredicate().negate();
    }

    public static DoublePredicate doubleDuplicationPredicate() {
        return doubleUniquenessPredicate().negate();
    }

    public static CharPredicate digitPredicate() {
        return DIGIT_PREDICATE;
    }

    public static Predicate<String> integerPredicate() {
        return INTEGER_PREDICATE;
    }

    public static boolean hasAllFlags(int data, int flags) {
        return (data & flags) == flags;
    }

    public static boolean hasAnyFlag(int data, int flags) {
        return (data & flags) != 0;
    }

    public static boolean hasAllFlags(long data, long flags) {
        return (data & flags) == flags;
    }

    public static boolean hasAnyFlag(long data, long flags) {
        return (data & flags) != 0;
    }

    public static boolean hasFlag(int data, int bit) {
        return (data & (1 << bit)) != 0;
    }

    public static boolean hasFlag(long data, long bit) {
        return (data & (1L << bit)) != 0;
    }
}
