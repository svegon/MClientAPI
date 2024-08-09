package io.github.svegon.utils.collections;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    private final Set<E> elementSet = initElementSet();

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

        for (Multiset.Entry<?> e : multiset.entrySet()) {
            if (e.getCount() != count(e.getElement())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override
    public int count(@Nullable Object o) {
        int c = 0;
        Iterator<E> it = iterator();

        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    c++;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    c++;
        }

        return c;
    }

    @Override
    public int add(@Nullable E e, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(e);

        for (int j = 0; j < i; j++) {
            add(e);
        }

        return c;
    }

    @Override
    public int remove(@Nullable Object o, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(o);

        for (int j = 0; j < i; j++) {
            remove(o);
        }

        return c;
    }

    @Override
    public int setCount(@NotNull E e, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(e) - i;

        if (c < 0) {
            return remove(e, -c);
        } else {
            return add(e, c);
        }
    }

    @Override
    public boolean setCount(E e, int i, int i1) {
        Preconditions.checkArgument(i >= 0 && i1 >= 0);

        if (i == i1 || count(e) != i) {
            return false;
        }

        setCount(e, i1);
        return true;
    }

    @Override
    public @NotNull Set<Multiset.Entry<E>> entrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractMultiset.this.clear();
            }

            @Override
            public ObjectIterator<Multiset.Entry<E>> iterator() {
                final var it = frameEntries().object2IntEntrySet().iterator();
                return new AbstractObjectIterator<>() {

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Multiset.Entry<E> next() {
                        final var e = it.next();
                        return new Multiset.Entry<E>() {
                            @Override
                            public E getElement() {
                                return e.getKey();
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
                return o instanceof Entry<?> e && AbstractMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return frameEntries().size();
            }

            private Object2IntOpenHashMap<E> frameEntries() {
                Object2IntOpenHashMap<E> map = new Object2IntOpenHashMap<>(AbstractMultiset.this.size() - 1);

                for (E o : AbstractMultiset.this) {
                    map.addTo(o, 1);
                }

                return map;
            }
        };
    }

    @Override
    public @NotNull Set<E> elementSet() {
        return elementSet;
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

    protected Set<E> initElementSet() {
        return new AbstractObjectSet<>() {
            private final Set<Multiset.Entry<E>> es = entrySet();

            @Override
            public ObjectIterator<E> iterator() {
                return new ObjectIterator<>() {
                    private final Iterator<Multiset.Entry<E>> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public E next() {
                        return it.next().getElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(E e) {
                return AbstractMultiset.this.add(e);
            }

            @Override
            public boolean remove(Object o) {
                try {
                    return setCount((E) o, 0) != 0;
                } catch (ClassCastException ignore) {
                    return false;
                }
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends E> c) {
                return AbstractMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return AbstractMultiset.this.contains(o);
            }
        };
    }

    public static abstract class Entry<E> implements Multiset.Entry<E>, Object2IntMap.Entry<E> {
        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Multiset.Entry e && Objects.equals(e.getElement(), getElement())
                    && getCount() == e.getCount()) || (obj instanceof Map.Entry entry
                    && entry.getValue() instanceof Integer && Objects.equals(getKey(), entry.getKey())
                    && ((Integer) entry.getValue()) == getCount());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getElement()) : getElement() + " x " + getCount();
        }

        @Override
        public final E getKey() {
            return getElement();
        }

        @Override
        public final int getIntValue() {
            return getCount();
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public final Integer getValue() {
            return Object2IntMap.Entry.super.getValue();
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public final Integer setValue(Integer value) {
            return Object2IntMap.Entry.super.setValue(value);
        }
    }
}
