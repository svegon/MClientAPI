package io.github.svegon.utils.fast.util.objects.transform.chars;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.objects.transform.TransformingObjectSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.function.Predicate;

public class C2LTransformingList<E> extends TransformingObjectSequentialList<Character, CharList, E> {
    private final Char2ObjectFunction<? extends E> transformer;

    public C2LTransformingList(CharList list, Char2ObjectFunction<? extends E> transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public ObjectListIterator<E> listIterator(int index) {
        return IterationUtil.mapToObj(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }
}
