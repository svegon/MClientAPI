package io.github.svegon.mclientapi.client.util

import com.google.common.collect.Lists
import com.mojang.authlib.GameProfile
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.github.svegon.mclientapi.mixininterface.IDefaultPosArgument
import io.github.svegon.mclientapi.mixininterface.IEntitySelector
import io.github.svegon.mclientapi.mixininterface.ILookingPosArgument
import io.github.svegon.mclientapi.util.GeometryUtil
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.*
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.math.*
import net.minecraft.world.World
import java.util.*
import java.util.function.Predicate

object CommandUtil {
    @Throws(CommandSyntaxException::class)
    fun getEntity(context: CommandContext<FabricClientCommandSource>, name: String): Entity {
        val list = getEntities(context, name)

        if (list.isEmpty()) {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create()
        } else if (list.size > 1) {
            throw EntityArgumentType.TOO_MANY_ENTITIES_EXCEPTION.create()
        } else {
            return list[0]
        }
    }

    fun getEntities(
        context: CommandContext<FabricClientCommandSource>,
        argName: String,
    ): List<Entity> {
        val selector: EntitySelector = context.getArgument(argName, EntitySelector::class.java)
        val playerName: String? = (selector as IEntitySelector).playerName
        val uuid: UUID? = (selector as IEntitySelector).uuid

        if (!selector.includesNonPlayers()) {
            return getPlayers(context, argName)
        } else if (playerName != null) {
            for (player in context.source.world.players) {
                if (player.gameProfile.name.equals(playerName)) {
                    return listOf(player)
                }
            }

            return emptyList()
        } else if (uuid != null) {
            val worldRegistry: Registry<World> = context.source.registryManager.get(RegistryKeys.WORLD)
            var entity: Entity?

            for (worldKey in context.source.worldKeys) {
                val world: World = worldRegistry[worldKey] ?: continue
                entity = getEntityByUUID(world, uuid)

                if (entity != null && entity.type.isEnabled(context.source.enabledFeatures)) {
                    return listOf(entity)
                }
            }

            return emptyList()
        } else {
            val vec3d: Vec3d = (selector as IEntitySelector).positionOffset.apply(
                context.source.player.pos
            )
            var box: Box? = (selector as IEntitySelector).`mClientAPI$getOffsetBox`(vec3d)
            val predicate: Predicate<Entity> = (selector as IEntitySelector).`mClientAPI$positionPredicate`(vec3d, box,
                if (selector.isSenderOnly) null else context.source.enabledFeatures)

            if (selector.isSenderOnly) {
                return (if (predicate.test(context.source.player)) Lists.newArrayList(context.source.player)
                else emptyList())
            } else {
                val entities: MutableList<Entity> = Lists.newArrayList()
                box = (selector as IEntitySelector).box

                if (selector.isLocalWorldOnly) {
                    appendEntitiesFromWorld(selector, entities, context.source.world, box, predicate)
                } else {
                    val worldRegistry: Registry<World> = context.source.registryManager.get(RegistryKeys.WORLD)

                    for (worldKey in context.source.worldKeys) {
                        val world: World = worldRegistry.get(worldKey) ?: continue

                        appendEntitiesFromWorld(selector, entities, world, box, predicate)
                    }
                }

                return (selector as IEntitySelector).`mClientAPI$getEntities`(vec3d, entities)
            }
        }
    }

    private fun appendEntitiesFromWorld(
        selector: EntitySelector,
        entities: MutableList<Entity>,
        world: World,
        box: Box?,
        predicate: Predicate<Entity>,
    ) {
        val appendLimit = (selector as IEntitySelector).entityAppendLimit

        if (entities.size < appendLimit) {
            world.collectEntitiesByType((selector as IEntitySelector).entityFilter,
                box ?: GeometryUtil.UNBOUND_BOX, predicate, entities, appendLimit)
        }
    }

    fun getPlayers(
        context: CommandContext<FabricClientCommandSource>,
        argName: String,
    ): List<PlayerEntity> {
        val selector: EntitySelector = context.getArgument(argName, EntitySelector::class.java)
        val playerName: String? = (selector as IEntitySelector).playerName
        val uuid: UUID? = (selector as IEntitySelector).uuid

        if (playerName != null) {
            for (player in context.source.world.players) {
                if (player.gameProfile.name.equals(playerName)) {
                    return listOf<PlayerEntity>(player)
                }
            }

            return emptyList()
        } else if (uuid != null) {
            for (player in context.source.world.players) {
                if (player.uuid.equals(uuid)) {
                    return listOf<PlayerEntity>(player)
                }
            }

            return emptyList()
        } else {
            val vec3d: Vec3d = (selector as IEntitySelector).positionOffset.apply(
                context.source.player.pos
            )
            val box = (selector as IEntitySelector).`mClientAPI$getOffsetBox`(vec3d)
            val predicate: Predicate<Entity> = (selector as IEntitySelector).`mClientAPI$positionPredicate`(vec3d,
                box, null)

            if (selector.isSenderOnly) {
                val player: ClientPlayerEntity = context.source.player

                return if (predicate.test(player)) listOf(player) else emptyList()
            } else {
                val appendLimit = (selector as IEntitySelector).entityAppendLimit
                val players: MutableList<AbstractClientPlayerEntity>

                if (selector.isLocalWorldOnly) {
                    players = getPlayers(context.source.world, predicate, appendLimit)
                } else {
                    players = Lists.newArrayList()

                    for (player in context.source.world.players) {
                        if (predicate.test(player)) {
                            players.add(player)

                            if (players.size >= appendLimit) {
                                return players
                            }
                        }
                    }
                }

                return (selector as IEntitySelector).`mClientAPI$getEntities`(vec3d, players)
            }
        }
    }

