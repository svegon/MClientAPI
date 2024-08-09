package io.github.svegon.utils.fast.util.chars.transform.bytes;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

public class B2CTransformingSet extends TransformingCharSet<Byte, ByteSet> {
    private final Byte2CharFunction forwardingTransformer;
    private final Char2ByteFunction backingTransformer;

    /**
     * the caller guarantees the forwarding and backing transformers map each
     * distinct original, respectively transformed element to a distinct
     * transformed, respectively original element
     *
     * @param set
     * @param forwardingTransformer
     * @param backingTransformer
     */
    public B2CTransformingSet(ByteSet set, Byte2CharFunction forwardingTransformer,
                              Char2ByteFunction backingTransformer) {
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
