package io.github.svegon.capi.mixin;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public interface EntitySelectorAccessor {
    @Accessor
    Function<Vec3d, Vec3d> getPositionOffset();

    @Accessor
    @Nullable Box getBox();

    @Accessor
    @Nullable String getPlayerName();

    @Accessor
    @Nullable UUID getUuid();

    @Accessor
    TypeFilter<Entity, ?> getEntityFilter();

    @Invoker(value = "getPositionPredicate")
    Predicate<Entity> getPositionPredicate(Vec3d pos);

    @Invoker(value = "getEntities")
    <T extends Entity> List<T> getEntities(Vec3d pos, List<T> entities);
}
