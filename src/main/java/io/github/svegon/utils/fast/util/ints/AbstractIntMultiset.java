package io.github.svegon.utils.fast.util.ints;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractIntMultiset extends AbstractMultiset<Integer> implements IntMultiset {
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

        if (multiset instanceof IntMultiset intMultiset) {
            for (IntMultiset.Entry e : ((Iterable<IntMultiset.Entry>) (Object) multiset.entrySet())) {
                if (e.getCount() != count(e.getIntElement())) {
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
    public boolean add(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(int value) {
        int c = 0;
        IntIterator it = iterator();

        while (it.hasNext())
            if (it.nextInt() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull IntSet elementSet() {
        return (IntSet) super.elementSet();
    }

    @Override
    public int add(int value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(int value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(int key) {
        for (int b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(int key) {
        IntIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextInt() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(int value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(int value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<IntMultiset.Entry> intEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractIntMultiset.this.clear();
            }

            @Override
            public ObjectIterator<IntMultiset.Entry> iterator() {
                return IterationUtil.transformToObj(entriesFrame().int2IntEntrySet().iterator(),
                        e -> new Entry() {
                            @Override
                            public int setValue(int value) {
                                return setCount(getIntElement(), value);
                            }

                            @Override
                    public int getIntElement() {
                        return e.getIntKey();
                    }

                    @Override
                    public int getCount() {
                        return e.getIntValue();
                    }
                });
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof AbstractMultiset.Entry<?> e
                        && AbstractIntMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractIntMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Int2IntOpenHashMap entriesFrame() {
                Int2IntOpenHashMap map = new Int2IntOpenHashMap();

                for (int s : AbstractIntMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected IntSet initElementSet() {
        return new AbstractIntSet() {
            private final Set<IntMultiset.Entry> es = intEntrySet();

            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final Iterator<IntMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public int nextInt() {
                        return it.next().getIntElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(int k) {
                return AbstractIntMultiset.this.add(k);
            }

            @Override
            public boolean remove(int value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull IntCollection c) {
                return AbstractIntMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractIntMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractIntMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractIntMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractIntMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(int value) {
                return AbstractIntMultiset.this.contains(value);
            }
        };
    }

    public static abstract class Entry extends AbstractMultiset.Entry<Integer> implements IntMultiset.Entry,
            Int2IntMap.Entry {
        @Override
        public final int hashCode() {
            return Integer.hashCode(getIntElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getIntElement()) : getIntElement() + " x " + getCount();
        }

        @Override
        public final int getIntKey() {
            return getIntElement();
        }
    }
}
