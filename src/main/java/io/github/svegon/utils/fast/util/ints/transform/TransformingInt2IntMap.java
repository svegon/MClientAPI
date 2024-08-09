package io.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.AbstractInt2IntMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingInt2IntMap<K_IN, V_IN, M extends Map<K_IN, V_IN>> extends AbstractInt2IntMap {
    protected final M m;

    protected TransformingInt2IntMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry> int2IntEntrySet();

    @Override
    public abstract int put(int key, int value);

    @Override
    public abstract int remove(int key);

    @Override
    public abstract int get(int key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>> implements Entry {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry intEntry
                    ? getIntKey() == intEntry.getIntKey() && getIntValue() == intEntry.getIntValue()
                    : Objects.equals(getKey(), entry.getKey()) && Objects.equals(entry.getValue(), getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getIntKey() + "=" + getValue();
        }
    }
}
