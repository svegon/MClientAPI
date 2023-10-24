package com.github.svegon.utils.fast.util.chars.transform.chars;

import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class C2CTransformingSet extends TransformingCharSet<Character, CharSet> {
    private final CharUnaryOperator forwardingTransformer;
    private final CharUnaryOperator backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public C2CTransformingSet(CharSet set, CharUnaryOperator forwardingTransformer,
                              CharUnaryOperator backingTransformer) {
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
        return set.contains(backingTransformer.apply(o));
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
