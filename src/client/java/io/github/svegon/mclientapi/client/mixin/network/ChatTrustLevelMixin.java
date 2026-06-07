package io.github.svegon.mclientapi.client.mixin.network;

import io.github.svegon.mclientapi.client.event.network.microsoft_validation.MessageStatusCallback;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(ChatTrustLevel.class)
public abstract class ChatTrustLevelMixin {
    @Inject(method = "evaluate", at = @At("RETURN"), cancellable = true)
    private static void onGetStatus(PlayerChatMessage message, Component decoratedMessage, Instant received,
                                    CallbackInfoReturnable<ChatTrustLevel> cir) {
        MessageStatusCallback.Companion.getEVENT().invoker().onGetMessageStatus(message, decoratedMessage,
                received, cir);
    }
}
