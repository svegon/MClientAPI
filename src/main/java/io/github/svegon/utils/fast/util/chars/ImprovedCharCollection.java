package io.github.svegon.utils.fast.util.chars;

import io.github.svegon.utils.collections.CollectionUtil;
import io.github.svegon.utils.collections.stream.CharStream;
import io.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * @since 1.0.0
 */
public interface ImprovedCharCollection extends CharCollection {
    @Override
    default char[] toCharArray() {
        return toArray((char[]) null);
    }

    @Override
    default char[] toArray(char[] a) {
        if (a == null || a.length < size()) {
            a = new char[size()];
        }

        CharIterator it = iterator();

        try {
            for (int i = 0; it.hasNext(); i++) {
                a[i] = it.nextChar();
            }
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            throw new ConcurrentModificationException(e);
        }

        return a;
    }

    @Override
    default boolean addAll(final @NotNull CharCollection c) {
        boolean modified = false;

        for (char chr : c) {
            modified |= add(chr);
        }

        return modified;
    }

    @Override
    default boolean containsAll(final @NotNull CharCollection c) {
        return StreamUtil.charStream(c.spliterator(), true).allMatch(this::contains);
    }

    @Override
    default boolean removeAll(final @NotNull CharCollection c) {
        return removeIf(c::contains);
    }

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default boolean addAll(@NotNull Collection<? extends Character> c) {
        return addAll(c instanceof CharCollection ? (CharCollection) c
                : CollectionUtil.transformToChar(c, Character::charValue));
    }

    @Override
    default boolean retainAll(final @NotNull CharCollection c) {
        return removeIf((s) -> !c.contains(s));
    }

    @Deprecated
    @Override
    default boolean removeAll(final @NotNull Collection<?> collection) {
        return collection instanceof CharCollection ? removeAll((CharCollection) collection)
                : removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(final @NotNull Collection<?> collection) {
        return collection instanceof CharCollection ? retainAll((CharCollection) collection)
                : removeIf((bl) -> !collection.contains(bl));
    }

    @Deprecated
    @Override
    default boolean containsAll(@NotNull Collection<?> c) {
        if (c instanceof CharCollection) {
            return containsAll((CharCollection) c);
        }

        for (Object o : c) {
            if (!(o instanceof Character) || !contains((char) o)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return a fast stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see Collection#stream()
     * @see CharStream
     */
    default CharStream charStream() {
        return StreamUtil.charStream(spliterator(), false);
    }

    /**
     * Return a fast parallel stream over the elements, performing widening casts if
     * needed.
     *
     * @return a fast stream over the elements.
     * @see Collection#stream()
     * @see CharStream
     */
    default CharStream parallelCharStream() {
        return StreamUtil.charStream(spliterator(), true);
    }
}
