package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.Byte2ByteFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ByteMap;
import it.unimi.dsi.fastutil.bytes.ByteBinaryOperator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

@Immutable
public abstract class ImmmutableByte2ByteMap implements Byte2ByteMap {
    private final int hashCode;

    ImmmutableByte2ByteMap(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public ImmmutableByte2ByteMap clone() {
        return this;
    }

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final void putAll(@NotNull Map<? extends Byte, ? extends Byte> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte defaultReturnValue() {
        return 0;
    }

    @Override
    public abstract ObjectSet<Entry> byte2ByteEntrySet();

    @Override
    public abstract ImmutableByteSet keySet();

    @Override
    public abstract ByteCollection values();

    @Override
    public abstract byte get(byte key);

    @Override
    public abstract boolean containsKey(byte key);

    @Override
    public abstract boolean containsValue(byte value);

    @Override
    public abstract byte getOrDefault(byte key, byte defaultValue);

    @Override
    public final byte putIfAbsent(byte key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(byte key, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean replace(byte key, byte oldValue, byte newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte computeIfAbsent(byte key, Byte2ByteFunction mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte compute(byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte merge(byte key, byte value,
                            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte mergeByte(byte key, byte value, ByteBinaryOperator remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte computeIfPresent(byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public static ImmutableByte2ByteSortedMap copyOf(final @NotNull Byte2ByteMap map) {
        if (map instanceof ImmutableByte2ByteSortedMap) {
            return (ImmutableByte2ByteSortedMap) map;
        }

        return ImmutableByte2ByteSortedMap.copyOf(null, map);
    }

    public static ImmutableByte2ByteSortedMap copyOf(final @NotNull Iterable<Entry> entries) {
        return ImmutableByte2ByteSortedMap.copyOf(null, entries);
    }

    public static ImmutableByte2ByteSortedMap of(final byte @NotNull ... keyValuePairs) {
        return ImmutableByte2ByteSortedMap.of(null, keyValuePairs);
    }
}
