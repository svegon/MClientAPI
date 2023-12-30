package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;

public final class C2LTransformingIterator<E> extends TransformingObjectIterator<Character, CharIterator, E> {
    private final Char2ObjectFunction<? extends E> transformer;

    public C2LTransformingIterator(CharIterator itr, Char2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextChar());
    }
}
