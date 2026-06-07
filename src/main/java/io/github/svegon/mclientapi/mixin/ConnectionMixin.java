package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.MClientAPI;
import io.github.svegon.mclientapi.event.network.PacketReceiveListener;
import io.github.svegon.mclientapi.event.network.PacketSendListener;
import io.github.svegon.mclientapi.mixininterface.network.IPacketListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin extends SimpleChannelInboundHandler<Packet<?>> {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;genericsFtw(" +
                    "Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V"),
            cancellable = true)
    private void onChannelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        PacketReceiveListener.Companion.getEVENT().invoker().onPacketReceive((Connection) (Object)
                        this, channelHandlerContext, packet, info);
    }

    @Inject(at = @At("HEAD"), method = "genericsFtw", cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener,
                                                                  CallbackInfo ci) {
        if (listener instanceof IPacketListener<?, ?> iPacketListener) {
            try {
                iPacketListener.getPacketReceivedEvent().invoker().intercept(packet, ci);
            } catch (ClassCastException e) {
                MClientAPI.Companion.getLOGGER().warn("class missmatch while intercepting packet " + packet
                        + " listened by " + listener);
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
            at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, @Nullable ChannelFutureListener arg, boolean flush, CallbackInfo callback) {
        PacketSendListener.Companion.getEVENT().invoker().onPacketSend((Connection) (Object) this, packet, callback, flush);
    }
}
