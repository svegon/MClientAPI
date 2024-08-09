package io.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.AbstractChar2CharMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingChar2CharMap<K_IN, V_IN, M extends Map<K_IN, V_IN>> extends AbstractChar2CharMap {
    protected final M m;

    protected TransformingChar2CharMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry> char2CharEntrySet();

    @Override
    public abstract char put(char key, char value);

    @Override
    public abstract char remove(char key);

    @Override
    public abstract char get(char key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>> implements Entry {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry charEntry
                    ? getCharKey() == charEntry.getCharKey() && getCharValue() == charEntry.getCharValue()
                    : Objects.equals(getKey(), entry.getKey()) && Objects.equals(entry.getValue(), getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getCharKey() + "=" + getValue();
        }
    }
}
