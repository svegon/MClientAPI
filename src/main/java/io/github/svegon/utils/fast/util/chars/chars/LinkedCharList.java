package io.github.svegon.utils.fast.util.chars.chars;

import io.github.svegon.utils.fast.util.chars.ImprovedCharList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.ConcurrentModificationException;

@NotThreadSafe
public class LinkedCharList extends ImprovedCharList {
    private Node first;
    private Node last;
    private int size;

    @Override
    public char getChar(int index) {
        return listIterator(index).nextChar();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public CharListIterator listIterator() {
        return new Itr(first);
    }

    @Override
    public CharListIterator listIterator(int index) {
        Preconditions.checkElementIndex(index, size());
        Itr itr;

        if (index < size >>> 1) {
            itr = new Itr(first);
            itr.skip(index);
        } else {
            itr = new Itr(new Node(last));

            while (itr.index > index) {
                itr.previousChar();
            }
        }

        return itr;
    }

    @Override
    public void add(int index, char k) {
        Preconditions.checkPositionIndex(index, size());

        if (first == null || last == null) {
            if (first != last) {
                throw new ConcurrentModificationException();
            }

            if (index == 0) {
                first = last = new Node(k);
                return;
            }
        }

        listIterator(index).add(k);
    }

    @Override
    public char removeChar(int i) {
        CharListIterator itr = listIterator(i);
        char ret = itr.nextChar();

        itr.remove();

        return ret;
    }

    @Override
    public char set(int index, char k) {
        CharListIterator itr = listIterator(index);
        char ret = itr.nextChar();

        itr.set(k);

        return ret;
    }

    @Override
    public void removeElements(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, size);

        Node start = ((Itr) listIterator(from)).node;
        Node end = ((Itr) listIterator(to)).node;

        if (start.next == first) {
            first = end.previous;
        } else {
            start.next.previous = null;
        }

        if (end.previous == last) {
            last = start;
        }

        end.previous = null;
    }

    @Override
    public boolean rem(char k) {
        CharListIterator itr = listIterator();

        while (itr.hasNext()) {
            if (itr.nextChar() == k) {
                itr.remove();
                return true;
            }
        }

        return false;
    }

    private static final class Node {
        private char value;
        private Node previous;
        private Node next;

        Node(char value) {
            this.value = value;
        }

        Node(Node previous) {
            this.previous = previous;
        }
    }

    private final class Itr implements CharListIterator {
        private Node node;
        private int index;

        Itr(Node node, int index) {
            this.node = node;
            this.index = index;
        }

        Itr(Node node) {
            this.node = node;
        }

        @Override
        public char previousChar() {
            char ret = (node = node.previous).value;
            index--;
            return ret;
        }

        @Override
        public boolean hasPrevious() {
            return node != null && node.previous != null;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public char nextChar() {
            try {
                char ret = node.value;
                node = node.next;
                index++;
                return ret;
            } catch (NullPointerException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        public void set(final char k) {
            try {
                node.previous.value = k;
            } catch (NullPointerException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        public void add(final char k) {
            try {
                node.previous.next = new Node(k);
                node.previous = node.previous.next;
                size++;
            } catch (NullPointerException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public void remove() {
            try {
                if (node.previous == first) {
                    first = node;
                }

                node.previous = node.previous.previous;
                size--;

                if (node.previous != null) {
                    node.previous.next = node;
                }
            } catch (NullPointerException e) {
                throw new ConcurrentModificationException(e);
            }
        }
    }
}
