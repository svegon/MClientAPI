package io.github.svegon.mclientapi.client.event.network.microsoft_validation

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.multiplayer.chat.ChatTrustLevel
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.PlayerChatMessage
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.time.Instant

fun interface MessageStatusCallback {
    fun onGetMessageStatus(
        message: PlayerChatMessage, decorated: Component, receptionTimestamp: Instant,
        callback: CallbackInfoReturnable<ChatTrustLevel>
    )

    companion object {
        val EVENT: Event<MessageStatusCallback> = EventFactory.createArrayBacked(
            MessageStatusCallback::class.java,
            MessageStatusCallback { message: PlayerChatMessage, decorated: Component,
                                    receptionTimestamp: Instant, callback: CallbackInfoReturnable<ChatTrustLevel> -> }
        ) { listeners: Array<MessageStatusCallback> ->
            MessageStatusCallback { message: PlayerChatMessage, decorated: Component, senderEntry: Instant,
                                    callback: CallbackInfoReturnable<ChatTrustLevel> ->
                for (listener in listeners) {
                    listener.onGetMessageStatus(message, decorated, senderEntry, callback)

                    if (callback.isCancelled) {
                        return@MessageStatusCallback
                    }
                }
            }
        }
    }
}
