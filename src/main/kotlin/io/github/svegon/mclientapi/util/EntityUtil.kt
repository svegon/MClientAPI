package io.github.svegon.capi.util

import com.google.common.collect.Lists
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import java.util.stream.Stream
import java.util.stream.StreamSupport

class EntityUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        fun getEntitiesStream(world: ClientWorld, parallel: Boolean): Stream<Entity> {
            return StreamSupport.stream(world.getEntities().spliterator(), parallel)
        }

        fun getEntitiesStream(world: ClientWorld): Stream<Entity> {
            return getEntitiesStream(world, false)
        }

        fun getEntitiesParallelStream(world: ClientWorld): Stream<Entity> {
            return getEntitiesStream(world, true)
        }

        fun getEntitiesList(world: ClientWorld): List<Entity> {
            return Lists.newArrayList(world.getEntities())
        }
    }
}
