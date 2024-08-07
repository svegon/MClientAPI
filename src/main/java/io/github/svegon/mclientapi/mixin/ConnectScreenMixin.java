package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.network.ServerConnectCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends Screen {
    private ConnectScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "connect(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/MinecraftClient;" +
            "Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Z)V",
            at = @At("HEAD"), cancellable = true)
    private static void onConnect(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info,
                                  boolean quickPlay, CallbackInfo ci) {
        ServerConnectCallback.EVENT.invoker().onServerConnect(screen, client, address, info, quickPlay, ci);
    }
}
