package com.github.svegon.utils.fast.util.chars.transform.ints;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntSet;

public class I2CTransformingSet extends TransformingCharSet<Integer, IntSet> {
    private final Int2CharFunction forwardingTransformer;
    private final Char2IntFunction backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public I2CTransformingSet(IntSet set, Int2CharFunction forwardingTransformer,
                              Char2IntFunction backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharIterator iterator() {
        return IterationUtil.mapToChar(set.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char o) {
        return set.contains(backingTransformer.get(o));
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
