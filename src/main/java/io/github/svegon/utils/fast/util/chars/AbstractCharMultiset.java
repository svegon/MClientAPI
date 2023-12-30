package io.github.svegon.utils.fast.util.chars;

import io.github.svegon.utils.collections.AbstractMultiset;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractCharMultiset extends AbstractMultiset<Character> implements CharMultiset {
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

        if (multiset instanceof CharMultiset charMultiset) {
            for (CharMultiset.Entry e : ((Iterable<CharMultiset.Entry>) (Object) multiset.entrySet())) {
                if (e.getCount() != count(e.getCharElement())) {
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
    public boolean add(char key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(char value) {
        int c = 0;
        CharIterator it = iterator();

        while (it.hasNext())
            if (it.nextChar() == value)
                c++;

        return c;
    }

    @Override
    public @NotNull CharSet elementSet() {
        return (CharSet) super.elementSet();
    }

    @Override
    public int add(char value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            add(value);
        }

        return c;
    }

    @Override
    public int remove(char value, int i) {
        Preconditions.checkArgument(i >= 0);
        int c = count(value);

        for (int j = 0; j < i; j++) {
            remove(value);
        }

        return c;
    }

    @Override
    public boolean contains(char key) {
        for (char b : this) {
            if (b == key) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean rem(char key) {
        CharIterator itr = iterator();

        while (itr.hasNext()){
            if (itr.nextChar() == key) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public int setCount(char value, int i) {
        Preconditions.checkArgument(i >= 0);

        int c = count(value) - i;

        if (c < 0) {
            return remove(value, -c);
        } else {
            return add(value, c);
        }
    }

    @Override
    public boolean setCount(char value, int prev, int count) {
        Preconditions.checkArgument(prev >= 0 && count >= 0);

        if (prev == count || count(value) != prev) {
            return false;
        }

        setCount(value, count);
        return true;
    }

    @Override
    public ObjectSet<CharMultiset.Entry> charEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public void clear() {
                AbstractCharMultiset.this.clear();
            }

            @Override
            public ObjectIterator<CharMultiset.Entry> iterator() {
                return IterationUtil.transformToObj(entriesFrame().char2IntEntrySet().iterator(),
                        e -> new CharMultiset.Entry() {
                    @Override
                    public char getCharElement() {
                        return e.getCharKey();
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
                        && AbstractCharMultiset.this.count(e.getElement()) == e.getCount();
            }

            @Override
            public boolean isEmpty() {
                return AbstractCharMultiset.this.isEmpty();
            }

            @Override
            public int size() {
                return entriesFrame().size();
            }

            private Char2IntOpenHashMap entriesFrame() {
                Char2IntOpenHashMap map = new Char2IntOpenHashMap();

                for (char s : AbstractCharMultiset.this) {
                    map.addTo(s, 1);
                }

                return map;
            }
        };
    }

    @Override
    protected CharSet initElementSet() {
        return new AbstractCharSet() {
            private final Set<CharMultiset.Entry> es = charEntrySet();

            @Override
            public CharIterator iterator() {
                return new CharIterator() {
                    private final Iterator<CharMultiset.Entry> it = es.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public char nextChar() {
                        return it.next().getCharElement();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean add(char k) {
                return AbstractCharMultiset.this.add(k);
            }

            @Override
            public boolean remove(char value) {
                return setCount(value, 0) != 0;
            }

            @Override
            public boolean addAll(@NotNull CharCollection c) {
                return AbstractCharMultiset.this.addAll(c);
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return AbstractCharMultiset.this.retainAll(c);
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return AbstractCharMultiset.this.removeAll(c);
            }

            @Override
            public void clear() {
                AbstractCharMultiset.this.clear();
            }

            @Override
            public int size() {
                return es.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractCharMultiset.this.isEmpty();
            }

            @Override
            public boolean contains(char value) {
                return AbstractCharMultiset.this.contains(value);
            }
        };
    }

    public static abstract class Entry extends AbstractMultiset.Entry<Character> implements CharMultiset.Entry,
            Char2IntMap.Entry {
        @Override
        public final int hashCode() {
            return Character.hashCode(getCharElement()) ^ getCount();
        }

        @Override
        public @NotNull String toString() {
            return getCount() == 1 ? String.valueOf(getCharElement()) : getCharElement() + " x " + getCount();
        }

        @Override
        public final char getCharKey() {
            return getCharElement();
        }
    }
}