    @Throws(CommandSyntaxException::class)
    fun getPlayer(
        context: CommandContext<FabricClientCommandSource>,
        name: String,
    ): AbstractClientPlayerEntity {
        val list: List<PlayerEntity> = getPlayers(context, name)

        if (list.size != 1) {
            throw EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION.create()
        } else {
            return list[0] as AbstractClientPlayerEntity
        }
    }

    fun getPlayers(
        world: ClientWorld,
        predicate: Predicate<in AbstractClientPlayerEntity>,
        limit: Int
    ): MutableList<AbstractClientPlayerEntity> {
        val list: MutableList<AbstractClientPlayerEntity> = Lists.newArrayList()

        for (player in world.players) {
            if (predicate.test(player)) {
                list.add(player)

                if (list.size >= limit) {
                    return list
                }
            }
        }

        return list
    }

    fun getEntityByUUID(world: World, uuid: UUID): Entity? {
        for (entity in world.getOtherEntities(null, GeometryUtil.UNBOUND_BOX
        ) { e: Entity -> e.uuid == uuid }) {
            return entity
        }

        return null
    }

    @Throws(CommandSyntaxException::class)
    fun getGameProfiles(
        context: CommandContext<FabricClientCommandSource>,
        name: String,
    ): Collection<GameProfile> {
        return context.getArgument(name, GameProfileArgumentType.GameProfileArgument::class.java)
            .getNames(context.source as ServerCommandSource)
    }

    fun getPosArgument(context: CommandContext<FabricClientCommandSource>, name: String): PosArgument {
        return context.getArgument(name, PosArgument::class.java)
    }

    fun toAbsolutePos(argument: PosArgument, source: FabricClientCommandSource): Vec3d {
        if (argument is LookingPosArgument) {
            val x: Double = (argument as ILookingPosArgument).x
            val y: Double = (argument as ILookingPosArgument).y
            val z: Double = (argument as ILookingPosArgument).y
            val vec2f: Vec2f = source.rotation
            val vec3d: Vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(source.entity)
            val f: Float = MathHelper.cos((vec2f.y + 90.0f) * (Math.PI.toFloat() / 180))
            val g: Float = MathHelper.sin((vec2f.y + 90.0f) * (Math.PI.toFloat() / 180))
            val h: Float = MathHelper.cos(-vec2f.x * (Math.PI.toFloat() / 180))
            val i: Float = MathHelper.sin(-vec2f.x * (Math.PI.toFloat() / 180))
            val j: Float = MathHelper.cos((-vec2f.x + 90.0f) * (Math.PI.toFloat() / 180))
            val k: Float = MathHelper.sin((-vec2f.x + 90.0f) * (Math.PI.toFloat() / 180))
            val vec3d2 = Vec3d((f * h).toDouble(), i.toDouble(), (g * h).toDouble())
            val vec3d3 = Vec3d((f * j).toDouble(), k.toDouble(), (g * j).toDouble())
            val vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0)
            val d = vec3d2.x * z + vec3d3.x * y + vec3d4.x * x
            val e = vec3d2.y * z + vec3d3.y * y + vec3d4.y * x
            val l = vec3d2.z * z + vec3d3.z * y + vec3d4.z * x
            return Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l)
        }

        if (argument is DefaultPosArgument) {
            val x: CoordinateArgument = (argument as IDefaultPosArgument).x
            val y: CoordinateArgument = (argument as IDefaultPosArgument).y
            val z: CoordinateArgument = (argument as IDefaultPosArgument).y
            val pos: Vec3d = source.position

            return Vec3d(
                x.toAbsoluteCoordinate(pos.getX()), y.toAbsoluteCoordinate(pos.getY()),
                z.toAbsoluteCoordinate(pos.getZ())
            )
        }

        throw IllegalStateException("unknown PosArgument: " + argument.javaClass + " " + argument)
    }

    fun toAbsoluteRotation(argument: PosArgument, source: FabricClientCommandSource): Vec2f {
        if (argument is LookingPosArgument) {
            return Vec2f.ZERO
        }

        if (argument is DefaultPosArgument) {
            val x: CoordinateArgument = (argument as IDefaultPosArgument).x
            val y: CoordinateArgument = (argument as IDefaultPosArgument).y
            val rot: Vec2f = source.rotation

            return Vec2f(
                x.toAbsoluteCoordinate(rot.x.toDouble()).toFloat(),
                y.toAbsoluteCoordinate(rot.y.toDouble()).toFloat()
            )
        }

        throw IllegalStateException("unknown PosArgument: " + argument.javaClass + " " + argument)
    }

    fun toAbsoluteBlockPos(argument: PosArgument, source: FabricClientCommandSource): BlockPos {
        val absolutePos = toAbsolutePos(argument, source)
        return BlockPos(absolutePos.x.toInt(), absolutePos.y.toInt(), absolutePos.z.toInt())
    }
}
