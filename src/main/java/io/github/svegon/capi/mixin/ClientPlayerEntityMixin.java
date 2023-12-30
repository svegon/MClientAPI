package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.network.PlayerPositionSyncEvents;
import io.github.svegon.capi.event.render.UnderwaterVisibilityCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow @Final private List<ClientPlayerTickable> tickables;

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(" +
                    "Lnet/minecraft/network/packet/Packet;)V", ordinal = 0))
    private void onTick(CallbackInfo callback) {
        PlayerPositionSyncEvents.BEFORE_VEHICLE_PACKETS_SEND_EVENT.invoker()
                .beforeVehiclePacketsSend((ClientPlayerEntity) (Object) this, callback);

        // ensure the end of method not being suppressed
        if (callback.isCancelled()) {
            for (ClientPlayerTickable clientPlayerTickable : tickables) {
                clientPlayerTickable.tick();
            }
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPacketHEAD(CallbackInfo callback) {
        PlayerPositionSyncEvents.BEFORE_MOVEMENT_PACKETS_SEND_EVENT.invoker()
                .beforePacketsSend((ClientPlayerEntity) (Object) this, callback);
    }
    @Inject(method = "sendMovementPackets", at = @At("RETURN"))
    private void sendMovementPacketRETURN(CallbackInfo callback) {
        PlayerPositionSyncEvents.AFTER_MOVEMENT_PACKETS_SEND_EVENT.invoker()
                .afterPacketsSend((ClientPlayerEntity) (Object) this);
    }

    @Inject(method = "getUnderwaterVisibility", at = @At("HEAD"), cancellable = true)
    private void onGetUnderwaterVisibility(CallbackInfoReturnable<Float> callback) {
        UnderwaterVisibilityCallback.EVENT.invoker()
                .onUnderwaterVisibility((ClientPlayerEntity) (Object) this, callback);
    }
}
