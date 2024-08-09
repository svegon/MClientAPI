package io.github.svegon.utils.fast.util.longs;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.longs.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractLongMultiset extends AbstractMultiset<Long> implements LongMultiset {
    @Override
    @SuppressWarnings("unchecked")
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

        if (multiset instanceof LongMultiset longMultiset) {
            for (LongMultiset.Entry e : longMultiset.longEntrySet()) {
                if (e.getCount() != count(e.getLongElement())) {
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
    public long[] toLongArray() {
        return toArray((long[]) null);
    }

    @Override
    public long[] toArray(long[] a) {
        if (a == null) {
            a = new long[size()];
        } else if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        LongIterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            a[i] = it.nextLong();
        }

        return a;
    }

    @Override
    public boolean addAll(LongCollection c) {
        boolean modified = false;

        for (long l : c) {
            modified |= add(l);
        }

        return modified;
    }

    @Override
    public boolean containsAll(LongCollection c) {
        return c.longParallelStream().allMatch(this::contains);
    }

    @Override
    public boolean add(long key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(long value) {
        int c = 0;
        LongIterator it = iterator();

        while (it.hasNext())
            if (it.nextLong() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull LongSet elementSet() {
        return (LongSet) super.elementSet();
    }

    @Override
    public int add(long value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(long value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(long key) {
        for (long b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(long key) {
        LongIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextLong() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(long value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(long value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<LongMultiset.Entry> longEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractLongMultiset.this.clear();
            }

            @Override
            public ObjectIterator<LongMultiset.Entry> iterator() {
                final var it = entriesFrame().long2IntEntrySet().iterator();
                return new AbstractObjectIterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public LongMultiset.Entry next() {
                        final var e = it.next();
                        return new LongMultiset.Entry() {
                            @Override
                            public long getLongElement() {
                                return e.getLongKey();
                            }

                            @Override
                            public int getCount() {
                                return e.getIntValue();
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof AbstractMultiset.Entry<?> e
                        && AbstractLongMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractLongMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Long2IntOpenHashMap entriesFrame() {
                Long2IntOpenHashMap map = new Long2IntOpenHashMap();

                for (long s : AbstractLongMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected LongSet initElementSet() {
        return new AbstractLongSet() {
            private final Set<LongMultiset.Entry> es = longEntrySet();

            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final Iterator<LongMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public long nextLong() {
                        return it.next().getLongElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(long k) {
                return AbstractLongMultiset.this.add(k);
            }

            @Override
            public boolean remove(long value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull LongCollection c) {
                return AbstractLongMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractLongMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractLongMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractLongMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractLongMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(long value) {
                return AbstractLongMultiset.this.contains(value);
            }
        };
    }

    protected static abstract class Entry extends AbstractMultiset.Entry<Long> implements LongMultiset.Entry {
        @Override
        public int hashCode() {
            return Long.hashCode(getLongElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getLongElement()) : getLongElement() + " x " + getCount();
        }
    }
}
