package io.github.svegon.mclientapi.client.util.extension

import com.google.common.collect.Lists
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.entity.Entity
import java.util.stream.Stream
import kotlin.streams.asStream

object ClientLevelExtension {
    val ClientLevel.entities: Iterable<Entity>
        get() {
            return entitiesForRendering()
        }

    val ClientLevel.getEntitiesStream: Stream<Entity>
        get() {
            return entities.asSequence().asStream()
        }

    val ClientLevel.getEntitiesList: List<Entity>
        get() {
            return Lists.newArrayList(entities)
        }
}