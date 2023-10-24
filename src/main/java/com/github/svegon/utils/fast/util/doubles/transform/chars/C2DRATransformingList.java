package com.github.svegon.utils.fast.util.doubles.transform.chars;

import com.github.svegon.utils.fast.util.doubles.transform.TransformingDoubleRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.CharList;

import java.util.function.DoublePredicate;

public class C2DRATransformingList extends TransformingDoubleRandomAccessList<Character, CharList> {
    private final Char2DoubleFunction transformer;

    public C2DRATransformingList(CharList list, Char2DoubleFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public double getDouble(int index) {
        return transformer.get(list.getChar(index));
    }

    @Override
    public boolean removeIf(final DoublePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
