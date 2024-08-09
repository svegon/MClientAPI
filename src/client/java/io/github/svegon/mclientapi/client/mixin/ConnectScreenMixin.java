package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.network.ServerConnectCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends Screen {
    private ConnectScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "connect*", at = @At("HEAD"), cancellable = true)
    private static void onConnect(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info,
                                  boolean quickPlay, @Nullable CookieStorage cookieStorage, CallbackInfo ci) {
        ServerConnectCallback.EVENT.invoker().onServerConnect(screen, client, address, info, quickPlay, cookieStorage,
                ci);
    }
}
