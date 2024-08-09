package io.github.svegon.utils.fast.util.shorts;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractShortMultiset extends AbstractMultiset<Short> implements ShortMultiset {
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

        if (multiset instanceof ShortMultiset shortMultiset) {
            for (ShortMultiset.Entry e : shortMultiset.shortEntrySet()) {
                if (e.getCount() != count(e.getShortElement())) {
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
    public abstract ShortIterator iterator();

    @Override
    public boolean add(short key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(short value) {
        int c = 0;
        ShortIterator it = iterator();

        while (it.hasNext())
            if (it.nextShort() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull ShortSet elementSet() {
        return (ShortSet) super.elementSet();
    }

    @Override
    public int add(short value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(short value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(short key) {
        for (short b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(short key) {
        ShortIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextShort() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(short value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(short value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractShortMultiset.this.clear();
            }

            @Override
            public ObjectIterator<ShortMultiset.Entry> iterator() {
                final var it = entriesFrame().short2IntEntrySet().iterator();
                return new AbstractObjectIterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public ShortMultiset.Entry next() {
                        final var e = it.next();
                        return new Entry() {
                            @Override
                            public int setValue(int value) {
                                return setCount(getShortElement(), value);
                            }

                            @Override
                            public short getShortElement() {
                                return e.getShortKey();
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
                        && AbstractShortMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractShortMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Short2IntOpenHashMap entriesFrame() {
                Short2IntOpenHashMap map = new Short2IntOpenHashMap();

                for (short s : AbstractShortMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected ShortSet initElementSet() {
        return new AbstractShortSet() {
            private final Set<ShortMultiset.Entry> es = shortEntrySet();

            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final Iterator<ShortMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public short nextShort() {
                        return it.next().getShortElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(short k) {
                return AbstractShortMultiset.this.add(k);
            }

            @Override
            public boolean remove(short value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull ShortCollection c) {
                return AbstractShortMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractShortMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractShortMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractShortMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractShortMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(short value) {
                return AbstractShortMultiset.this.contains(value);
            }
        };
    }

    @Override
    public short[] toShortArray() {
        return toArray(ShortArrays.DEFAULT_EMPTY_ARRAY);
    }

    @Override
    public short[] toArray(short[] a) {
        if (a == null || a.length < size()) {
            return ShortIterators.unwrap(iterator());
        }

        var i = iterator();
        var unwrapped = 0;

        while ((unwrapped += ShortIterators.unwrap(i, a)) < size()) {
            a = ShortArrays.ensureCapacity(a, size());
        }

        return a;
    }

    @Override
    public boolean addAll(ShortCollection c) {
        boolean changed = false;

        for (short s : c) {
            changed |= add(s);
        }

        return changed;
    }

    @Override
    public boolean containsAll(ShortCollection c) {
        return c.intParallelStream().allMatch(i -> contains((short) i));
    }

    public static abstract class Entry extends AbstractMultiset.Entry<Short> implements ShortMultiset.Entry,
            Short2IntMap.Entry {
        @Override
        public final int hashCode() {
            return Short.hashCode(getShortElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getShortElement()) : getShortElement() + " x " + getCount();
        }

        @Override
        public final short getShortKey() {
            return getShortElement();
        }
    }
}
