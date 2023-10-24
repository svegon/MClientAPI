package com.github.svegon.utils.fast.util.floats.transform.chars;

import com.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import org.jetbrains.annotations.NotNull;

public class C2FTransformingList extends TransformingFloatSequentialList<Character, CharList> {
    private final Char2FloatFunction transformer;

    public C2FTransformingList(CharList list, Char2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new C2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.get(list.removeChar(index));
    }
}
