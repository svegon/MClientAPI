package io.github.svegon.utils.collections;

import io.github.svegon.utils.fast.util.booleans.AbstractBooleanMultiset;
import io.github.svegon.utils.fast.util.booleans.BooleanMultiset;
import io.github.svegon.utils.fast.util.bytes.AbstractByteMultiset;
import io.github.svegon.utils.fast.util.bytes.ByteMultiset;
import io.github.svegon.utils.fast.util.chars.AbstractCharMultiset;
import io.github.svegon.utils.fast.util.chars.CharMultiset;
import io.github.svegon.utils.fast.util.doubles.AbstractDoubleMultiset;
import io.github.svegon.utils.fast.util.doubles.DoubleMultiset;
import io.github.svegon.utils.fast.util.floats.AbstractFloatMultiset;
import io.github.svegon.utils.fast.util.floats.FloatMultiset;
import io.github.svegon.utils.fast.util.ints.AbstractIntMultiset;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import io.github.svegon.utils.fast.util.longs.AbstractLongMultiset;
import io.github.svegon.utils.fast.util.longs.LongMultiset;
import io.github.svegon.utils.fast.util.objects.AbstractObjectMultiset;
import io.github.svegon.utils.fast.util.objects.ObjectMultiset;
import io.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterators;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharIterators;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.chars.CharSets;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterators;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.doubles.DoubleSets;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatIterators;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.floats.FloatSets;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongIterators;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterators;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import it.unimi.dsi.fastutil.shorts.ShortSets;
import org.jetbrains.annotations.NotNull;
import oshi.annotation.concurrent.Immutable;

import java.util.Map;
import java.util.Objects;

public enum MultisetUtil {
    ;

    public static final ObjectMultiset<?> EMPTY_OBJECT_MULTISET = new EmptyObjectMultiset();
    public static final BooleanMultiset EMPTY_BOOLEAN_MULTISET = new EmptyBooleanMultiset();
    public static final ByteMultiset EMPTY_BYTE_MULTISET = new EmptyByteMultiset();
    public static final CharMultiset EMPTY_CHAR_MULTISET = new EmptyCharMultiset();
    public static final ShortMultiset EMPTY_SHORT_MULTISET = new EmptyShortMultiset();
    public static final IntMultiset EMPTY_INT_MULTISET = new EmptyIntMultiset();
    public static final LongMultiset EMPTY_LONG_MULTISET = new EmptyLongMultiset();
    public static final FloatMultiset EMPTY_FLOAT_MULTISET = new EmptyFloatMultiset();
    public static final DoubleMultiset EMPTY_DOUBLE_MULTISET = new EmptyDoubleMultiset();

    public static final ObjectMultiset<?> SINGLETON_NULL_MULTISET = new SingletonNullMultiset();
    public static final SingletonBooleanMultiset SINGLETON_FALSE_MULTISET = new SingletonBooleanMultiset(false);
    public static final SingletonBooleanMultiset SINGLETON_TRUE_MULTISET = new SingletonBooleanMultiset(true);

    @SuppressWarnings("unchecked")
    public static <E> ObjectMultiset<E> emptyMultiset() {
        return (ObjectMultiset<E>) EMPTY_OBJECT_MULTISET;
    }

    public static BooleanMultiset emptyBooleanMultiset() {
        return EMPTY_BOOLEAN_MULTISET;
    }

    public static ByteMultiset emptyByteMultiset() {
        return EMPTY_BYTE_MULTISET;
    }

    public static CharMultiset emptyCharMultiset() {
        return EMPTY_CHAR_MULTISET;
    }

    public static ShortMultiset emptyShortMultiset() {
        return EMPTY_SHORT_MULTISET;
    }

    public static IntMultiset emptyIntMultiset() {
        return EMPTY_INT_MULTISET;
    }

    public static LongMultiset emptyLongMultiset() {
        return EMPTY_LONG_MULTISET;
    }

    public static FloatMultiset emptyFloatMultiset() {
        return EMPTY_FLOAT_MULTISET;
    }

    public static DoubleMultiset emptyDoubleMultiset() {
        return EMPTY_DOUBLE_MULTISET;
    }

    @SuppressWarnings("unchecked")
    public static <E> ObjectMultiset<E> singleton(E s) {
        return s == null ? (ObjectMultiset<E>) SINGLETON_NULL_MULTISET : new SingletonObjectMultiset<>(s);
    }

