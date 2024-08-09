package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.fast.util.chars.transform.TransformingCharRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public class C2CRATransformingList extends TransformingCharRandomAccessList<Character, CharList> {
    private final CharUnaryOperator transformer;

    public C2CRATransformingList(CharList list, CharUnaryOperator transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public char getChar(int index) {
        return transformer.apply(list.getChar(index));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.apply(e)));
    }
}
