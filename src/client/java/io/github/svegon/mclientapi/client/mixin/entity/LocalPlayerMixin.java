package io.github.svegon.mclientapi.client.mixin.entity;

import io.github.svegon.mclientapi.client.event.input.CrosshairTargetUpdateCallback;
import io.github.svegon.mclientapi.client.event.input.EntityTargetUpdateCallback;
import io.github.svegon.mclientapi.client.event.network.PlayerPositionSyncEvents;
import io.github.svegon.mclientapi.client.mixinterface.ILocalPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements ILocalPlayer {
    @Shadow @Final private List<AmbientSoundHandler> ambientSoundHandlers;
    @Shadow protected @Final Minecraft minecraft;

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V", shift = At.Shift.AFTER))
    private void afterAbstractTick(CallbackInfo ci) {
        PlayerPositionSyncEvents.INSTANCE.getBEFORE_INPUT_SYNC().invoker().beforeInputSync((LocalPlayer)(Object)this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isPassenger()Z"))
    private void vehicleCheck(CallbackInfo ci) {
        PlayerPositionSyncEvents.INSTANCE.getAFTER_INPUT_SYNC().invoker().afterInputSync((LocalPlayer)(Object)this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;" +
                    "send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 2), cancellable = true)
    private void beforeVehicleMovePacketSend(CallbackInfo callback) {
        PlayerPositionSyncEvents.INSTANCE.getBEFORE_VEHICLE_PACKETS_SEND_EVENT().invoker()
                .beforeVehiclePacketsSend((LocalPlayer) (Object) this, callback);

        // ensure the end of method not being suppressed
        if (callback.isCancelled()) {
            for (AmbientSoundHandler clientPlayerTickable : ambientSoundHandlers) {
                clientPlayerTickable.tick();
            }
        }
    }

    @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
    private void sendMovementPacketHEAD(CallbackInfo callback) {
        PlayerPositionSyncEvents.INSTANCE.getBEFORE_MOVEMENT_PACKETS_SEND_EVENT().invoker()
                .beforePacketsSend((LocalPlayer) (Object) this, callback);
    }
    @Inject(method = "sendPosition", at = @At("RETURN"))
    private void sendMovementPacketRETURN(CallbackInfo callback) {
        PlayerPositionSyncEvents.INSTANCE.getAFTER_MOVEMENT_PACKETS_SEND_EVENT().invoker()
                .afterPacketsSend((LocalPlayer) (Object) this);
    }

    @Inject(method = "raycastHitResult", at = @At("RETURN"), cancellable = true)
    private void onRaycastHitResult(float a, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
        CrosshairTargetUpdateCallback.Companion.getEVENT().invoker().onCrosshairTargetUpdate(minecraft, a, cir);
    }

    @Redirect(method = "pick", target = @Desc(owner = ProjectileUtil.class, value = "getEntityHitResult",
            args={Entity.class, Vec3.class, Vec3.class, AABB.class, Predicate.class, double.class},
            ret= EntityHitResult.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(" +
                    "Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;" +
                    "Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)" +
                    "Lnet/minecraft/world/phys/EntityHitResult;"))
    private static EntityHitResult pickEntity(
            final Entity except, final Vec3 from, final Vec3 to, final AABB box, final Predicate<Entity> matching,
            final double maxValue
    ) {
        return EntityTargetUpdateCallback.Companion.getEVENT().invoker().onEntityTargetUpdate(except, from, to, box,
                matching, maxValue, ProjectileUtil.getEntityHitResult(except, from, to, box, matching, maxValue));
    }
}
