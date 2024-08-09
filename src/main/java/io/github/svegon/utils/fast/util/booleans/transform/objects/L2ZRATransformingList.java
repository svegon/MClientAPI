package io.github.svegon.utils.fast.util.booleans.transform.objects;

import io.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanRandomAccessList;
import io.github.svegon.utils.interfaces.function.IntObjectBiPredicate;
import com.google.common.base.Preconditions;

import java.util.List;

public class L2ZRATransformingList<E> extends TransformingBooleanRandomAccessList<E, List<E>> {
    private final IntObjectBiPredicate<? super E> transformer;

    public L2ZRATransformingList(List<E> list,  IntObjectBiPredicate<? super E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public boolean getBoolean(int index) {
        return transformer.test(index, list.get(index));
    }
}
