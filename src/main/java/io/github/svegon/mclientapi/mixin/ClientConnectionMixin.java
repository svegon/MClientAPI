package io.github.svegon.capi.mixin;

import io.github.svegon.capi.ClientAPI;
import io.github.svegon.capi.event.network.PacketReceiveCallback;
import io.github.svegon.capi.event.network.PacketSendCallback;
import io.github.svegon.capi.event.network.S2CLoginPacketListener;
import io.github.svegon.capi.event.network.S2CPlayPacketListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow
    private PacketListener packetListener;

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(" +
                    "Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"))
    @SuppressWarnings("unchecked")
    private void onChannelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        PacketReceiveCallback.EVENT.invoker().onPacketSend((ClientConnection) (Object) this, packet, info);

        if (info.isCancelled()) {
            return;
        }

        try {
            if (packetListener instanceof ClientPlayPacketListener) {
                S2CPlayPacketListener.CLIENT_PACKET_RECEIVED_EVENT.invoker()
                        .apply((Packet<ClientPlayPacketListener>) packet, info);
            } else if (packetListener instanceof ClientLoginPacketListener) {
                S2CLoginPacketListener.CLIENT_PACKET_SENT_EVENT.invoker()
                        .apply((Packet<ClientLoginPacketListener>) packet, info);
            }
        } catch (ClassCastException e) {
            ClientAPI.LOGGER.error("invalid packet class " + packet.getClass().getName() + " doesn't apply to "
                    + packetListener.getClass().getName());
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V",
            at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, @Nullable PacketCallbacks arg, CallbackInfo callback) {
        PacketSendCallback.EVENT.invoker().onPacketSend((ClientConnection) (Object) this, packet, callback);
    }
}
