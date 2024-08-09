package io.github.svegon.mclientapi.client.util

import com.google.common.collect.Lists
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import java.util.stream.Stream
import java.util.stream.StreamSupport

object ClientWorldUtil {
    fun ClientWorld.getEntitiesStream(parallel: Boolean): Stream<Entity> {
        return StreamSupport.stream(entities.spliterator(), parallel)
    }

    fun ClientWorld.getEntitiesStream(): Stream<Entity> {
        return getEntitiesStream(false)
    }

    fun ClientWorld.getEntitiesParallelStream(): Stream<Entity> {
        return getEntitiesStream(true)
    }

    fun ClientWorld.getEntitiesList(): List<Entity> {
        return Lists.newArrayList(entities)
    }
}
