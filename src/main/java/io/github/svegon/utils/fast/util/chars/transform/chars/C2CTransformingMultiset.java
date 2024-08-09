package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public class C2CTransformingMultiset extends TransformingCharMultiset<Character, CharCollection> {
    private final CharUnaryOperator forwardingTransformer;
    private final CharUnaryOperator backingTransformer;

    public C2CTransformingMultiset(CharCollection c, CharUnaryOperator forwardingTransformer,
                                   CharUnaryOperator backingTransformer) {
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
        return col.contains(backingTransformer.apply(value));
    }

    @Override
    public boolean removeIf(final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.apply(t)));
    }
}
