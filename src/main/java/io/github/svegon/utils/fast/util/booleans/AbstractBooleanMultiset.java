package io.github.svegon.utils.fast.util.booleans;

import io.github.svegon.utils.collections.AbstractMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractBooleanMultiset extends AbstractMultiset<Boolean> implements BooleanMultiset {
    @SuppressWarnings("unchecked")
    private final Set<BooleanMultiset.Entry> entrySet = (Set<BooleanMultiset.Entry>) (Object) entrySet();

    @Override
    public int count(boolean e) {
        int c = 0;

        for (boolean bl : this) {
            if (bl == e) {
                c++;
            }
        }

        return c;
    }

    @Override
    public boolean add(boolean key) {
        return add(key, 1) > 0;
    }

    @Override
    public boolean contains(final boolean key) {
        return parallelStream().anyMatch((bl) -> bl == key);
    }

    @Override
    public boolean rem(boolean key) {
        return remove(key, 1) > 0;
    }

    @Override
    public int add(boolean bl, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int remove(boolean bl, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int setCount(boolean aBoolean, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setCount(boolean e, int i, int i1) {
        Preconditions.checkArgument(i >= 0 && i1 >= 0);

        if (i == i1 || count(e) != i) {
            return false;
        }

        setCount(e, i1);
        return true;
    }

    @Override
    public Set<BooleanMultiset.Entry> booleanEntrySet() {
        return entrySet;
    }

    @Override
    public @NotNull BooleanSet elementSet() {
        return (BooleanSet) super.elementSet();
    }

    @Override
    public boolean addAll(BooleanCollection c) {
        boolean modified = false;

        for (boolean bl : c) {
            modified |= add(bl);
        }

        return modified;
    }

    @Override
    protected BooleanSet initElementSet() {
        return new AbstractBooleanSet() {
            private final Set<BooleanMultiset.Entry> es = booleanEntrySet();

            @Override
            public BooleanIterator iterator() {
                return new BooleanIterator() {
                    private final Iterator<BooleanMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public boolean nextBoolean() {
                        return it.next().getBooleanElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(boolean bl) {
                return AbstractBooleanMultiset.this.add(bl);
            }

            @Override
            public boolean rem(boolean o) {
                try {
                    return setCount(o, 0) != 0;
                } catch (ClassCastException ignore) {
                    return false;
                }
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends Boolean> c) {
                return AbstractBooleanMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull BooleanCollection c) {
                return AbstractBooleanMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull BooleanCollection c) {
                return AbstractBooleanMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractBooleanMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractBooleanMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(boolean o) {
                return AbstractBooleanMultiset.this.contains(o);
            }
        };
    }

    public static abstract class Entry extends AbstractMultiset.Entry<Boolean> implements BooleanMultiset.Entry {
        @Override
        public int hashCode() {
            return Boolean.hashCode(getBooleanElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getBooleanElement()) : getBooleanElement() + " x " + getCount();
        }
    }
}
