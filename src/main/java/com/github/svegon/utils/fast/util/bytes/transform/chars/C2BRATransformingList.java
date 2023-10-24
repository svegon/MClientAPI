package com.github.svegon.utils.fast.util.bytes.transform.chars;


import com.github.svegon.utils.fast.util.bytes.transform.TransformingByteRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharList;

public class C2BRATransformingList extends TransformingByteRandomAccessList<Character, CharList> {
    private final Char2ByteFunction transformer;

    public C2BRATransformingList(CharList list, Char2ByteFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public byte getByte(int index) {
        return transformer.get(list.getChar(index));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
