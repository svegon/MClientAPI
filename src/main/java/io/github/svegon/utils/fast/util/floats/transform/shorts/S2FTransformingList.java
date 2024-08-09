package io.github.svegon.utils.fast.util.floats.transform.shorts;

import io.github.svegon.utils.fast.util.floats.transform.TransformingFloatSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.jetbrains.annotations.NotNull;

public class S2FTransformingList extends TransformingFloatSequentialList<Short, ShortList> {
    private final Short2FloatFunction transformer;

    public S2FTransformingList(ShortList list, Short2FloatFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull FloatListIterator listIterator(int index) {
        return new S2FTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final FloatPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public float removeFloat(int index) {
        return transformer.get(list.removeShort(index));
    }
}
