package io.github.svegon.utils.fast.util.chars.transform.bytes;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class B2CTransformingMultiset extends TransformingCharMultiset<Byte, ByteCollection> {
    private final Byte2CharFunction forwardingTransformer;
    private final Char2ByteFunction backingTransformer;

    public B2CTransformingMultiset(ByteCollection c, Byte2CharFunction forwardingTransformer,
                                   Char2ByteFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharIterator iterator() {
        return IterationUtil.mapToChar(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
