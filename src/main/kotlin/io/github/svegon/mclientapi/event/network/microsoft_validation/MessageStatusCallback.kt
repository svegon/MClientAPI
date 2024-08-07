package io.github.svegon.capi.event.network.microsoft_validation

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.message.MessageTrustStatus
import net.minecraft.text.Text
import java.time.Instant
import java.util.function.Function

interface MessageStatusCallback {
    fun onGetMessageStatus(
        message: SignedMessage?, decorated: Text?, receptionTimestamp: Instant?,
        callback: CallbackInfoReturnable<MessageTrustStatus?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<MessageStatusCallback?> = EventFactory.createArrayBacked<MessageStatusCallback?>(
            MessageStatusCallback::class.java,
            MessageStatusCallback { message: SignedMessage?, decorated: Text?, receptionTimestamp: Instant?, callback: CallbackInfoReturnable<MessageTrustStatus?>? -> },
            Function<Array<MessageStatusCallback?>, MessageStatusCallback?> { listeners: Array<MessageStatusCallback?>? ->
                MessageStatusCallback { message: SignedMessage?, decorated: Text?, senderEntry: Instant?, callback: CallbackInfoReturnable<MessageTrustStatus?>? ->
                    for (listener in listeners) {
                        listener.onGetMessageStatus(message, decorated, senderEntry, callback)

                        if (callback.isCancelled()) {
                            return@MessageStatusCallback
                        }
                    }
                }
            })
    }
}
