package io.github.svegon.mclientapi.mixininterface

import net.minecraft.entity.Entity
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.function.Function
import java.util.function.Predicate

interface IEntitySelector {
    val positionOffset: Function<Vec3d, Vec3d>

    val box: Box?

    val playerName: String?

    val uuid: UUID?

    val entityFilter: TypeFilter<Entity?, *>

    val entityAppendLimit: Int

    fun `mClientAPI$getOffsetBox`(offset: Vec3d): Box?

    fun `mClientAPI$positionPredicate`(pos: Vec3d, box: Box?, enabledFeatures: FeatureSet?): Predicate<Entity>

    fun <T : Entity> `mClientAPI$getEntities`(pos: Vec3d, entities: List<T>): List<T>
}