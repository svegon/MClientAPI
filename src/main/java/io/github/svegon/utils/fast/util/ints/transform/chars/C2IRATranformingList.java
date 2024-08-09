package io.github.svegon.utils.fast.util.ints.transform.chars;

import io.github.svegon.utils.fast.util.ints.transform.TransformingIntRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharList;

import java.util.function.IntPredicate;

public class C2IRATranformingList extends TransformingIntRandomAccessList<Character, CharList> {
    private final Char2IntFunction transformer;

    public C2IRATranformingList(CharList list, Char2IntFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public int getInt(int index) {
        return transformer.get(list.getChar(index));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        return list.removeIf(e -> filter.test(transformer.get(e)));
    }
}
