package io.github.svegon.capi.util

import com.google.common.collect.Lists
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.entity.Entity
import net.minecraft.registry.Registry
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.function.Predicate

class CommandUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        @Throws(CommandSyntaxException::class)
        fun getEntity(context: CommandContext<FabricClientCommandSource?>, name: String?): Entity {
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
            context: CommandContext<FabricClientCommandSource?>,
            argName: String?
        ): List<Entity> {
            val selector: EntitySelector = context.getArgument<EntitySelector>(argName, EntitySelector::class.java)
            val playerName: String = (selector as EntitySelectorAccessor).getPlayerName()
            val uuid: UUID = (selector as EntitySelectorAccessor).getUuid()

            if (!selector.includesNonPlayers()) {
                return getPlayers(context, argName)
            } else if (playerName != null) {
                for (player in context.getSource().getWorld().getPlayers()) {
                    if (player.getGameProfile().getName().equals(playerName)) {
                        return listOf(player)
                    }
                }

                return emptyList()
            } else if (uuid != null) {
                val worldRegistry: Registry<World> = context.getSource().getRegistryManager().get(RegistryKeys.WORLD)
                var entity: Entity

                for (worldKey in context.getSource().getWorldKeys()) {
                    val world: World = worldRegistry[worldKey]

                    if (world != null && (getEntityByUUID(world, uuid).also { entity = it!! }) != null) {
                        return listOf(entity)
                    }
                }

                return emptyList()
            } else {
                val vec3d: Vec3d = (selector as EntitySelectorAccessor).getPositionOffset().apply(
                    context.getSource().getPlayer()
                        .getPos()
                )
                val predicate: Predicate<Entity> = (selector as EntitySelectorAccessor).getPositionPredicate(vec3d)

                if (selector.isSenderOnly()) {
                    return (if (predicate.test(context.getSource().getPlayer())) Lists.newArrayList(
                        context.getSource()
                            .getPlayer()
                    ) else emptyList())
                } else {
                    val list: MutableList<Entity> = Lists.newArrayList()
                    val box: Box = (selector as EntitySelectorAccessor).getBox()

                    if (selector.isLocalWorldOnly()) {
                        if (box != null) {
                            list.addAll(
                                context.getSource().getWorld().getEntitiesByType(
                                    (selector as EntitySelectorAccessor)
                                        .getEntityFilter(), box.offset(vec3d), predicate
                                )
                            )
                        } else {
                            list.addAll(
                                context.getSource().getWorld().getEntitiesByType(
                                    (selector as EntitySelectorAccessor)
                                        .getEntityFilter(), GeometryUtil.UNBOUND_BOX, predicate
                                )
                            )
                        }
                    } else {
                        val worldRegistry: Registry<World> =
                            context.getSource().getRegistryManager().get(RegistryKeys.WORLD)

                        for (worldKey in context.getSource().getWorldKeys()) {
                            val world: World = worldRegistry.get(worldKey) ?: continue

                            if (box != null) {
                                list.addAll(
                                    world.getEntitiesByType(
                                        (selector as EntitySelectorAccessor)
                                            .getEntityFilter(), box.offset(vec3d), predicate
                                    )
                                )
                            } else {
                                list.addAll(
                                    world.getEntitiesByType(
                                        (selector as EntitySelectorAccessor)
                                            .getEntityFilter(), GeometryUtil.UNBOUND_BOX, predicate
                                    )
                                )
                            }
                        }
                    }

                    return (selector as EntitySelectorAccessor).getEntities<Entity>(vec3d, list)
                }
            }
        }

        fun getPlayers(
            context: CommandContext<FabricClientCommandSource?>,
            argName: String?
        ): List<PlayerEntity> {
            val selector: EntitySelector = context.getArgument<EntitySelector>(argName, EntitySelector::class.java)
            val playerName: String = (selector as EntitySelectorAccessor).getPlayerName()
            val uuid: UUID = (selector as EntitySelectorAccessor).getUuid()

            if (playerName != null) {
                for (player in context.getSource().getWorld().getPlayers()) {
                    if (player.getGameProfile().getName().equals(playerName)) {
                        return listOf<PlayerEntity>(player)
                    }
                }

                return emptyList<PlayerEntity>()
            } else if (uuid != null) {
                for (player in context.getSource().getWorld().getPlayers()) {
                    if (player.getUuid().equals(uuid)) {
                        return listOf<PlayerEntity>(player)
                    }
                }

                return emptyList<PlayerEntity>()
            } else {
                val vec3d: Vec3d = (selector as EntitySelectorAccessor).getPositionOffset().apply(
                    context.getSource().getPlayer()
                        .getPos()
                )
                val predicate: Predicate<Entity?> = (selector as EntitySelectorAccessor).getPositionPredicate(vec3d)

                if (selector.isSenderOnly()) {
                    val player: ClientPlayerEntity = context.getSource().getPlayer()

                    return if (predicate.test(player)) listOf<PlayerEntity>(player) else emptyList<PlayerEntity>()
                } else {
                    val list: MutableList<AbstractClientPlayerEntity>

                    if (selector.isLocalWorldOnly()) {
                        list = getPlayers(context.getSource().getWorld(), predicate)
                    } else {
                        list = Lists.newArrayList<AbstractClientPlayerEntity>()

                        for (player in context.getSource().getWorld().getPlayers()) {
                            if (predicate.test(player)) {
                                list.add(player)
                            }
                        }
                    }

                    return (selector as EntitySelectorAccessor).getEntities(vec3d, list)
                }
            }
        }

        @Throws(CommandSyntaxException::class)
        fun getPlayer(
            context: CommandContext<FabricClientCommandSource?>,
            name: String
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
            predicate: Predicate<in AbstractClientPlayerEntity?>
        ): MutableList<AbstractClientPlayerEntity> {
            val list: MutableList<AbstractClientPlayerEntity> = ArrayList<AbstractClientPlayerEntity>()

            for (player in world.getPlayers()) {
                if (predicate.test(player)) {
                    list.add(player)
                }
            }

            return list
        }

        fun getEntityByUUID(world: World, uuid: UUID): Entity? {
            for (entity in world.getOtherEntities(null, GeometryUtil.UNBOUND_BOX,
                Predicate<Entity> { e: Entity -> e.uuid == uuid })) {
                return entity
            }

            return null
        }

        @Throws(CommandSyntaxException::class)
        fun getGameProfiles(
            context: CommandContext<FabricClientCommandSource?>,
            name: String
        ): Collection<GameProfile> {
            return context.getArgument<GameProfileArgument>(name, GameProfileArgument::class.java)
                .getNames(context.getSource() as ServerCommandSource)
        }

        fun getPosArgument(context: CommandContext<FabricClientCommandSource?>, name: String?): PosArgument {
            return context.getArgument<PosArgument>(name, PosArgument::class.java)
        }

        fun toAbsolutePos(argument: PosArgument, source: FabricClientCommandSource): Vec3d {
            if (argument is LookingPosArgument) {
                val x: Double = (argument as ILookingPosArgument).getX()
                val y: Double = (argument as ILookingPosArgument).getY()
                val z: Double = (argument as ILookingPosArgument).getZ()
                val vec2f: Vec2f = source.getRotation()
                val vec3d: Vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(source.getEntity())
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
                val x: CoordinateArgument = (argument as IDefaultPosArgument).getX()
                val y: CoordinateArgument = (argument as IDefaultPosArgument).getY()
                val z: CoordinateArgument = (argument as IDefaultPosArgument).getZ()
                val pos: Vec3d = source.getPosition()

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
                val x: CoordinateArgument = (argument as IDefaultPosArgument).getX()
                val y: CoordinateArgument = (argument as IDefaultPosArgument).getY()
                val rot: Vec2f = source.getRotation()

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
}
