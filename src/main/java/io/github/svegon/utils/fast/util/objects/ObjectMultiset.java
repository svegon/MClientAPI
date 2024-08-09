package io.github.svegon.utils.fast.util.objects;

import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSpliterator;

import java.util.Set;

public interface ObjectMultiset<E> extends Multiset<E>, ObjectCollection<E> {
    @Override
    default ObjectSpliterator<E> spliterator() {
        return ObjectCollection.super.spliterator();
    }

    @Override
    ObjectSet<Entry<E>> entrySet();
}
