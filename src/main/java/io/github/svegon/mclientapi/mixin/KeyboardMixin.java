package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.input.KeyListener;
import net.minecraft.client.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(at = @At(value = "FIELD", opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/Keyboard;debugCrashStartTime:J"), method = "onKey", cancellable = true)
    private void onKeyMixin(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        KeyListener.KEY_HANDLE_START.invoker().onKeyPress(key, scancode, action, modifiers, info);
    }

    @Inject(at = @At("TAIL"), method = "onKey")
    private void onKeyTail(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        KeyListener.EVENT.invoker().onKeyPress(key, scancode, action, modifiers, info);
    }
}
