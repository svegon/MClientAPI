package com.github.svegon.utils.fast.util.objects.transform.chars;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObjectListIterator;
import com.github.svegon.utils.interfaces.function.IntCharBiFunction;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharListIterator;

public class C2LTransformingListIterator<E>
        extends TransformingObjectListIterator<Character, CharListIterator, E> {
    private final IntCharBiFunction<? extends E> transformer;

    public C2LTransformingListIterator(CharListIterator itr, IntCharBiFunction<? extends E> transformer) {
        super(itr);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E next() {
        return transformer.apply(itr.nextIndex(), itr.nextChar());
    }

    @Override
    public E previous() {
        return transformer.apply(itr.previousIndex(), itr.previousChar());
    }
}
