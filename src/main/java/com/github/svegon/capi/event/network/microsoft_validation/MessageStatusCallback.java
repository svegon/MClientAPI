package com.github.svegon.capi.event.network.microsoft_validation;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

public interface MessageStatusCallback {
    Event<MessageStatusCallback> EVENT = EventFactory.createArrayBacked(MessageStatusCallback.class,
            (message, decorated, receptionTimestamp, callback) -> {},
            listeners -> (message, decorated, senderEntry, callback) -> {
        for (MessageStatusCallback listener : listeners) {
            listener.onGetMessageStatus(message, decorated, senderEntry, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
                    });

    void onGetMessageStatus(SignedMessage message, Text decorated, Instant receptionTimestamp,
                            CallbackInfoReturnable<MessageTrustStatus> callback);
}
