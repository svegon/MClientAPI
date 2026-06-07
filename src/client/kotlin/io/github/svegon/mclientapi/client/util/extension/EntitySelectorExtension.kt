package io.github.svegon.mclientapi.client.util.extension

import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.github.svegon.mclientapi.mixininterface.IEntitySelector
import io.github.svegon.mclientapi.util.GeometryUtil
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.level.Level
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.UUID
import java.util.function.Predicate

/**
 * adds extensions to {@code EntitySelector} for same-named functions with client command source instead of server's one
 */
object EntitySelectorExtension {
    val EntitySelector.positionOffset: (Vec3) -> Vec3
        get() {
            val javaFun = (this as IEntitySelector).positionOffset
            return javaFun::apply
        }

    val EntitySelector.box: AABB?
        get() {
            return (this as IEntitySelector).box
        }

    val EntitySelector.playerName: String?
        get() {
            return (this as IEntitySelector).playerName
        }

    val EntitySelector.uuid: UUID?
        get() {
            return (this as IEntitySelector).uuid
        }

    val EntitySelector.entityFilter: EntityTypeTest<Entity, *>
        get() {
            return (this as IEntitySelector).entityFilter
        }

    val EntitySelector.order: (Vec3, List<Entity>) -> Unit
        get() {
            val order = (this as IEntitySelector).`mClientAPI$order`
            return { pos: Vec3, entities: List<Entity> -> {
                order.accept(pos, entities as java.util.List<out Entity>)
            }}
        }

    val EntitySelector.limit: Int
        get() {
            return (this as IEntitySelector).`mClientAPI$limit`
        }

    fun EntitySelector.getAABB(offset: Vec3): AABB? {
        return box?.move(offset)
    }

    fun EntitySelector.positionPredicate(pos: Vec3, aabb: AABB?, enabledFeatures: FeatureFlagSet?):
                (Entity) -> Boolean {
        val javaPred = (this as IEntitySelector).`mClientAPI$positionPredicate`(pos, aabb, enabledFeatures)
        return javaPred::test
    }

    fun EntitySelector.sortAndLimit(pos: Vec3, entities: MutableList<Entity>): List<Entity> {
        return (this as IEntitySelector).`mClientAPI$sortAndLimit`(pos, entities)
    }

    @Throws(CommandSyntaxException::class)
    fun EntitySelector.findSingleEntity(sender: FabricClientCommandSource): Entity {
        val entities = findEntities(sender)
        if (entities.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create()
        } else if (entities.size > 1) {
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create()
        } else {
            return entities[0]
        }
    }

    @Throws(CommandSyntaxException::class)
    fun EntitySelector.findEntities(sender: FabricClientCommandSource): List<Entity> {
        if (!(includesEntities())) {
            return findPlayers(sender)
        }

        val playerByName = playerName

        if (playerByName != null) {
            val result = sender.player.connection.getPlayerInfo(playerByName)
            return if (result == null) listOf<Entity>() else
                listOf<Entity>(sender.level.getEntity(result.profile.id)!!)
        }

        val entityUUID = uuid
        val dimensions = sender.registryAccess().getOrThrow(Registries.DIMENSION).value()

        if (entityUUID != null) {
            for (level in sender.levels().map { key -> dimensions.getValue(key)!! }) {
                val entity: Entity? = level.getEntity(entityUUID)
                if (entity != null) {
                    if (entity.type.isEnabled(sender.enabledFeatures())) {
                        return listOf(entity)
                    }
                    break
                }
            }

            return emptyList()
        } else {
            val pos = positionOffset(sender.position)
            val absoluteAabb: AABB? = box
            val predicate = positionPredicate(pos,
                absoluteAabb, sender.enabledFeatures())

            if (isSelfSelector) {
                return if (predicate(sender.entity)) listOf(sender.entity) else emptyList()
            } else {
                val result: ArrayList<Entity> = ArrayList()

                if (isWorldLimited) {
                    addEntities(result, sender.level, absoluteAabb, predicate)
                } else {
                    for (level in sender.levels().map { key -> dimensions.getValue(key)!! }) {
                        addEntities( result, level, absoluteAabb, predicate)
                    }
                }

                return sortAndLimit(pos, result)
            }
        }
    }

    fun EntitySelector.addEntities(
        result: MutableList<Entity>,
        level: Level,
        absoluteAABB: AABB?,
        predicate: Predicate<Entity>
    ) {
        if (result.size < limit) {
            val type = entityFilter

            if (absoluteAABB != null) {
                level.getEntities(type, absoluteAABB, predicate, result, limit)
            } else {
                level.getEntities(type, GeometryUtil.UNBOUND_BOX,
                    predicate, result, limit)
            }
        }
    }

    @Throws(CommandSyntaxException::class)
    fun EntitySelector.findSinglePlayer(sender: FabricClientCommandSource): AbstractClientPlayer {
        val players = findPlayers(sender)
        if (players.size != 1) {
            throw EntityArgument.NO_PLAYERS_FOUND.create()
        } else {
            return players[0]
        }
    }

    @Throws(CommandSyntaxException::class)
    fun EntitySelector.findPlayers(sender: FabricClientCommandSource): List<AbstractClientPlayer> {
        val playerByName = playerName
        val playerUUID: UUID?

        if (playerByName != null) {
            val result = sender.player.connection.getPlayerInfo(playerByName)
            playerUUID = result?.profile?.id
        } else {
            playerUUID = uuid
        }

        if (playerUUID != null) {
            val result = sender.level.getEntity(playerUUID)
            return if (result == null || result.type != EntityType.PLAYER) listOf<AbstractClientPlayer>()
            else listOf<AbstractClientPlayer>(result as AbstractClientPlayer)
        } else {
            val pos = positionOffset(sender.position)
            val absoluteAabb: AABB? = getAABB(pos)
            val predicate = positionPredicate(pos, absoluteAabb, null)

            if (isSelfSelector) {
                if (sender.entity is AbstractClientPlayer) {
                    val player = sender.entity as AbstractClientPlayer

                    if (predicate(player)) {
                        return listOf(player)
                    }
                }

                return emptyList()
            } else {
                val limit: Int = maxResults
                val result: List<Entity> = if (isWorldLimited) {
                    sender.level.players()
                } else {
                    sender.player.connection.onlinePlayers.mapNotNull { playerInfo: PlayerInfo ->
                        sender.level.getEntity(playerInfo.profile.id)
                    }
                }

                return sortAndLimit(pos, result.filter(predicate).take(limit).toMutableList())
                        as List<AbstractClientPlayer>
            }
        }
    }
}