package io.github.svegon.utils.fast.util.bytes;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.fast.util.bytes.ints.Byte2IntTableMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractByteMultiset extends AbstractMultiset<Byte> implements ByteMultiset {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Multiset<?> multiset)) {
            return false;
        }

        if (size() != multiset.size()) {
            return false;
        }

        if (multiset instanceof ByteMultiset) {
            for (ByteMultiset.Entry e : ((ByteMultiset) multiset).byteEntrySet()) {
                if (e.getCount() != count(e.getByteElement())) {
                    return false;
                }
            }
        } else {
            for (Multiset.Entry<?> e : multiset.entrySet()) {
                if (e.getCount() != count(e.getElement())) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public abstract @NotNull ByteIterator iterator();

    @Override
    public boolean add(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(byte value) {
        int c = 0;
        ByteIterator it = iterator();

        while (it.hasNext())
            if (it.nextByte() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull ByteSet elementSet() {
        return (ByteSet) super.elementSet();
    }

    @Override
    public int add(byte value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(byte value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(byte key) {
        for (byte b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(byte key) {
        ByteIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextByte() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(byte value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(byte value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<ByteMultiset.Entry> byteEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractByteMultiset.this.clear();
            }

            @Override
            public @NotNull ObjectIterator<ByteMultiset.Entry> iterator() {
                final var it = entriesFrame().byte2IntEntrySet().iterator();

                return new ObjectIterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public ByteMultiset.Entry next() {
                        final var e = it.next();

                        return new Entry() {
                            @Override
                            public byte getByteElement() {
                                return e.getByteKey();
                            }

                            @Override
                            public int getCount() {
                                return e.getIntValue();
                            }

                            @Override
                            public int setValue(int value) {
                                return setCount(getByteElement(), value);
                            }
                        };
                    }
                };
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof AbstractMultiset.Entry<?> e
                        && AbstractByteMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractByteMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Byte2IntTableMap entriesFrame() {
                Byte2IntTableMap map = new Byte2IntTableMap();

                for (byte b : AbstractByteMultiset.this) {
                    map.put(b, map.get(b) + 1);
                }

                return map;
            }
        };
    }

    @Override
    protected ByteSet initElementSet() {
        return new AbstractByteSet() {
            private final Set<ByteMultiset.Entry> es = byteEntrySet();

            @Override
            public @NotNull ByteIterator iterator() {
                return new ByteIterator() {
                    private final Iterator<ByteMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public byte nextByte() {
                        return it.next().getByteElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(byte k) {
                return AbstractByteMultiset.this.add(k);
            }

            @Override
            public boolean remove(byte value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull ByteCollection c) {
                return AbstractByteMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractByteMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractByteMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractByteMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractByteMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(byte value) {
                return AbstractByteMultiset.this.contains(value);
            }
        };
    }

    public static abstract class Entry extends AbstractMultiset.Entry<Byte> implements ByteMultiset.Entry,
            Byte2IntMap.Entry {
        @Override
        public int hashCode() {
            return Byte.hashCode(getByteElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getByteElement()) : getByteElement() + " x " + getCount();
        }

        @Override
        public final byte getByteKey() {
            return getByteElement();
        }
    }

    @Override
    public boolean containsAll(ByteCollection c) {
        return c.intParallelStream().allMatch(i -> contains((byte) i));
    }

    @Override
    public final byte[] toByteArray() {
        return toArray(ByteArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    public byte[] toArray(byte[] a) {
        if (a == null || a.length < size()) {
            return ByteIterators.unwrap(iterator());
        }

        var i = iterator();
        var unwrapped = 0;

        while ((unwrapped += ByteIterators.unwrap(i, a)) < size()) {
            a = ByteArrays.ensureCapacity(a, size());
        }

        return a;
    }

    @Override
    public boolean addAll(ByteCollection c) {
        boolean changed = false;

        for (byte b : c) {
            changed |= add(b);
        }

        return changed;
    }
}
