package io.github.svegon.mclientapi.mixininterface

import net.minecraft.world.entity.Entity
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Predicate

interface IEntitySelector {
    val positionOffset: Function<Vec3, Vec3>

    val box: AABB?

    val playerName: String?

    val uuid: UUID?

    val entityFilter: EntityTypeTest<Entity, *>

    val `mClientAPI$order`: BiConsumer<Vec3, java.util.List<out Entity>>

    val `mClientAPI$limit`: Int

    fun `mClientAPI$positionPredicate`(pos: Vec3, AABB: AABB?, enabledFeatures: FeatureFlagSet?): Predicate<Entity>

    fun `mClientAPI$sortAndLimit`(pos: Vec3, entities: MutableList<Entity>): List<Entity>
}