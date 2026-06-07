package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.entity.BlockSpeedMultiplierCallback;
import io.github.svegon.mclientapi.event.entity.EntityMoveListener;
import io.github.svegon.mclientapi.event.entity.EntityTeamColorCallback;
import io.github.svegon.mclientapi.event.entity.IsInvisibleToListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void onIsInvisibleTo(Player player, CallbackInfoReturnable<Boolean> info) {
        IsInvisibleToListener.Companion.getEVENT().invoker().invisibleSightCheck((Entity) (Object) this, player, info);
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> callback) {
        EntityTeamColorCallback.Companion.getEVENT().invoker().getEntityTeamColor((Entity) (Object) this, callback);
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("HEAD"), cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        BlockSpeedMultiplierCallback.Companion.getEVENT().invoker()
                .getEntityHorizontalSpeedMultiplier((Entity) (Object) this, cir);
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(MoverType moverType, Vec3 movement, CallbackInfo ci) {
        EntityMoveListener.Companion.getEVENT().invoker().moveEntity((Entity) (Object) this, moverType, movement, ci);
    }
}
