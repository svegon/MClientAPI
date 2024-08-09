package io.github.svegon.utils.fast.util.chars.transform.ints;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntCollection;

public class I2CTransformingMultiset extends TransformingCharMultiset<Integer, IntCollection> {
    private final Int2CharFunction forwardingTransformer;
    private final Char2IntFunction backingTransformer;

    public I2CTransformingMultiset(IntCollection c, Int2CharFunction forwardingTransformer,
                                   Char2IntFunction backingTransformer) {
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
