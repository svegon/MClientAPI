package com.github.svegon.utils.fast.util.booleans.transform.bytes;

import com.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanListIterator;
import com.github.svegon.utils.fast.util.booleans.transform.TransformingBooleanSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import it.unimi.dsi.fastutil.booleans.BooleanPredicate;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class B2ZTransformingList extends TransformingBooleanSequentialList<Byte, ByteList> {
    private final BytePredicate transformer;

    public B2ZTransformingList(ByteList list, BytePredicate transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull BooleanListIterator listIterator(int index) {
        return new TransformingBooleanListIterator<>(list.listIterator(index)) {
            @Override
            public boolean nextBoolean() {
                return transformer.test(itr.next());
            }

            @Override
            public boolean previousBoolean() {
                return transformer.test(itr.previous());
            }
        };
    }

    @Override
    public boolean removeIf(final BooleanPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.test(e)));
    }

    @Override
    public boolean removeBoolean(int index) {
        return transformer.test(list.removeByte(index));
    }
}
