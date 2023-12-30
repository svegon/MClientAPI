package io.github.svegon.utils.fast.util.chars.transform.booleans;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

public class Z2CTransformingList extends TransformingCharSequentialList<Boolean, BooleanList> {
    private final Boolean2CharFunction transformer;

    public Z2CTransformingList(BooleanList list, Boolean2CharFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull CharListIterator listIterator(int index) {
        return new Z2CTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public char removeChar(int index) {
        return transformer.get(list.removeBoolean(index));
    }
}
