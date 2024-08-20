package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.MClientAPI;
import io.github.svegon.mclientapi.event.network.PacketReceiveListener;
import io.github.svegon.mclientapi.event.network.PacketSendListener;
import io.github.svegon.mclientapi.mixininterface.network.MClientAPIPacketListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin extends SimpleChannelInboundHandler<Packet<?>> {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(" +
                    "Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"),
            cancellable = true)
    private void onChannelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        PacketReceiveListener.EVENT.invoker().onPacketReceive((ClientConnection) (Object)
                        this, channelHandlerContext, packet, info);
    }

    @Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener,
                                                                  CallbackInfo ci) {
        if (listener instanceof MClientAPIPacketListener<?, ?> MClientAPIPacketListener) {
            try {
                MClientAPIPacketListener.getPacketReceivedEvent().invoker().intercept(packet, ci);
            } catch (ClassCastException e) {
                MClientAPI.Companion.getLOGGER().warn("class missmatch while intercepting packet " + packet
                        + " listened by " + listener);
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V",
            at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, @Nullable PacketCallbacks arg, boolean flush, CallbackInfo callback) {
        PacketSendListener.EVENT.invoker().onPacketSend((ClientConnection) (Object) this, packet, callback, flush);
    }
}
