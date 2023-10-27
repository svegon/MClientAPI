package io.github.svegon.utils.fast.util.chars;

import it.unimi.dsi.fastutil.chars.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

public abstract class ImprovedCharList extends AbstractCharList implements ImprovedCharCollection, CharSequence {
    @Override
    public ImprovedCharList subList(int from, int to) {
        return new SubList(this, from, to);
    }

    @Override
    public final int length() {
        return size();
    }

    @Override
    public final char charAt(int index) {
        return getChar(index);
    }

    @NotNull
    @Override
    public final CharSequence subSequence(int start, int end) {
        return subList(start, end);
    }

    @NotNull
    @Override
    public String toString() {
        return new String(new StringBuilder(size()).append(this));
    }

    protected static class SubList extends ImprovedCharList {
        /** The list this sublist restricts. */
        protected final ImprovedCharList l;
        /** Initial (inclusive) index of this sublist. */
        protected final int from;
        /** Final (exclusive) index of this sublist. */
        protected int to;

        public SubList(final ImprovedCharList l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }

        private boolean assertRange() {
            assert from <= l.size();
            assert to <= l.size();
            assert to >= from;
            return true;
        }

        @Override
        public boolean add(final char k) {
            l.add(to, k);
            to++;
            assert assertRange();
            return true;
        }

        @Override
        public void add(final int index, final char k) {
            ensureIndex(index);
            l.add(from + index, k);
            to++;
            assert assertRange();
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends Character> c) {
            ensureIndex(index);
            to += c.size();
            return l.addAll(from + index, c);
        }

        @Override
        public char getChar(final int index) {
            ensureRestrictedIndex(index);
            return l.getChar(from + index);
        }

        @Override
        public @NotNull String toString() {
            return new String(toCharArray());
        }

        @Override
        public char removeChar(final int index) {
            ensureRestrictedIndex(index);
            to--;
            return l.removeChar(from + index);
        }

        @Override
        public char set(final int index, final char k) {
            ensureRestrictedIndex(index);
            return l.set(from + index, k);
        }

        @Override
        public int size() {
            return to - from;
        }

        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
            ensureIndex(from);
            if (from + length > size())
                throw new IndexOutOfBoundsException(
                        "End index (" + from + length + ") is greater than list size (" + size() + ")");
            l.getElements(this.from + from, a, offset, length);
        }

        @Override
        public void removeElements(final int from, final int to) {
            ensureIndex(from);
            ensureIndex(to);
            l.removeElements(this.from + from, this.from + to);
            this.to -= (to - from);
            assert assertRange();
        }

        @Override
        public void addElements(int index, final char a[], int offset, int length) {
            ensureIndex(index);
            l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert assertRange();
        }

        @Override
        public void setElements(int index, final char a[], int offset, int length) {
            ensureIndex(index);
            l.setElements(this.from + index, a, offset, length);
            assert assertRange();
        }

