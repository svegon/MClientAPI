package io.github.svegon.utils.collections;

import java.util.Comparator;

public enum SetType {
    HASH {
        @Override
        public double memoryEffectiveFragmentation() {
            return Double.MAX_VALUE;
        }
    },
    COMPRESSED_TABLE {
        @Override
        public double memoryEffectiveFragmentation() {
            return SetUtil.BYTE_TABLE_SET_EFFECTIVE_FRAGMENTATION;
        }
    },
    TABLE {
        @Override
        public double memoryEffectiveFragmentation() {
            return SetUtil.BYTE_TABLE_SET_EFFECTIVE_FRAGMENTATION / 8;
        }
    };

    public abstract double memoryEffectiveFragmentation();

    /**
     * the ordinals sort them from the slowest to the fastest
     */
    public static Comparator<SetType> timePriority() {
        return Comparator.comparingInt(SetType::ordinal);
    }

    public static Comparator<SetType> memoryPriority() {
        return Comparator.comparingDouble(SetType::memoryEffectiveFragmentation).reversed();
    }
}
