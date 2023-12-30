package io.github.svegon.capi.util;

import com.google.common.collect.Lists;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class EntityUtil {
    private EntityUtil() {
        throw new UnsupportedOperationException();
    }

    public static Stream<Entity> getEntitiesStream(@NotNull final ClientWorld world, boolean parallel) {
        return StreamSupport.stream(world.getEntities().spliterator(), parallel);
    }

    public static Stream<Entity> getEntitiesStream(@NotNull final ClientWorld world) {
        return getEntitiesStream(world, false);
    }

    public static Stream<Entity> getEntitiesParallelStream(@NotNull final ClientWorld world) {
        return getEntitiesStream(world, true);
    }

    public static List<Entity> getEntitiesList(@NotNull final ClientWorld world) {
        return Lists.newArrayList(world.getEntities());
    }
}
