package io.github.svegon.mclientapi.client.mixin.screen;

import io.github.svegon.mclientapi.client.event.network.ServerConnectCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends Screen {
    @Inject(method = "connect*", at = @At("HEAD"), cancellable = true)
    private static void onConnect(Minecraft minecraft, ServerAddress hostAndPort, ServerData server,
                                  TransferState transferState, CallbackInfo ci) {
        ServerConnectCallback.Companion.getEVENT().invoker().onServerConnect(minecraft, hostAndPort, server,
                transferState, ci);
    }

    private ConnectScreenMixin(Component title) {
        super(title);
    }
}
