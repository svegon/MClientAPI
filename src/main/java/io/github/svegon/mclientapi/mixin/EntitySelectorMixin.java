package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPIEntitySelector;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements MClientAPIEntitySelector {
    @Shadow
    private @Final Function<Vec3d, Vec3d> positionOffset;
    @Shadow
    @Nullable
    private @Final Box box;
    @Shadow
    @Nullable
    private @Final String playerName;
    @Shadow
    @Nullable
    private @Final UUID uuid;
    @Shadow
    private @Final TypeFilter<Entity, ?> entityFilter;

    @Shadow
    private Predicate<Entity> getPositionPredicate(Vec3d pos, @Nullable Box box,
                                                @Nullable FeatureSet enabledFeatures) { throw new AssertionError(); }

    @Shadow
    private <T extends Entity> List<T> getEntities(Vec3d pos, List<T> entities) { throw new AssertionError(); }

    @Shadow @Nullable protected abstract Box getOffsetBox(Vec3d offset);

    @Shadow protected abstract int getAppendLimit();

    @NotNull
    @Override
    public Function<Vec3d, Vec3d> getPositionOffset() {
        return positionOffset;
    }

    @Override
    public @Nullable Box getBox() {
        return box;
    }

    @Override
    public @Nullable String getPlayerName() {
        return playerName;
    }

    @Override
    public @Nullable UUID getUuid() {
        return uuid;
    }

    @NotNull
    @Override
    public TypeFilter<Entity, ?> getEntityFilter() {
        return entityFilter;
    }

    @Override
    public int getEntityAppendLimit() {
        return getAppendLimit();
    }

    @Nullable
    @Override
    public Box mClientAPI$getOffsetBox(@NotNull Vec3d offset) {
        return getOffsetBox(offset);
    }

    @NotNull
    @Override
    public Predicate<Entity> mClientAPI$positionPredicate(@NotNull Vec3d pos, @Nullable Box box,
                                                          @Nullable FeatureSet enabledFeatures) {
        return getPositionPredicate(pos, box, enabledFeatures);
    }

    @NotNull
    @Override
    public <T extends Entity> List<T> mClientAPI$getEntities(@NotNull Vec3d pos, @NotNull List<? extends T> entities) {
        return getEntities(pos, (List<T>) entities);
    }
}
