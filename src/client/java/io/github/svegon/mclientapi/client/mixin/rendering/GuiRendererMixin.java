package io.github.svegon.mclientapi.client.mixin.rendering;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import io.github.svegon.mclientapi.client.event.render.screen.GUIRenderCallback;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin implements AutoCloseable {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(final GpuBufferSlice fogBuffer, CallbackInfo ci) {
        GUIRenderCallback.Companion.getEVENT().invoker().onGUIRender((GuiRenderer) (Object) this, fogBuffer, ci);
    }
}
