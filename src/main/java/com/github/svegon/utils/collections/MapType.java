package com.github.svegon.utils.collections;

import java.util.Comparator;

public enum MapType {
    HASH {
        @Override
        public double memoryEffectiveFragmentation() {
            return MapUtil.MAX_EFFECTIVE_FRAGMENTATION_OF_KEYS_FOR_HASH_MAP;
        }
    },
    TABLE_FORK {
        @Override
        public double memoryEffectiveFragmentation() {
            return 1;
        }
    },
    TABLE {
        @Override
        public double memoryEffectiveFragmentation() {
            return MapUtil.MAX_EFFECTIVE_FRAGMENTATION_OF_KEYS_FOR_TABLE_MAP;
        }
    };

    public abstract double memoryEffectiveFragmentation();

    // the ordinals sort them from the slowest to the fastest

    public static Comparator<MapType> timePriority() {
        return Comparator.comparingInt(MapType::ordinal);
    }

    public static Comparator<MapType> memoryPriority() {
        return Comparator.comparingDouble(MapType::memoryEffectiveFragmentation).reversed();
    }
}
