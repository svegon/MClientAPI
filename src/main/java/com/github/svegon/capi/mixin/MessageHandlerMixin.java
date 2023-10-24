package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.network.microsoft_validation.MessageStatusCallback;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin {
    @Inject(method = "getStatus", at = @At("RETURN"), cancellable = true)
    private void onGetStatus(SignedMessage message, Text decorated, Instant receptionTimestamp,
                             CallbackInfoReturnable<MessageTrustStatus> callback) {
        MessageStatusCallback.EVENT.invoker().onGetMessageStatus(message, decorated, receptionTimestamp, callback);
    }
}
