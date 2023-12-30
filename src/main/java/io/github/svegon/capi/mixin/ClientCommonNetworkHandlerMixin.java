package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.network.C2SPlayPacketListener;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("unchecked")
    public void onSendPacket(Packet<?> packet, CallbackInfo info) {
        try {
            C2SPlayPacketListener.CLIENT_PACKET_SENT_EVENT.invoker().apply((Packet<ServerPlayPacketListener>) packet,
                    info);
        } catch (ClassCastException ignored) {

        }
    }
}
