package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.entity.EntityHorizontalSpeedMultiplierCallback;
import io.github.svegon.mclientapi.event.entity.EntityMoveCallback;
import io.github.svegon.mclientapi.event.entity.EntityTeamColorCallback;
import io.github.svegon.mclientapi.event.entity.IsInvisibleToListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void onIsInvisibleTo(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        IsInvisibleToListener.EVENT.invoker().invisibleSightCheck((Entity) (Object) this, player, info);
    }

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> callback) {
        EntityTeamColorCallback.EVENT.invoker().getEntityTeamColor((Entity) (Object) this, callback);
    }

    @Inject(method = "getVelocityMultiplier", at = @At("HEAD"), cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        EntityHorizontalSpeedMultiplierCallback.EVENT.invoker()
                .getEntityHorizontalSpeedMultiplier((Entity) (Object) this, cir);
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        EntityMoveCallback.EVENT.invoker().moveEntity((Entity) (Object) this, movementType, movement, ci);
    }
}
