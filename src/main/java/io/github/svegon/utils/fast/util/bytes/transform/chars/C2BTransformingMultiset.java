package io.github.svegon.utils.fast.util.bytes.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharCollection;

public class C2BTransformingMultiset extends TransformingByteMultiset<Character, CharCollection> {
    private final Char2ByteFunction forwardingTransformer;
    private final Byte2CharFunction backingTransformer;

    public C2BTransformingMultiset(CharCollection c, Char2ByteFunction forwardingTransformer,
                                   Byte2CharFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ByteIterator iterator() {
        return IterationUtil.mapToByte(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(byte value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
