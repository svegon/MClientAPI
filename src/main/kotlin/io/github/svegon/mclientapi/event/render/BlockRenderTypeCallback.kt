package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.BlockRenderTypeCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

interface BlockRenderTypeCallback {
    fun getBlockRenderType(state: BlockState?, callback: CallbackInfoReturnable<BlockRenderType?>?)

    companion object {
        @JvmField
        val EVENT: Event<BlockRenderTypeCallback?> = EventFactory.createArrayBacked(BlockRenderTypeCallback::class.java,
            BlockRenderTypeCallback { state: BlockState?, callback: CallbackInfoReturnable<BlockRenderType?>? -> },
            Function { listeners: Array<BlockRenderTypeCallback?>? ->
                BlockRenderTypeCallback { state: BlockState?, callback: CallbackInfoReturnable<BlockRenderType?>? ->
                    for (listener in listeners) {
                        listener.getBlockRenderType(state, callback)

                        if (callback.isCancelled()) {
                            return@BlockRenderTypeCallback
                        }
                    }
                }
            })
    }
}
