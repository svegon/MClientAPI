package io.github.svegon.utils.fast.util.ints.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.transform.TransformingIntMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2IntFunction;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntPredicate;

public class C2ITransformingMultiset extends TransformingIntMultiset<Character, CharCollection> {
    private final Char2IntFunction forwardingTransformer;
    private final Int2CharFunction backingTransformer;

    public C2ITransformingMultiset(CharCollection c, Char2IntFunction forwardingTransformer,
                                   Int2CharFunction backingTransformer) {
        super(c);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public IntIterator iterator() {
        return IterationUtil.mapToInt(col.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(int value) {
        return col.contains(backingTransformer.get(value));
    }

    @Override
    public boolean removeIf(final IntPredicate filter) {
        Preconditions.checkNotNull(filter);
        return col.removeIf(t -> filter.test(forwardingTransformer.get(t)));
    }
}
