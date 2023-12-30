package io.github.svegon.utils.fast.util.ints.transform.chars;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.function.IntPredicate;

public class C2ITransformingList extends TransformingIntSequentialList<Character, CharList> {
    private final Char2IntFunction transformer;

    public C2ITransformingList(CharList list, Char2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new C2ITransformingListIterator(list.listIterator(), transformer);
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        Preconditions.checkNotNull(filter);

        return list.removeIf(e -> filter.test(transformer.get(e)));
    }

    @Override
    public int removeInt(int index) {
        return transformer.get(list.getChar(index));
    }
}
