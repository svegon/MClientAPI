package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectRandomAccessList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharList;

import java.util.function.Predicate;

public class C2LRATransformingList<E> extends TransformingObjectRandomAccessList<Character, CharList, E> {
    private final Char2ObjectFunction<? extends E> transformer;

    public C2LRATransformingList(CharList list, Char2ObjectFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public E get(int index) {
        return transformer.get(list.getChar(index));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
