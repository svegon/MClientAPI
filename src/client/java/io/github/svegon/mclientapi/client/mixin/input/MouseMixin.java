package io.github.svegon.mclientapi.client.mixin.input;

import io.github.svegon.mclientapi.client.event.input.MousePosListener;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Shadow private double xpos;
    @Shadow private double ypos;
    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;

    @Inject(method = "onMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isWindowActive()Z"))
    private void onMouseCursorPosMixin(long window, double x, double y, CallbackInfo callback) {
        MousePosListener.Companion.getEVENT().invoker().onMousePos(xpos, ypos, x, y, accumulatedDX, accumulatedDY);
    }
}
