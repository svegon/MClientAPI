package com.github.svegon.utils.fast.util.booleans.transform.objects;

import com.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanSequentialList;
import com.github.svegon.utils.interfaces.function.IntObjectBiPredicate;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class L2ZTransformingList<E> extends TransformingBooleanSequentialList<E, List<E>> {
    private final IntObjectBiPredicate<? super E> transformer;

    public L2ZTransformingList(List<E> list,  IntObjectBiPredicate<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull BooleanListIterator listIterator(int index) {
        return new L2ZTransformingListIterator<>(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeBoolean(int index) {
        return transformer.test(index, list.remove(index));
    }
}
