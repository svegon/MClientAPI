package io.github.svegon.utils.fast.util.objects;

import io.github.svegon.utils.collections.AbstractMultiset;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractObjectMultiset<E> extends AbstractMultiset<E> implements ObjectMultiset<E> {
    @Override
    public abstract ObjectIterator<E> iterator();

    @Override
    public @NotNull ObjectSet<Multiset.Entry<E>> entrySet() {
        return (ObjectSet<Multiset.Entry<E>>) super.entrySet();
    }
}