    public static SingletonBooleanMultiset singleton(boolean bl) {
        return bl ? SINGLETON_TRUE_MULTISET : SINGLETON_FALSE_MULTISET;
    }

    public static ByteMultiset singleton(byte b) {
        return new SingletonByteMultiset(b);
    }

    public static CharMultiset singleton(char c) {
        return new SingletonCharMultiset(c);
    }

    public static ShortMultiset singleton(short s) {
        return new SingletonShortMultiset(s);
    }

    public static IntMultiset singleton(int b) {
        return new SingletonIntMultiset(b);
    }

    public static LongMultiset singleton(long l) {
        return new SingletonLongMultiset(l);
    }

    public static FloatMultiset singleton(float f) {
        return new SingletonFloatMultiset(f);
    }

    public static DoubleMultiset singleton(double d) {
        return new SingletonDoubleMultiset(d);
    }

    @SuppressWarnings("unchecked")
    public static <E> Multiset.Entry<E> immutableEntry(E obj, int count) {
        Preconditions.checkArgument(count > 0, "count must be positive: %f", count);
        return obj == null ? (Multiset.Entry<E>) (count == 1 ? SINGLETON_NULL_MULTISET : new LNullEntry(count))
                : new LEntry<>(obj, count);
    }

    public static BooleanMultiset.Entry immutableEntry(boolean element, int count) {
        Preconditions.checkArgument(count > 0, "count must be positive: %f", count);
        return count == 1 ? singleton(element) : new ZEntry(element, count);
    }

    public static BEntry immutableEntry(byte element, int count) {
        Preconditions.checkArgument(count > 0, "count must be positive: %f", count);
        return new BEntry(element, count);
    }

    public static SEntry immutableEntry(short element, int count) {
        Preconditions.checkArgument(count > 0, "count must be positive: %f", count);
        return new SEntry(element, count);
    }

    public static IEntry immutableEntry(int element, int count) {
        Preconditions.checkArgument(count > 0, "count must be positive: %f", count);
        return new IEntry(element, count);
    }

