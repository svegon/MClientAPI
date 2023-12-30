package io.github.svegon.utils.fast.util.floats;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.floats.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractFloatMultiset extends AbstractMultiset<Float> implements FloatMultiset {
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

        if (multiset instanceof FloatMultiset floatMultiset) {
            for (FloatMultiset.Entry e : floatMultiset.floatEntrySet()) {
                if (e.getCount() != count(e.getFloatElement())) {
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
    public boolean add(float key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(float value) {
        int c = 0;
        FloatIterator it = iterator();

        while (it.hasNext())
            if (it.nextFloat() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull FloatSet elementSet() {
        return (FloatSet) super.elementSet();
    }

    @Override
    public int add(float value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(float value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(float key) {
        for (float b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(float key) {
        FloatIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextFloat() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(float value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(float value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<FloatMultiset.Entry> floatEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractFloatMultiset.this.clear();
            }

            @Override
            public ObjectIterator<FloatMultiset.Entry> iterator() {
                return IterationUtil.transformToObj(entriesFrame().float2IntEntrySet().iterator(),
                        e -> new Entry() {
                            @Override
                            public int setValue(int value) {
                                return setCount(getFloatElement(), value);
                            }

                            @Override
                    public float getFloatElement() {
                        return e.getFloatKey();
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
                        && AbstractFloatMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractFloatMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Float2IntOpenHashMap entriesFrame() {
                Float2IntOpenHashMap map = new Float2IntOpenHashMap();

                for (float s : AbstractFloatMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected FloatSet initElementSet() {
        return new AbstractFloatSet() {
            private final Set<FloatMultiset.Entry> es = floatEntrySet();

            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final Iterator<FloatMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public float nextFloat() {
                        return it.next().getFloatElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(float k) {
                return AbstractFloatMultiset.this.add(k);
            }

            @Override
            public boolean remove(float value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull FloatCollection c) {
                return AbstractFloatMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractFloatMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractFloatMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractFloatMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractFloatMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(float value) {
                return AbstractFloatMultiset.this.contains(value);
            }
        };
    }

    protected static abstract class Entry extends AbstractMultiset.Entry<Float> implements FloatMultiset.Entry {
        @Override
        public int hashCode() {
            return Float.hashCode(getFloatElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getFloatElement()) : getFloatElement() + " x " + getCount();
        }
    }
}
