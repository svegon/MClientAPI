package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IEntitySelector;
import kotlin.jvm.functions.Function2;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements IEntitySelector {
    @Shadow private @Final Function<Vec3, Vec3> position;
    @Shadow @Nullable private @Final AABB aabb;
    @Shadow private @Final BiConsumer<Vec3, List<? extends Entity>> order;
    @Shadow @Nullable private @Final String playerName;
    @Shadow @Nullable private @Final UUID entityUUID;
    @Shadow private @Final EntityTypeTest<@NotNull Entity, ?> type;
    @Shadow
    private Predicate<Entity> getPredicate(final Vec3 pos, final @Nullable AABB absoluteAabb,
                                           final @Nullable FeatureFlagSet enabledFeatures) {
        throw new AssertionError(); }
    @Shadow @Nullable protected abstract AABB getAbsoluteAabb(Vec3 offset);
    @Shadow protected abstract int getResultLimit();
    @Shadow
    private <T extends Entity> List<T> sortAndLimit(final Vec3 pos, final List<T> result) {
        throw new AssertionError();
    }

    @NotNull
    @Override
    public Function<Vec3, Vec3> getPositionOffset() {
        return position;
    }

    @Override
    public @Nullable AABB getBox() {
        return aabb;
    }

    @Override
    public @Nullable String getPlayerName() {
        return playerName;
    }

    @Override
    public @Nullable UUID getUuid() {
        return entityUUID;
    }

    @Override
    public @NotNull EntityTypeTest<@NotNull Entity, ?> getEntityFilter() {
        return type;
    }

    @Override
    public int getMClientAPI$limit() {
        return getResultLimit();
    }

    @Override
    public @NotNull BiConsumer<Vec3, List<? extends Entity>> getMClientAPI$order() {
        return order;
    }

    @NotNull
    @Override
    public Predicate<Entity> mClientAPI$positionPredicate(@NotNull Vec3 pos, @Nullable AABB AABB,
                                                          @Nullable FeatureFlagSet enabledFeatures) {
        return getPredicate(pos, AABB, enabledFeatures);
    }

    @Override
    public @NotNull List<@NotNull Entity> mClientAPI$sortAndLimit(@NotNull Vec3 pos,
                                                                  @NotNull List<@NotNull Entity> entities) {
        return sortAndLimit(pos, entities);
    }
}
