package io.github.svegon.capi.util;

import io.github.svegon.capi.mixin.EntitySelectorAccessor;
import io.github.svegon.capi.mixininterface.IDefaultPosArgument;
import io.github.svegon.capi.mixininterface.ILookingPosArgument;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public final class CommandUtil {
    private CommandUtil() {
        throw new UnsupportedOperationException();
    }

    public static Entity getEntity(CommandContext<FabricClientCommandSource> context, String name)
            throws CommandSyntaxException {
        List<? extends Entity> list = getEntities(context, name);

        if (list.isEmpty()) {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        } else if (list.size() > 1) {
            throw EntityArgumentType.TOO_MANY_ENTITIES_EXCEPTION.create();
        } else {
            return list.get(0);
        }
    }

    public static List<? extends Entity> getEntities(CommandContext<FabricClientCommandSource> context,
                                                     String argName) {
        EntitySelector selector = context.getArgument(argName, EntitySelector.class);
        String playerName = ((EntitySelectorAccessor) selector).getPlayerName();
        UUID uuid = ((EntitySelectorAccessor) selector).getUuid();

        if (!selector.includesNonPlayers()) {
            return getPlayers(context, argName);
        } else if (playerName != null) {
            for (AbstractClientPlayerEntity player : context.getSource().getWorld().getPlayers()) {
                if (player.getGameProfile().getName().equals(playerName)) {
                    return Collections.singletonList(player);
                }
            }

            return Collections.emptyList();
        } else if (uuid != null) {
            Registry<World> worldRegistry = context.getSource().getRegistryManager().get(RegistryKeys.WORLD);
            Entity entity;

            for (RegistryKey<World> worldKey : context.getSource().getWorldKeys()) {
                World world = worldRegistry.get(worldKey);

                if (world != null && (entity = getEntityByUUID(world, uuid)) != null) {
                    return Collections.singletonList(entity);
                }
            }

            return Collections.emptyList();
        } else {
            Vec3d vec3d = ((EntitySelectorAccessor) selector).getPositionOffset().apply(context.getSource().getPlayer()
                    .getPos());
            Predicate<Entity> predicate = ((EntitySelectorAccessor) selector).getPositionPredicate(vec3d);

            if (selector.isSenderOnly()) {
                return (predicate.test(context.getSource().getPlayer()) ? Lists.newArrayList(context.getSource()
                        .getPlayer()) : Collections.emptyList());
            } else {
                List<Entity> list = Lists.newArrayList();
                Box box = ((EntitySelectorAccessor) selector).getBox();

                if (selector.isLocalWorldOnly()) {
                    if (box != null) {
                        list.addAll(context.getSource().getWorld().getEntitiesByType(((EntitySelectorAccessor) selector)
                                .getEntityFilter(), box.offset(vec3d), predicate));
                    } else {
                        list.addAll(context.getSource().getWorld().getEntitiesByType(((EntitySelectorAccessor) selector)
                                .getEntityFilter(), GeometryUtil.UNBOUND_BOX, predicate));
                    }
                } else {
                    Registry<World> worldRegistry = context.getSource().getRegistryManager().get(RegistryKeys.WORLD);

                    for (RegistryKey<World> worldKey : context.getSource().getWorldKeys()) {
                        World world = worldRegistry.get(worldKey);

                        if (world == null) {
                            continue;
                        }

                        if (box != null) {
                            list.addAll(world.getEntitiesByType(((EntitySelectorAccessor) selector)
                                    .getEntityFilter(), box.offset(vec3d), predicate));
                        } else {
                            list.addAll(world.getEntitiesByType(((EntitySelectorAccessor) selector)
                                    .getEntityFilter(), GeometryUtil.UNBOUND_BOX, predicate));
                        }
                    }
                }

                return ((EntitySelectorAccessor) selector).getEntities(vec3d, list);
            }
        }
    }

    public static List<? extends PlayerEntity> getPlayers(CommandContext<FabricClientCommandSource> context,
                                                          String argName) {
        EntitySelector selector = context.getArgument(argName, EntitySelector.class);
        String playerName = ((EntitySelectorAccessor) selector).getPlayerName();
        UUID uuid = ((EntitySelectorAccessor) selector).getUuid();

        if (playerName != null) {
            for (AbstractClientPlayerEntity player : context.getSource().getWorld().getPlayers()) {
                if (player.getGameProfile().getName().equals(playerName)) {
                    return Collections.singletonList(player);
                }
            }

            return Collections.emptyList();
        } else if (uuid != null) {
            for (AbstractClientPlayerEntity player : context.getSource().getWorld().getPlayers()) {
                if (player.getUuid().equals(uuid)) {
                    return Collections.singletonList(player);
                }
            }

            return Collections.emptyList();
        } else {
            Vec3d vec3d = ((EntitySelectorAccessor) selector).getPositionOffset().apply(context.getSource().getPlayer()
                    .getPos());
            Predicate<Entity> predicate = ((EntitySelectorAccessor) selector).getPositionPredicate(vec3d);

            if (selector.isSenderOnly()) {
                ClientPlayerEntity player = context.getSource().getPlayer();

                return predicate.test(player) ? Collections.singletonList(player) : Collections.emptyList();
            } else {
                List<AbstractClientPlayerEntity> list;

                if (selector.isLocalWorldOnly()) {
                    list = getPlayers(context.getSource().getWorld(), predicate);
                } else {
                    list = Lists.newArrayList();

                    for (AbstractClientPlayerEntity player : context.getSource().getWorld().getPlayers()) {
                        if (predicate.test(player)) {
                            list.add(player);
                        }
                    }
                }

                return ((EntitySelectorAccessor) selector).getEntities(vec3d, list);
            }
        }
    }

    public static AbstractClientPlayerEntity getPlayer(@NotNull final CommandContext<FabricClientCommandSource> context,
                                                       @NotNull final String name) throws CommandSyntaxException {
        List<? extends PlayerEntity> list = getPlayers(context, name);

        if (list.size() != 1) {
            throw EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION.create();
        } else {
            return (AbstractClientPlayerEntity) list.get(0);
        }
    }

    public static List<AbstractClientPlayerEntity> getPlayers(@NotNull final ClientWorld world,
                                              final @NotNull Predicate<? super AbstractClientPlayerEntity> predicate) {
        List<AbstractClientPlayerEntity> list = new ArrayList<>();

        for (AbstractClientPlayerEntity player : world.getPlayers()) {
            if (predicate.test(player)) {
                list.add(player);
            }
        }

        return list;
    }

    @Nullable
    public static Entity getEntityByUUID(@NotNull final World world, final UUID uuid) {
        for (Entity entity : world.getOtherEntities(null, GeometryUtil.UNBOUND_BOX,
                (e) -> e.getUuid().equals(uuid))) {
            return entity;
        }

        return null;
    }

    public static Collection<GameProfile> getGameProfiles(@NotNull final CommandContext<FabricClientCommandSource>
                                                                  context, @NotNull final String name)
            throws CommandSyntaxException {
        return context.getArgument(name, GameProfileArgumentType.GameProfileArgument.class)
                .getNames((ServerCommandSource)context.getSource());
    }

    public static PosArgument getPosArgument(CommandContext<FabricClientCommandSource> context, String name) {
        return context.getArgument(name, PosArgument.class);
    }

    public static Vec3d toAbsolutePos(PosArgument argument, FabricClientCommandSource source) {
        if (argument instanceof LookingPosArgument) {
            double x = ((ILookingPosArgument) argument).getX();
            double y = ((ILookingPosArgument) argument).getY();
            double z = ((ILookingPosArgument) argument).getZ();
            Vec2f vec2f = source.getRotation();
            Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(source.getEntity());
            float f = MathHelper.cos((vec2f.y + 90.0f) * ((float) Math.PI / 180));
            float g = MathHelper.sin((vec2f.y + 90.0f) * ((float) Math.PI / 180));
            float h = MathHelper.cos(-vec2f.x * ((float) Math.PI / 180));
            float i = MathHelper.sin(-vec2f.x * ((float) Math.PI / 180));
            float j = MathHelper.cos((-vec2f.x + 90.0f) * ((float) Math.PI / 180));
            float k = MathHelper.sin((-vec2f.x + 90.0f) * ((float) Math.PI / 180));
            Vec3d vec3d2 = new Vec3d(f * h, i, g * h);
            Vec3d vec3d3 = new Vec3d(f * j, k, g * j);
            Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0);
            double d = vec3d2.x * z + vec3d3.x * y + vec3d4.x * x;
            double e = vec3d2.y * z + vec3d3.y * y + vec3d4.y * x;
            double l = vec3d2.z * z + vec3d3.z * y + vec3d4.z * x;
            return new Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
        }

        if (argument instanceof DefaultPosArgument) {
            CoordinateArgument x = ((IDefaultPosArgument) argument).getX();
            CoordinateArgument y = ((IDefaultPosArgument) argument).getY();
            CoordinateArgument z = ((IDefaultPosArgument) argument).getZ();
            Vec3d pos = source.getPosition();

            return new Vec3d(x.toAbsoluteCoordinate(pos.getX()), y.toAbsoluteCoordinate(pos.getY()),
                    z.toAbsoluteCoordinate(pos.getZ()));
        }

        throw new IllegalStateException("unknown PosArgument: " + argument.getClass() + " " + argument);
    }

    public static Vec2f toAbsoluteRotation(PosArgument argument, FabricClientCommandSource source) {
        if (argument instanceof LookingPosArgument) {
            return Vec2f.ZERO;
        }

        if (argument instanceof DefaultPosArgument) {
            CoordinateArgument x = ((IDefaultPosArgument) argument).getX();
            CoordinateArgument y = ((IDefaultPosArgument) argument).getY();
            Vec2f rot = source.getRotation();

            return new Vec2f((float) x.toAbsoluteCoordinate(rot.x), (float) y.toAbsoluteCoordinate(rot.y));
        }

        throw new IllegalStateException("unknown PosArgument: " + argument.getClass() + " " + argument);
    }

    public static BlockPos toAbsoluteBlockPos(PosArgument argument, FabricClientCommandSource source) {
        Vec3d absolutePos = toAbsolutePos(argument, source);
        return new BlockPos((int) absolutePos.x, (int) absolutePos.y, (int) absolutePos.z);
    }
}
