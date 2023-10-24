package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.input.MousePosListener;
import net.minecraft.client.Mouse;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow
    private double x;
    @Shadow
    private double y;
    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;

    @Inject(method = "onCursorPos", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/Mouse;x:D", ordinal = 1))
    private void onMouseCursorPosMixin(long window, double x, double y, CallbackInfo callback) {
        MousePosListener.EVENT.invoker().onMousePos(this.x, this.y, x, y, cursorDeltaX, cursorDeltaY);
    }
}