    public static final class EmptyObjectMultiset extends AbstractObjectMultiset<Object> {
        private EmptyObjectMultiset() {

        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public ObjectIterator<Object> iterator() {
            return ObjectIterators.emptyIterator();
        }

        @Override
        public int count(Object value) {
            return 0;
        }

        @Override
        public boolean contains(Object key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public @NotNull ObjectSet<Multiset.Entry<Object>> entrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected ObjectSet<Object> initElementSet() {
            return ObjectSets.emptySet();
        }
    }

    private static class SingletonObjectMultiset<E> extends AbstractObjectMultiset<E>
            implements Multiset.Entry<E> {
        private final E element;

        private SingletonObjectMultiset(final E element) {
            this.element = element;
        }

        @Override
        public ObjectIterator<E> iterator() {
            return ObjectIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(Object value) {
            return element.equals(value) ? 1 : 0;
        }

        @Override
        public boolean contains(Object key) {
            return element.equals(key);
        }

        @Override
        public @NotNull ObjectSet<Multiset.Entry<E>> entrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected ObjectSet<E> initElementSet() {
            return ObjectSets.singleton(element);
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return getElement().hashCode() ^ getCount();
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }

    public static final class SingletonNullMultiset extends SingletonObjectMultiset<Object> {
        private SingletonNullMultiset() {
            super(null);
        }

        @Override
        public int count(Object value) {
            return value == null ? 1 : 0;
        }

        @Override
        public boolean contains(Object key) {
            return key == null;
        }

        @Override
        public int hashCode() {
            return getCount();
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    public static final class EmptyBooleanMultiset extends AbstractBooleanMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public BooleanIterator iterator() {
            return BooleanIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(boolean value) {
            return 0;
        }

        @Override
        public boolean contains(boolean key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<BooleanMultiset.Entry> booleanEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected BooleanSet initElementSet() {
            return BooleanSets.emptySet();
        }
    }

    public static final class SingletonBooleanMultiset extends AbstractBooleanMultiset implements BooleanMultiset.Entry {
        private final boolean element;

        private SingletonBooleanMultiset(boolean element) {
            this.element = element;
        }

        @Override
        public BooleanIterator iterator() {
            return BooleanIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(boolean value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(boolean key) {
            return key == element;
        }

        @Override
        public ObjectSet<BooleanMultiset.Entry> booleanEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected BooleanSet initElementSet() {
            return BooleanSets.singleton(element);
        }

        @Override
        public boolean getBooleanElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Boolean.hashCode(getBooleanElement()) ^ getCount();
        }

        @Override
        public String toString() {
            return String.valueOf(element);
        }
    }
    
    public static final class EmptyByteMultiset extends AbstractByteMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public @NotNull ByteIterator iterator() {
            return ByteIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(byte value) {
            return 0;
        }

        @Override
        public boolean contains(byte key) {
            return false;
        }

        @Override
        public byte[] toArray(byte[] a) {
            return a != null ? a : ByteArrays.EMPTY_ARRAY;
        }

        @Override
        public boolean addAll(it.unimi.dsi.fastutil.bytes.ByteCollection c) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<ByteMultiset.Entry> byteEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected ByteSet initElementSet() {
            return ByteSets.emptySet();
        }
    }

    public static final class SingletonByteMultiset extends AbstractByteMultiset implements ByteMultiset.Entry {
        private final byte element;

        private SingletonByteMultiset(byte element) {
            this.element = element;
        }

        @Override
        public @NotNull ByteIterator iterator() {
            return ByteIterators.singleton(element);
        }

        @Override
        public int count(byte value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(byte key) {
            return key == element;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ObjectSet<ByteMultiset.Entry> byteEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected ByteSet initElementSet() {
            return ByteSets.singleton(element);
        }

        @Override
        public byte getByteElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteElement()) ^ getCount();
        }

        @Override
        public String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyCharMultiset extends AbstractCharMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public CharIterator iterator() {
            return CharIterators.EMPTY_ITERATOR;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int count(char value) {
            return 0;
        }

        @Override
        public boolean contains(char key) {
            return false;
        }

        @Override
        public ObjectSet<CharMultiset.Entry> charEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected CharSet initElementSet() {
            return CharSets.emptySet();
        }
    }

    public static final class SingletonCharMultiset extends AbstractCharMultiset implements CharMultiset.Entry {
        private final char element;

        private SingletonCharMultiset(char element) {
            this.element = element;
        }

        @Override
        public CharIterator iterator() {
            return CharIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(char value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(char key) {
            return key == element;
        }

        @Override
        public ObjectSet<CharMultiset.Entry> charEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected CharSet initElementSet() {
            return CharSets.singleton(element);
        }

        @Override
        public char getCharElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Character.hashCode(getCharElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyShortMultiset extends AbstractShortMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public ShortIterator iterator() {
            return ShortIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(short value) {
            return 0;
        }

        @Override
        public boolean contains(short key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected ShortSet initElementSet() {
            return ShortSets.emptySet();
        }
    }

    public static final class SingletonShortMultiset extends AbstractShortMultiset implements ShortMultiset.Entry {
        private final short element;

        private SingletonShortMultiset(short element) {
            this.element = element;
        }

        @Override
        public @NotNull ShortIterator iterator() {
            return ShortIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(short value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(short key) {
            return key == element;
        }

        @Override
        public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected ShortSet initElementSet() {
            return ShortSets.singleton(element);
        }

        @Override
        public short getShortElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyIntMultiset extends AbstractIntMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public @NotNull IntIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int count(int value) {
            return 0;
        }

        @Override
        public boolean contains(int key) {
            return false;
        }

        @Override
        public ObjectSet<IntMultiset.Entry> intEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected IntSet initElementSet() {
            return IntSets.emptySet();
        }
    }

    public static final class SingletonIntMultiset extends AbstractIntMultiset implements IntMultiset.Entry {
        private final int element;

        private SingletonIntMultiset(int element) {
            this.element = element;
        }

        @Override
        public @NotNull IntIterator iterator() {
            return IntIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(int value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(int key) {
            return key == element;
        }

        @Override
        public ObjectSet<IntMultiset.Entry> intEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected IntSet initElementSet() {
            return IntSets.singleton(element);
        }

        @Override
        public int getIntElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyLongMultiset extends AbstractLongMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public @NotNull LongIterator iterator() {
            return LongIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(long value) {
            return 0;
        }

        @Override
        public boolean contains(long key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<LongMultiset.Entry> longEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected LongSet initElementSet() {
            return LongSets.emptySet();
        }
    }

    public static final class SingletonLongMultiset extends AbstractLongMultiset implements LongMultiset.Entry {
        private final long element;

        private SingletonLongMultiset(long element) {
            this.element = element;
        }

        @Override
        public @NotNull LongIterator iterator() {
            return LongIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(long value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(long key) {
            return key == element;
        }

        @Override
        public ObjectSet<LongMultiset.Entry> longEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected LongSet initElementSet() {
            return LongSets.singleton(element);
        }

        @Override
        public long getLongElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(getLongElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyFloatMultiset extends AbstractFloatMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public @NotNull FloatIterator iterator() {
            return FloatIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(float value) {
            return 0;
        }

        @Override
        public boolean contains(float key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<FloatMultiset.Entry> floatEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected FloatSet initElementSet() {
            return FloatSets.emptySet();
        }
    }

    public static final class SingletonFloatMultiset extends AbstractFloatMultiset implements FloatMultiset.Entry {
        private final float element;

        private SingletonFloatMultiset(float element) {
            this.element = element;
        }

        @Override
        public FloatIterator iterator() {
            return FloatIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(float value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(float key) {
            return key == element;
        }

        @Override
        public ObjectSet<FloatMultiset.Entry> floatEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected FloatSet initElementSet() {
            return FloatSets.singleton(element);
        }

        @Override
        public float getFloatElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Float.hashCode(getFloatElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    public static final class EmptyDoubleMultiset extends AbstractDoubleMultiset {
        @Override
        public boolean equals(Object o) {
            return o instanceof Multiset<?> c && c.isEmpty();
        }

        @Override
        public @NotNull DoubleIterator iterator() {
            return DoubleIterators.EMPTY_ITERATOR;
        }

        @Override
        public int count(double value) {
            return 0;
        }

        @Override
        public boolean contains(double key) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ObjectSet<DoubleMultiset.Entry> doubleEntrySet() {
            return ObjectSets.emptySet();
        }

        @Override
        protected DoubleSet initElementSet() {
            return DoubleSets.emptySet();
        }
    }

    public static final class SingletonDoubleMultiset extends AbstractDoubleMultiset implements DoubleMultiset.Entry {
        private final double element;

        private SingletonDoubleMultiset(double element) {
            this.element = element;
        }

        @Override
        public @NotNull DoubleIterator iterator() {
            return DoubleIterators.singleton(element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int count(double value) {
            return value == element ? 1 : 0;
        }

        @Override
        public boolean contains(double key) {
            return key == element;
        }

        @Override
        public ObjectSet<DoubleMultiset.Entry> doubleEntrySet() {
            return ObjectSets.singleton(this);
        }

        @Override
        protected DoubleSet initElementSet() {
            return DoubleSets.singleton(element);
        }

        @Override
        public double getDoubleElement() {
            return element;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(getDoubleElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(element);
        }
    }

    @Immutable
    public static final class LEntry<E> extends AbstractMultiset.Entry<E> implements Object2IntMap.Entry<E> {
        final E element;
        final int count;

        LEntry(E element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Multiset.Entry<?> e && getElement().equals(e.getElement())
                    && getCount() == e.getCount()) || (obj instanceof Map.Entry<?, ?> entry
                    && entry.getValue() instanceof Integer && getKey().equals(entry.getKey())
                    && ((int) entry.getValue()) == getCount());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getElement()) ^ getCount();
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    public static final class LNullEntry extends AbstractMultiset.Entry<Object> {
        final int count;

        LNullEntry(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Multiset.Entry<?> e && e.getElement() == null
                    && e.getCount() == getCount()) || (obj instanceof Map.Entry<?, ?> entry
                    && entry.getValue() instanceof Integer && entry.getKey() == null
                    && ((Integer) entry.getValue()) == getCount());
        }

        @Override
        public int hashCode() {
            return count;
        }

        @Override
        public @NotNull String toString() {
            return "null x " + getCount();
        }

        @Override
        public Object getElement() {
            return null;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    public static final class ZEntry extends AbstractBooleanMultiset.Entry {
        final boolean element;
        final int count;

        ZEntry(boolean element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public boolean getBooleanElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    public static final class BEntry extends AbstractByteMultiset.Entry {
        final byte element;
        final int count;

        BEntry(byte element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public byte getByteElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    public static final class CEntry extends AbstractCharMultiset.Entry {
        final char element;
        final int count;

        CEntry(char element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public char getCharElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    public static final class SEntry extends AbstractShortMultiset.Entry {
        final short element;
        final int count;

        SEntry(short element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public short getShortElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }

    @Immutable
    public static final class IEntry extends AbstractIntMultiset.Entry {
        final int element;
        final int count;

        IEntry(int element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public int getIntElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }
    }
}
