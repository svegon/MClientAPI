package com.github.svegon.utils.collections;

import com.github.svegon.utils.FunctionUtil;
import com.github.svegon.utils.fast.util.bytes.transform.bytes.L2BKL2BVDefRetValueRedirectingTranformingMap;
import com.github.svegon.utils.fast.util.bytes.transform.bytes.L2BKL2BVTranformingMap;
import com.github.svegon.utils.fast.util.objects.LateBindingSupplier;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LKI2IVTranformingMap;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LKL2LVDefRetValueRedirectingTranformingMap;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LKL2LVTranformingMap;
import com.github.svegon.utils.fast.util.shorts.transform.objects.L2SKL2LVDefRetValueRedirectingTransformingMap;
import com.github.svegon.utils.fast.util.shorts.transform.objects.L2SKL2LVTranformingMap;
import com.github.svegon.utils.fast.util.shorts.transform.objects.L2SKL2SVDefRetValueRedirectingTransformingMap;
import com.github.svegon.utils.fast.util.shorts.transform.objects.L2SKL2SVTranformingMap;
import com.github.svegon.utils.fast.util.shorts.transform.shorts.S2SKS2SVTranformingMap;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.Char2IntMap;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public final class MapUtil {
    private MapUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * approximately the amount of items in a hash map and a table map
     * starting which it's faster to use the table map than use the hash map
     */
    public static final int HASH_MAP_EFFECTIVITY = 450;
    /**
     * (approximately) the maximum amount of entries at which
     * it's still faster to use a fork table with branch
     * factor 4 than with branch factor 3
     */
    public static final int FORK_TABLE_FACTOR_4_EFFECTIVITY = 990;
    /**
     * (approximately) the maximum amount of values out of which
     * there should be at least one set for a table map
     * to still be more effective than a fork table
     * with branch factor 4
     */
    public static final double MAX_EFFECTIVE_FRAGMENTATION_OF_KEYS_FOR_TABLE_MAP = 18192;
    /**
     * (approximately) the maximum amount of values out of which
     * there should be at least one set for a direct access map
     * to still be more effective than a fork table
     * with branch factor 4
     */
    public static final double MAX_EFFECTIVE_FRAGMENTATION_OF_KEYS_FOR_HASH_MAP = 8;
    /**
     * note that according it to my tests it's more effective to use an AVL tree map
     * than an RB tree map in all cases
     */

    public static <K, V> FixedCacheCombinedMap<K, V> newFixedCacheCombinedMap(final ImmutableList<Map<K, V>> cache) {
        return new FixedCacheCombinedMap<>(cache);
    }

    public static <K, V> FixedCacheCombinedMap<K, V> newFixedCacheCombinedMap(
            final Map<? extends K, ? extends V>... initialMaps) {

        return new FixedCacheCombinedMap<>(initialMaps);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> FixedCacheCombinedMap<K, V> newFixedCacheCombinedMap(final Iterable<? extends Map<? extends K,
            ? extends V>> initialMaps) {
        return initialMaps instanceof Collection ? new FixedCacheCombinedMap<>((Collection<? extends Map<? extends K,
                ? extends V>>) initialMaps) : newFixedCacheCombinedMap(initialMaps.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <K, V> FixedCacheCombinedMap<K, V> newFixedCacheCombinedMap(final Iterator<? extends Map<? extends K,
            ? extends V>> initialMaps) {
        return newFixedCacheCombinedMap((ImmutableList<Map<K, V>>) ImmutableList.copyOf(initialMaps));
    }

    public static <K_IN, V_IN, K_OUT, V_OUT> Object2ObjectMap<K_OUT, V_OUT> transformKToObjectVToObject(
            Map<K_IN, V_IN> map, Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
            Function<? super K_OUT, ? extends K_IN> backingKeyTransformer,
            Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
            Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        return map instanceof Object2ObjectMap<K_IN, V_IN> m ? new L2LKL2LVDefRetValueRedirectingTranformingMap<>(m,
                forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer, backingValueTransformer) :
                new L2LKL2LVTranformingMap<>(map, forwardingKeyTransformer, backingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    public static <K_IN, K_OUT> Object2IntMap<K_OUT> transformKToObjectVToInt(Object2IntMap<K_IN> map,
                                                      Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                                      Function<? super K_OUT, ? extends K_IN> backingKeyTransformer,
                                                      IntUnaryOperator forwardingValueTransformer,
                                                      IntUnaryOperator backingValueTransformer) {
        return new L2LKI2IVTranformingMap<>(map, forwardingKeyTransformer, backingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    public static <K_IN, V_IN, V_OUT> Short2ObjectMap< V_OUT> transformKToShortVToObject(
            Map<K_IN, V_IN> map, Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
            Short2ObjectFunction<? extends K_IN> backingKeyTransformer,
            Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
            Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        return map instanceof Object2ObjectMap<K_IN, V_IN> m ? new L2SKL2LVDefRetValueRedirectingTransformingMap<>(m,
                forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer, backingValueTransformer) :
                new L2SKL2LVTranformingMap<>(map, forwardingKeyTransformer, backingKeyTransformer,
                        forwardingValueTransformer, backingValueTransformer);
    }

    public static <K_IN, V_IN> L2SKL2SVTranformingMap<K_IN, V_IN> transformKToShortVToShort(
            Map<K_IN, V_IN> map, Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
            Short2ObjectFunction<? extends K_IN> backingKeyTransformer,
            Object2ShortFunction<? super V_IN> forwardingValueTransformer,
            Short2ObjectFunction<? extends V_IN> backingValueTransformer) {
        return map instanceof Object2ObjectMap<K_IN, V_IN> m ? new L2SKL2SVDefRetValueRedirectingTransformingMap<>(m,
                forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer, backingValueTransformer) :
                new L2SKL2SVTranformingMap<>(map, forwardingKeyTransformer, backingKeyTransformer,
                        forwardingValueTransformer, backingValueTransformer);
    }

    public static S2SKS2SVTranformingMap mapKToShortVToShort(
            Short2ShortMap map, ShortUnaryOperator forwardingKeyTransformer, ShortUnaryOperator backingKeyTransformer,
            ShortUnaryOperator forwardingValueTransformer, ShortUnaryOperator backingValueTransformer) {
        return new S2SKS2SVTranformingMap(map, forwardingKeyTransformer, backingKeyTransformer,
                        forwardingValueTransformer, backingValueTransformer);
    }

    public static <K_IN, V_IN> L2BKL2BVTranformingMap<K_IN, V_IN> transformKToByteVToByte(
            Map<K_IN, V_IN> map, Object2ByteFunction<? super K_IN> forwardingKeyTransformer,
            Byte2ObjectFunction<? extends K_IN> backingKeyTransformer,
            Object2ByteFunction<? super V_IN> forwardingValueTransformer,
            Byte2ObjectFunction<? extends V_IN> backingValueTransformer) {
        return map instanceof Object2ObjectMap<K_IN, V_IN> m ? new L2BKL2BVDefRetValueRedirectingTranformingMap<>(m,
                forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer, backingValueTransformer) :
                new L2BKL2BVTranformingMap<>(map, forwardingKeyTransformer, backingKeyTransformer,
                        forwardingValueTransformer, backingValueTransformer);
    }

    public static <K_IN, V, K_OUT> Object2ObjectMap<K_OUT, V> transformKToObject(
            Map<K_IN, V> map, Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
            Function<? super K_OUT, ? extends K_IN> backingKeyTransformer) {
        return transformKToObjectVToObject(map, forwardingKeyTransformer, backingKeyTransformer,
                FunctionUtil.identityOperator(), FunctionUtil.identityOperator());
    }

    public static <K_IN, K_OUT> Object2IntMap<K_OUT> transformKToObject(
            Object2IntMap<K_IN> map, Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
            Function<? super K_OUT, ? extends K_IN> backingKeyTransformer) {
        return transformKToObjectVToInt(map, forwardingKeyTransformer, backingKeyTransformer,
                FunctionUtil.intIdentityOperator(), FunctionUtil.intIdentityOperator());
    }

    public static <K, V_IN, V_OUT> Object2ObjectMap<K, V_OUT> transformVToObject(Map<K, V_IN> map,
            Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
            Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        return transformKToObjectVToObject(map, FunctionUtil.identityOperator(), FunctionUtil.identityOperator(),
                forwardingValueTransformer, backingValueTransformer);
    }

    public static <K, V> Object2ObjectMap<K, V> derefWeakRefK(final Map<WeakReference<K>, V> original) {
        return derefK(original, WeakReference::new);
    }

    public static <K> Object2IntMap<K> derefWeakRefK(final Object2IntMap<WeakReference<K>> original) {
        return derefK(original, WeakReference::new);
    }

    public static <K, V> Object2ObjectMap<K, V> derefSoftRefK(final Map<SoftReference<K>, V> original) {
        return derefK(original, SoftReference::new);
    }

    public static <K> Object2IntMap<K> derefSoftRefK(final Object2IntMap<SoftReference<K>> original) {
        return derefK(original, SoftReference::new);
    }

    public static <K, V> Object2ObjectMap<K, V> derefWeakRefV(final Map<K, WeakReference<V>> original) {
        return derefV(original, WeakReference::new);
    }

    public static <K, V> Object2ObjectMap<K, V> derefSoftRefV(final Map<K, SoftReference<V>> original) {
        return derefV(original, SoftReference::new);
    }

    public static <K, V, R extends Reference<K>> Object2ObjectMap<K, V> derefK(final Map<R, V> original,
                                                               Function<? super K, ? extends R> backingKeyTranformer) {
        return transformKToObject(original, ref -> {
            K k = ref.get();

            if (k == null) {
                original.remove(ref);
            }

            return k;
        }, backingKeyTranformer);
    }

    public static <K, R extends Reference<K>> Object2IntMap<K> derefK(final @NotNull Object2IntMap<R> original,
                                            final @NotNull Function<? super K, ? extends R> backingKeyTransformer) {
        return transformKToObject(original, ref -> {
            if (ref == null) {
                return null;
            }

            K k = ref.get();

            if (k == null) {
                original.removeInt(ref);
            }

            return k;
        }, (k) -> k == null ? null : backingKeyTransformer.apply(k));
    }

    public static <K, V, R extends Reference<V>> Object2ObjectMap<K, V> derefV(final Map<K, R> original,
                                                           Function<? super V, ? extends R> backingValueTranformer) {
        final LateBindingSupplier<V> defRetVal = new LateBindingSupplier<>();
        Object2ObjectMap<K, V> map = transformVToObject(original, ref -> {
            if (ref == null) {
                return null;
            }

            V v = ref.get();

            if (v == null) {
                original.values().remove(ref);
                return defRetVal.get();
            }

            return v;
        }, (v) -> v == null ? null : backingValueTranformer.apply(v));

        defRetVal.bind(map::defaultReturnValue);
        return map;
    }

    public static <K, V, R_K extends Reference<K>, R_V extends Reference<V>> Object2ObjectMap<K, V>
    deref(final Map<R_K, R_V> original, Function<? super K, ? extends R_K> backingKeyTransformer,
                                                      Function<? super V, ? extends R_V> backingValueTranformer) {
        final LateBindingSupplier<V> defRetVal = new LateBindingSupplier<>();
        Object2ObjectMap<K, V> map = transformKToObjectVToObject(original, ref -> {
            if (ref == null) {
                return null;
            }

            K k = ref.get();

            if (k == null) {
                original.remove(ref);
            }

            return k;
        }, (k) -> k == null ? null : backingKeyTransformer.apply(k), ref -> {
            if (ref == null) {
                return null;
            }

            V v = ref.get();

            if (v == null) {
                original.values().remove(ref);
                return defRetVal.get();
            }

            return v;
        }, (v) -> v == null ? null : backingValueTranformer.apply(v));

        defRetVal.bind(map::defaultReturnValue);
        return map;
    }

    public static <K, V> Map.Entry<K, V> mutableEntry(K key) {
        return key == null ? new MutableNullKeyEntry<>() : new MutableNotNullKeyEntry<>(key);
    }

    public static <K, V> Map.Entry<K, V> mutableEntry(K key, V value) {
        return key == null ? new MutableNullKeyEntry<>(value) : new MutableNotNullKeyEntry<>(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Object2ObjectMap.Entry<K, V> immutableEntry(K key, V value) {
        return key == null ? value == null ? (Object2ObjectMap.Entry<K, V>) ImmutableNull2NullEntry.INSTANCE
                : new ImmutableNullKeyEntry<>(value) : value == null ? new ImmutableNullValueEntry<>(key)
                : new ImmutableNotNullEntry<>(key, value);
    }

    public static <K> Object2BooleanMap.Entry<K> immutableEntry(K key, boolean value) {
        return key == null ? (Object2BooleanMap.Entry<K>) (value ? ImmutableNullTrueL2ZEntry.INSTANCE
                : ImmutableNullFalseL2ZEntry.INSTANCE) : new ImmutableNotNullL2ZEntry<>(key, value);
    }

    public static <K> Object2ByteMap.Entry<K> immutableEntry(K key, byte value) {
        return key == null ? new ImmutableNullL2BEntry<>(value) : new ImmutableNotNullL2BEntry<K>(key, value);
    }

    public static <K> Object2IntMap.Entry<K> immutableEntry(K key, int value) {
        return key == null ? new ImmutableNullL2IEntry<>(value) : new ImmutableNotNullL2IEntry<K>(key, value);
    }

    public static <K> Object2FloatMap.Entry<K> immutableEntry(K key, float value) {
        return key == null ? new ImmutableNullL2FEntry<>(value) : new ImmutableNotNullL2FEntry<K>(key, value);
    }

    public static Byte2BooleanMap.Entry immutableEntry(byte key, boolean value) {
        return new ImmutableB2ZEntry(key, value);
    }

    public static Byte2ByteMap.Entry immutableEntry(byte key, byte value) {
        return new ImmutableB2BEntry(key, value);
    }

    public static Byte2CharMap.Entry immutableEntry(byte key, char value) {
        return new ImmutableB2CEntry(key, value);
    }

    public static Byte2ShortMap.Entry immutableEntry(byte key, short value) {
        return new ImmutableB2SEntry(key, value);
    }

    public static Byte2IntMap.Entry immutableEntry(byte key, int value) {
        return new ImmutableB2IEntry(key, value);
    }

    public static Char2IntMap.Entry immutableEntry(char key, int value) {
        return new ImmutableC2IEntry(key, value);
    }

    public static Short2IntMap.Entry immutableEntry(short key, int value) {
        return new ImmutableS2IEntry(key, value);
    }

    public static Int2BooleanMap.Entry immutableEntry(int key, boolean value) {
        return new ImmutableI2ZEntry(key, value);
    }

    public static Int2ByteMap.Entry immutableEntry(int key, byte value) {
        return new ImmutableI2BEntry(key, value);
    }

    public static Int2CharMap.Entry immutableEntry(int key, char value) {
        return new ImmutableI2CEntry(key, value);
    }

    public static Int2ShortMap.Entry immutableEntry(int key, short value) {
        return new ImmutableI2SEntry(key, value);
    }

    public static Int2IntMap.Entry immutableEntry(int key, int value) {
        return new ImmutableI2IEntry(key, value);
    }

    public static Int2LongMap.Entry immutableEntry(int key, long value) {
        return new ImmutableI2JEntry(key, value);
    }

    public static Int2FloatMap.Entry immutableEntry(int key, float value) {
        return new ImmutableI2FEntry(key, value);
    }

    public static Int2DoubleMap.Entry immutableEntry(int key, double value) {
        return new ImmutableI2DEntry(key, value);
    }

    public abstract static class MutableEntry<K, V> implements Object2ObjectMap.Entry<K, V> {
        private V value;

        public MutableEntry(V value) {
            this.value = value;
        }

        public MutableEntry() {
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V v = this.value;
            this.value = value;
            return v;
        }

        @Override
        public abstract boolean equals(Object o);

        @Override
        public abstract int hashCode();

        @Override
        public abstract String toString();
    }

    public static class MutableNullKeyEntry<K, V> extends MutableEntry<K, V> {
        public MutableNullKeyEntry() {
        }

        public MutableNullKeyEntry(V value) {
            super(value);
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that.getKey() == null && Objects.equals(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return "null=" + getValue();
        }
    }

    public static class MutableNotNullKeyEntry<K, V> extends MutableEntry<K, V> {
        private final K key;

        protected MutableNotNullKeyEntry(K key) {
            this.key = key;
        }

        protected MutableNotNullKeyEntry(K key, V value) {
            super(value);
            this.key = key;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return getKey().equals(that.getKey()) && Objects.equals(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return 31 * getKey().hashCode() + Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    @Immutable
    private record ImmutableNotNullEntry<K, V>(@NotNull K key, @NotNull V value)
            implements Object2ObjectMap.Entry<K, V> {
        private ImmutableNotNullEntry(@NotNull K key, @NotNull V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> that))
                return false;

            return getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return getKey().hashCode() ^ getValue().hashCode();
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Immutable
    private record ImmutableNullKeyEntry<K, V>(@NotNull V value) implements Object2ObjectMap.Entry<K, V> {
        private ImmutableNullKeyEntry(@NotNull V value) {
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that.getKey() == null && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return getValue().hashCode();
        }

        @Override
        public String toString() {
            return "null=" + value;
        }
    }

    @Immutable
    private record ImmutableNullValueEntry<K, V>(@NotNull K key) implements Object2ObjectMap.Entry<K, V> {
        private ImmutableNullValueEntry(@NotNull K key) {
            this.key = key;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return getKey().equals(that.getKey()) && that.getValue() == null;
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }

        @Override
        public String toString() {
            return key + "=null";
        }
    }

    @Immutable
    private static final class ImmutableNull2NullEntry<K, V> implements Object2ObjectMap.Entry<K, V> {
        private static final ImmutableNull2NullEntry<?, ?> INSTANCE = new ImmutableNull2NullEntry<>();

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that.getKey() == that.getValue() && that.getValue() == null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "null=null";
        }
    }

    @Immutable
    private record ImmutableNotNullL2ZEntry<K>(@NotNull K key, boolean value) implements Object2BooleanMap.Entry<K> {
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public boolean getBooleanValue() {
            return value;
        }

        @Override
        public boolean setValue(boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return getKey().equals(that.getKey()) && (that instanceof Object2BooleanMap.Entry<?> e
                    ? getBooleanValue() == e.getBooleanValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return getKey().hashCode() ^ Boolean.hashCode(getBooleanValue());
        }

        @Override
        public String toString() {
            return getKey() +  "=" + getBooleanValue();
        }
    }

    @Immutable
    private record ImmutableNullFalseL2ZEntry<K>() implements Object2BooleanMap.Entry<K> {
        static final ImmutableNullFalseL2ZEntry<?> INSTANCE = new ImmutableNullFalseL2ZEntry<>();

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public boolean getBooleanValue() {
            return false;
        }

        @Override
        public boolean setValue(boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return null == that.getKey() && (that instanceof Object2BooleanMap.Entry<?> e
                    ? !e.getBooleanValue() : Boolean.FALSE.equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return 1237;
        }

        @Override
        public String toString() {
            return "null=false";
        }
    }

    @Immutable
    private record ImmutableNullTrueL2ZEntry<K>() implements Object2BooleanMap.Entry<K> {
        static final ImmutableNullTrueL2ZEntry<?> INSTANCE = new ImmutableNullTrueL2ZEntry<>();

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public boolean getBooleanValue() {
            return true;
        }

        @Override
        public boolean setValue(boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return null == that.getKey() && (that instanceof Object2BooleanMap.Entry<?> e
                    ? e.getBooleanValue() : Boolean.TRUE.equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return 1231;
        }

        @Override
        public String toString() {
            return "null=true";
        }
    }

    @Immutable
    private record ImmutableNotNullL2BEntry<K>(@NotNull K key, byte value) implements Object2ByteMap.Entry<K> {
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public byte getByteValue() {
            return value;
        }

        @Override
        public byte setValue(byte value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return getKey().equals(that.getKey()) && (that instanceof Object2ByteMap.Entry<?> e
                    ? getByteValue() == e.getByteValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return getKey().hashCode() ^ Integer.hashCode(getByteValue());
        }

        @Override
        public String toString() {
            return getKey() +  "=" + value;
        }
    }

    @Immutable
    private record ImmutableNullL2BEntry<K>(byte value) implements Object2ByteMap.Entry<K> {
        private ImmutableNullL2BEntry(final byte value) {
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public byte getByteValue() {
            return value;
        }

        @Override
        public byte setValue(byte value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return null == that.getKey() && (that instanceof Object2ByteMap.Entry<?> e
                    ? getByteValue() == e.getByteValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getByteValue());
        }

        @Override
        public String toString() {
            return "null=" + value;
        }
    }

    @Immutable
    private record ImmutableNotNullL2IEntry<K>(@NotNull K key, int value) implements Object2IntMap.Entry<K> {
        private ImmutableNotNullL2IEntry(final @NotNull K key, final int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return getKey().equals(that.getKey()) && (that instanceof Object2IntMap.Entry<?> e
                    ? getIntValue() == e.getIntValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return getKey().hashCode() ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getKey() + "=" + value;
        }
    }

    @Immutable
    private record ImmutableNullL2IEntry<K>(int value) implements Object2IntMap.Entry<K> {
        private ImmutableNullL2IEntry(final int value) {
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return null == that.getKey() && (that instanceof Object2IntMap.Entry<?> e
                    ? getIntValue() == e.getIntValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return "null=" + value;
        }
    }

    @Immutable
    private record ImmutableNotNullL2FEntry<K>(@NotNull K key, float value) implements Object2FloatMap.Entry<K> {
        private ImmutableNotNullL2FEntry(@NotNull K key, final float value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public float getFloatValue() {
            return value;
        }

        @Override
        public float setValue(float value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> that))
                return false;

            return getKey().equals(that.getKey()) && (that instanceof Object2FloatMap.Entry<?> e
                    ? getFloatValue() == e.getFloatValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return getKey().hashCode() ^ Float.hashCode(getFloatValue());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Immutable
    private record ImmutableNullL2FEntry<K>(float value) implements Object2FloatMap.Entry<K> {
        private ImmutableNullL2FEntry(final float value) {
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that.getKey() == null && (that instanceof Object2FloatMap.Entry<?> e
                    ? getFloatValue() == e.getFloatValue() : getValue().equals(that.getValue()));
        }

        @Override
        public int hashCode() {
            return Float.hashCode(getFloatValue());
        }

        @Override
        public String toString() {
            return "null=" + value;
        }

        @Override
        public float getFloatValue() {
            return value;
        }

        @Override
        public float setValue(float value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    private record ImmutableB2ZEntry(byte key, boolean value) implements Byte2BooleanMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public boolean getBooleanValue() {
            return value;
        }

        @Override
        public boolean setValue(boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2BooleanMap.Entry e
                    ? getByteKey() == e.getByteKey() && getBooleanValue() == e.getBooleanValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Boolean.hashCode(getBooleanValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getBooleanValue();
        }
    }

    @Immutable
    private record ImmutableB2BEntry(byte key, byte value) implements Byte2ByteMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public byte getByteValue() {
            return value;
        }

        @Override
        public byte setValue(byte value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2ByteMap.Entry e
                    ? getByteKey() == e.getByteKey() && getByteValue() == e.getByteValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Byte.hashCode(getByteValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getByteValue();
        }
    }

    @Immutable
    private record ImmutableB2CEntry(byte key, char value) implements Byte2CharMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public char getCharValue() {
            return value;
        }

        @Override
        public char setValue(char value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2CharMap.Entry e
                    ? getByteKey() == e.getByteKey() && getCharValue() == e.getCharValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Character.hashCode(getCharValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getCharValue();
        }
    }

    @Immutable
    private record ImmutableB2SEntry(byte key, short value) implements Byte2ShortMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public short getShortValue() {
            return value;
        }

        @Override
        public short setValue(short value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2ShortMap.Entry e
                    ? getByteKey() == e.getByteKey() && getShortValue() == e.getShortValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Short.hashCode(getShortValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getShortValue();
        }
    }

    @Immutable
    private record ImmutableB2IEntry(byte key, int value) implements Byte2IntMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2IntMap.Entry e
                    ? getByteKey() == e.getByteKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getIntValue();
        }
    }

    @Immutable
    private record ImmutableB2JEntry(byte key, long value) implements Byte2LongMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public long getLongValue() {
            return value;
        }

        @Override
        public long setValue(long value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2LongMap.Entry e
                    ? getByteKey() == e.getByteKey() && getLongValue() == e.getLongValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Long.hashCode(getLongValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getLongValue();
        }
    }

    @Immutable
    private record ImmutableB2FEntry(byte key, float value) implements Byte2FloatMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public float getFloatValue() {
            return value;
        }

        @Override
        public float setValue(float value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2FloatMap.Entry e
                    ? getByteKey() == e.getByteKey() && getFloatValue() == e.getFloatValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Float.hashCode(getFloatValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getFloatValue();
        }
    }

    @Immutable
    private record ImmutableB2DEntry(byte key, double value) implements Byte2DoubleMap.Entry {
        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public double getDoubleValue() {
            return value;
        }

        @Override
        public double setValue(double value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Byte2DoubleMap.Entry e
                    ? getByteKey() == e.getByteKey() && getDoubleValue() == e.getDoubleValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Double.hashCode(getDoubleValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getDoubleValue();
        }
    }

    @Immutable
    private record ImmutableC2IEntry(char key, int value) implements Char2IntMap.Entry {
        @Override
        public char getCharKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Char2IntMap.Entry e
                    ? getCharKey() == e.getCharKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Character.hashCode(getCharKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getCharKey() + "=" + getIntValue();
        }
    }

    @Immutable
    private record ImmutableS2IEntry(short key, int value) implements Short2IntMap.Entry {
        @Override
        public short getShortKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Short2IntMap.Entry e
                    ? getShortKey() == e.getShortKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getShortKey() + "=" + getIntValue();
        }
    }

    @Immutable
    private record ImmutableI2ZEntry(int key, boolean value) implements Int2BooleanMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public boolean getBooleanValue() {
            return value;
        }

        @Override
        public boolean setValue(boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2BooleanMap.Entry e
                    ? getIntKey() == e.getIntKey() && getBooleanValue() == e.getBooleanValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Boolean.hashCode(getBooleanValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getBooleanValue();
        }
    }

    @Immutable
    private record ImmutableI2BEntry(int key, byte value) implements Int2ByteMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public byte getByteValue() {
            return value;
        }

        @Override
        public byte setValue(byte value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2ByteMap.Entry e
                    ? getIntKey() == e.getIntKey() && getByteValue() == e.getByteValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Byte.hashCode(getByteValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getByteValue();
        }
    }

    @Immutable
    private record ImmutableI2CEntry(int key, char value) implements Int2CharMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public char getCharValue() {
            return value;
        }

        @Override
        public char setValue(char value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2CharMap.Entry e
                    ? getIntKey() == e.getIntKey() && getCharValue() == e.getCharValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Character.hashCode(getCharValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getCharValue();
        }
    }

    @Immutable
    private record ImmutableI2SEntry(int key, short value) implements Int2ShortMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public short getShortValue() {
            return value;
        }

        @Override
        public short setValue(short value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2ShortMap.Entry e
                    ? getIntKey() == e.getIntKey() && getShortValue() == e.getShortValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Short.hashCode(getShortValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getShortValue();
        }
    }

    @Immutable
    private record ImmutableI2IEntry(int key, int value) implements Int2IntMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2IntMap.Entry e
                    ? getIntKey() == e.getIntKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getIntValue();
        }
    }

    @Immutable
    private record ImmutableI2JEntry(int key, long value) implements Int2LongMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public long getLongValue() {
            return value;
        }

        @Override
        public long setValue(long value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2LongMap.Entry e
                    ? getIntKey() == e.getIntKey() && getLongValue() == e.getLongValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Long.hashCode(getLongValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getLongValue();
        }
    }

    @Immutable
    private record ImmutableI2FEntry(int key, float value) implements Int2FloatMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public float getFloatValue() {
            return value;
        }

        @Override
        public float setValue(float value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2FloatMap.Entry e
                    ? getIntKey() == e.getIntKey() && getFloatValue() == e.getFloatValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Float.hashCode(getFloatValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getFloatValue();
        }
    }

    @Immutable
    private record ImmutableI2DEntry(int key, double value) implements Int2DoubleMap.Entry {
        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public double getDoubleValue() {
            return value;
        }

        @Override
        public double setValue(double value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> that)) {
                return false;
            }

            return that instanceof Int2DoubleMap.Entry e
                    ? getIntKey() == e.getIntKey() && getDoubleValue() == e.getDoubleValue()
                    : getKey().equals(that.getKey()) && getValue().equals(that.getValue());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Double.hashCode(getDoubleValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getDoubleValue();
        }
    }
}
