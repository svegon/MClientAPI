package io.github.svegon.utils.fast.util.doubles;

import io.github.svegon.utils.collections.AbstractMultiset;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractDoubleMultiset extends AbstractMultiset<Double> implements DoubleMultiset {
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

        if (multiset instanceof DoubleMultiset doubleMultiset) {
            for (DoubleMultiset.Entry e : doubleMultiset.doubleEntrySet()) {
                if (e.getCount() != count(e.getDoubleElement())) {
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
    public double[] toDoubleArray() {
        return toArray((double[]) null);
    }

    @Override
    public double[] toArray(double[] a) {
        if (a == null) {
            a = new double[size()];
        } else if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        DoubleIterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            a[i] = it.nextDouble();
        }

        return a;
    }

    @Override
    public boolean addAll(DoubleCollection c) {
        boolean modified = false;

        for (double l : c) {
            modified |= add(l);
        }

        return modified;
    }

    @Override
    public boolean containsAll(DoubleCollection c) {
        return c.doubleParallelStream().allMatch(this::contains);
    }

    @Override
    public boolean add(double key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(double value) {
        int c = 0;
        DoubleIterator it = iterator();

        while (it.hasNext())
            if (it.nextDouble() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull DoubleSet elementSet() {
        return (DoubleSet) super.elementSet();
    }

    @Override
    public int add(double value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(double value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(double key) {
        for (double b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(double key) {
        DoubleIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextDouble() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(double value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(double value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<DoubleMultiset.Entry> doubleEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractDoubleMultiset.this.clear();
            }

            @Override
            public ObjectIterator<DoubleMultiset.Entry> iterator() {
                final var it = entriesFrame().double2IntEntrySet().iterator();
                return new AbstractObjectIterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public DoubleMultiset.Entry next() {
                        final var e = it.next();
                        return new Entry() {
                            @Override
                            public int setValue(int value) {
                                return setCount(getDoubleElement(), value);
                            }

                            @Override
                            public double getDoubleElement() {
                                return e.getDoubleKey();
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
                        && AbstractDoubleMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractDoubleMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Double2IntOpenHashMap entriesFrame() {
                Double2IntOpenHashMap map = new Double2IntOpenHashMap();

                for (double s : AbstractDoubleMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected DoubleSet initElementSet() {
        return new AbstractDoubleSet() {
            private final Set<DoubleMultiset.Entry> es = doubleEntrySet();

            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final Iterator<DoubleMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public double nextDouble() {
                        return it.next().getDoubleElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(double k) {
                return AbstractDoubleMultiset.this.add(k);
            }

            @Override
            public boolean remove(double value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull DoubleCollection c) {
                return AbstractDoubleMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractDoubleMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractDoubleMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractDoubleMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractDoubleMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(double value) {
                return AbstractDoubleMultiset.this.contains(value);
            }
        };
    }

    protected static abstract class Entry extends AbstractMultiset.Entry<Double> implements DoubleMultiset.Entry {
        @Override
        public int hashCode() {
            return Double.hashCode(getDoubleElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getDoubleElement()) : getDoubleElement() + " x " + getCount();
        }
    }
}
