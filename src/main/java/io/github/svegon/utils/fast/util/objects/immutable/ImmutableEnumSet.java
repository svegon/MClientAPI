package io.github.svegon.utils.fast.util.objects.immutable;

import io.github.svegon.utils.collections.collecting.CollectingUtil;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public abstract class ImmutableEnumSet<E extends Enum<E>> extends ImmutableObjectSet<E> {
    ImmutableEnumSet(int hashCode) {
        super(hashCode);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> ImmutableEnumSet<E> of(final @NotNull Collection<E> elements) {
        if (elements.isEmpty()) {
            return EmptyImmutableEnumSet.INSTANCE;
        }

        Class<E> enumClass;

        try {
            enumClass = (Class<E>) elements.iterator().next().getClass();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException();
        }

        boolean[] flags = new boolean[enumClass.getEnumConstants().length];

        for (E element : elements) {
            flags[element.ordinal()] = true;
        }

        return new RegularImmutableEnumSet<>(enumClass, flags);
    }

    @SafeVarargs
    public static <E extends Enum<E>> ImmutableEnumSet<E> of(E @NotNull ... elements) {
        return of(Arrays.asList(elements));
    }

    private static <E extends Enum<E>> int hash(final @NotNull Class<E> enumClass, final boolean @NotNull [] flags) {
        E[] values = enumClass.getEnumConstants();
        int h = 0;

        for (int i = 0; i < flags.length; i++) {
            if (flags[i]) {
                h += values[i].hashCode();
            }
        }

        return h;
    }

    private static class RegularImmutableEnumSet<E extends Enum<E>> extends ImmutableEnumSet<E> {
        private final Class<E> enumClass;
        private final boolean[] flags;
        private final ImmutableObjectList<E> iterable;

        RegularImmutableEnumSet(final @NotNull Class<E> enumClass, final boolean @NotNull [] flags) {
            super(hash(enumClass, flags));

            this.enumClass = enumClass;
            this.flags = flags;
            this.iterable = Arrays.stream(enumClass.getEnumConstants()).filter((e) -> flags[e.ordinal()])
                    .collect(CollectingUtil.toImmutableObjectList());
        }

        @Override
        public @NotNull ObjectIterator<E> iterator() {
            return iterable.iterator();
        }

        @Override
        public int size() {
            return iterable.size();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            return enumClass.isInstance(o) && flags[((E) o).ordinal()];
        }
    }

    private static final class EmptyImmutableEnumSet<E extends Enum<E>> extends ImmutableEnumSet<E> {
        private static final EmptyImmutableEnumSet INSTANCE = new EmptyImmutableEnumSet();

        EmptyImmutableEnumSet() {
            super(0);
        }

        @Override
        @SuppressWarnings("unchecked")
        public @NotNull ObjectIterator<E> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(@Nullable Object element) {
            return false;
        }
    }
}
