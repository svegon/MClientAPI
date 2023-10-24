package com.github.svegon.utils.fast.util.doubles.transform.chars;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;

public class C2DTransformingList extends TransformingDoubleSequentialList<Character, CharList> {
    private final Char2DoubleFunction transformer;

    public C2DTransformingList(CharList list, Char2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull DoubleListIterator listIterator(int index) {
        return new C2DTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public double removeDouble(int index) {
        return transformer.get(list.removeChar(index));
    }
}
