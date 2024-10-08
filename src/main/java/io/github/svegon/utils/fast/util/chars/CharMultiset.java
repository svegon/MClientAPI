package io.github.svegon.utils.fast.util.chars;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.chars.CharSpliterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface CharMultiset extends Multiset<Character>, CharCollection {
    @Deprecated
    @Override
    default boolean removeAll(@NotNull Collection<?> collection) {
        return removeIf(collection::contains);
    }

    @Deprecated
    @Override
    default boolean retainAll(@NotNull Collection<?> collection) {
        return removeIf(c -> !collection.contains(c));
    }

    @Deprecated
    @Override
    default boolean containsAll(@NotNull Collection<?> collection) {
        return collection.parallelStream().allMatch(this::contains);
    }

    @Deprecated
    @Override
    default int count(Object o) {
        return o instanceof Character ? count((char) o) : 0;
    }

    int count(char value);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default void forEach(@NotNull Consumer<? super Character> action) {
        CharCollection.super.forEach(action);
    }

    @Deprecated
    @Override
    default int add(@Nullable Character aChar, int i) {
        return add(aChar.charValue(), i);
    }

    int add(char value, int i);

    @Override
    default @NotNull CharSpliterator spliterator() {
        return CharCollection.super.spliterator();
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean add(Character aChar) {
        return CharCollection.super.add(aChar);
    }

    @Deprecated
    @Override
    default int remove(@Nullable Object o, int i) {
        return o instanceof Character ? remove(((char) o), i) : 0;
    }

    int remove(char value, int i);

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean remove(@Nullable Object o) {
        return CharCollection.super.remove(o);
    }

    @Deprecated
    @Override
    default int setCount(@NotNull Character aChar, int i) {
        return setCount(aChar.charValue(), i);
    }

    int setCount(char value, int i);

    @Override
    default boolean setCount(Character aChar, int prev, int count) {
        return setCount(aChar.charValue(), prev, count);
    }

    boolean setCount(char value, int prev, int count);

    @Override
    @NotNull
    CharSet elementSet();

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull Set<Multiset.Entry<Character>> entrySet() {
        return (Set<Multiset.Entry<Character>>) (Object) charEntrySet();
    }

    Set<Entry> charEntrySet();

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    default boolean contains(@Nullable Object o) {
        return CharCollection.super.contains(o);
    }

    interface Entry extends Multiset.Entry<Character> {
        @Deprecated
        @Override
        default Character getElement() {
            return getCharElement();
        }

        char getCharElement();
    }
}
