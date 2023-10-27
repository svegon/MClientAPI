package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectBidirectionalIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;

public class C2LTransformingBidirectionalIterator<E>
        extends TransformingObjectBidirectionalIterator<Character, CharBidirectionalIterator, E> {
    private final Char2ObjectFunction<? extends E> transformer;

    public C2LTransformingBidirectionalIterator(CharBidirectionalIterator itr,
                                                Char2ObjectFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.get(itr.nextChar());
    }

    @Override
    public E previous() {
        return transformer.get(itr.previousChar());
    }
}
