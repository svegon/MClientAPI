package io.github.svegon.utils.fast.util.bytes.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.bytes.AbstractByte2ByteMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingByte2ByteMap<K_IN, V_IN, M extends Map<K_IN, V_IN>> extends AbstractByte2ByteMap {
    protected final M m;

    protected TransformingByte2ByteMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry> byte2ByteEntrySet();

    @Override
    public abstract byte put(byte key, byte value);

    @Override
    public abstract byte remove(byte key);

    @Override
    public abstract byte get(byte key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>> implements Entry {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry byteEntry
                    ? getByteKey() == byteEntry.getByteKey() && getByteValue() == byteEntry.getByteValue()
                    : Objects.equals(getKey(), entry.getKey()) && Objects.equals(entry.getValue(), getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getByteKey() + "=" + getValue();
        }
    }
}