        private final class RandomAccessIter extends CharIterators.AbstractIndexBasedListIterator {
            // We don't set the minPos to be "from" because we need to call our containing
            // class'
            // add, set, and remove methods with 0 relative to the start of the sublist, not
            // the
            // start of the original list.
            // Thus pos is relative to the start of the SubList, not the start of the
            // original list.
            RandomAccessIter(int pos) {
                super(0, pos);
            }
            @Override
            protected final char get(int i) {
                return l.getChar(from + i);
            }
            // Remember, these are calling SUBLIST's methods, meaning 0 is the start of the
            // sublist for these.
            @Override
            protected final void add(int i, char k) {
                SubList.this.add(i, k);
            }
            @Override
            protected final void set(int i, char k) {
                SubList.this.set(i, k);
            }
            @Override
            protected final void remove(int i) {
                SubList.this.removeChar(i);
            }
            @Override
            protected final int getMaxPos() {
                return to - from;
            }
            @Override
            public void add(char k) {
                super.add(k);
                assert assertRange();
            }
            @Override
            public void remove() {
                super.remove();
                assert assertRange();
            }
        }
        private class ParentWrappingIter implements CharListIterator {
            private CharListIterator parent;
            ParentWrappingIter(CharListIterator parent) {
                this.parent = parent;
            }
            @Override
            public int nextIndex() {
                return parent.nextIndex() - from;
            }
            @Override
            public int previousIndex() {
                return parent.previousIndex() - from;
            }
            @Override
            public boolean hasNext() {
                return parent.nextIndex() < to;
            }
            @Override
            public boolean hasPrevious() {
                return parent.previousIndex() >= from;
            }
            @Override
            public char nextChar() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return parent.nextChar();
            }
            @Override
            public char previousChar() {
                if (!hasPrevious())
                    throw new NoSuchElementException();
                return parent.previousChar();
            }
            @Override
            public void add(char k) {
                parent.add(k);
            }
            @Override
            public void set(char k) {
                parent.set(k);
            }
            @Override
            public void remove() {
                parent.remove();
            }
            @Override
            public int back(int n) {
                if (n < 0)
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                int currentPos = parent.previousIndex();
                int parentNewPos = currentPos - n;
                // Remember, the minimum acceptable previousIndex is not from but (from - 1),
                // since (from - 1)
                // means this subList is at the beginning of our sub range.
                // Same reason why previousIndex()'s minimum for the full list is not 0 but -1.
                if (parentNewPos < (from - 1))
                    parentNewPos = (from - 1);
                int toSkip = parentNewPos - currentPos;
                return parent.back(toSkip);
            }
            @Override
            public int skip(int n) {
                if (n < 0)
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                int currentPos = parent.nextIndex();
                int parentNewPos = currentPos + n;
                if (parentNewPos > to)
                    parentNewPos = to;
                int toSkip = parentNewPos - currentPos;
                return parent.skip(toSkip);
            }
        }
        @Override
        public CharListIterator listIterator(final int index) {
            ensureIndex(index);
            // If this class wasn't public, then RandomAccessIter would live in
            // SUBLISTRandomAccess,
            // and the switching would be done in sublist(int, int). However, this is a
            // public class
            // that may have existing implementors, so to get the benefit of
            // RandomAccessIter class for
            // for existing uses, it has to be done in this class.
            return l instanceof java.util.RandomAccess
                    ? new RandomAccessIter(index)
                    : new ParentWrappingIter(l.listIterator(index + from));
        }

        @Override
        public CharSpliterator spliterator() {
            return l instanceof java.util.RandomAccess ? new IndexBasedSpliterator(l, from, to) : super.spliterator();
        }

        @Override
        public ImprovedCharList subList(final int from, final int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to)
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            // Sadly we have to rewrap this, because if there is a sublist of a sublist, and
            // the
            // subsublist adds, both sublists need to update their "to" value.
            return new SubList(this, from, to);
        }

        @Override
        public boolean rem(final char k) {
            int index = indexOf(k);
            if (index == -1)
                return false;
            to--;
            l.removeChar(from + index);
            assert assertRange();
            return true;
        }

        @Override
        public boolean addAll(final int index, final CharCollection c) {
            ensureIndex(index);
            return super.addAll(index, c);
        }

        @Override
        public boolean addAll(final int index, final CharList l) {
            ensureIndex(index);
            return super.addAll(index, l);
        }
    }

    protected static class IndexBasedSpliterator extends CharSpliterators.AbstractIndexBasedSpliterator {
        private final ImprovedCharList l;
        private final int to;

        protected IndexBasedSpliterator(final ImprovedCharList l, final int initialPos, final int endingPos) {
            super(initialPos);
            this.l = l;
            this.to = endingPos;
        }

        @Override
        protected char get(int location) {
            return l.getChar(location);
        }

        @Override
        protected int getMaxPos() {
            return to;
        }

        @Override
        protected IndexBasedSpliterator makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator(l, pos, maxPos);
        }
    }
}
