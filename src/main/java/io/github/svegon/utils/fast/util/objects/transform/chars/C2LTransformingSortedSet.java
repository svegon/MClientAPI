package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSortedSet;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public class C2LTransformingSortedSet<E> extends TransformingObjectSortedSet<Character, CharSortedSet, E> {
    private final Char2ObjectFunction<? extends E> forwardingTransformer;
    private final Object2CharFunction<? super E> backingTransformer;

    public C2LTransformingSortedSet(final CharSortedSet set,
                                    final Char2ObjectFunction<? extends E> forwardingTransformer,
                                    final Object2CharFunction<? super E> backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(E fromElement) {
        return IterationUtil.mapToObj(set.iterator(backingTransformer.applyAsChar(fromElement)),
                forwardingTransformer);
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator() {
        return IterationUtil.mapToObj(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        try {
            return set.contains(backingTransformer.applyAsChar((E) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable Comparator<? super E> comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.applyAsChar(o1),
                backingTransformer.applyAsChar(o2)) : Comparator.comparingInt(backingTransformer::applyAsChar);
    }

    @Override
    public @NotNull ObjectSortedSet<E> subSet(E fromElement, E toElement) {
        return SetUtil.mapToObj(set.subSet(backingTransformer.applyAsChar(fromElement),
                backingTransformer.applyAsChar(toElement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> headSet(E toElement) {
        return SetUtil.mapToObj(set.headSet(backingTransformer.applyAsChar(toElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ObjectSortedSet<E> tailSet(E fromElement) {
        return SetUtil.mapToObj(set.tailSet(backingTransformer.applyAsChar(fromElement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public E last() {
        return forwardingTransformer.get(set.lastChar());
    }
}
