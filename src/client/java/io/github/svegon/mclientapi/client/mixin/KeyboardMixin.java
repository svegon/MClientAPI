package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.input.KeyCallback;
import io.github.svegon.mclientapi.client.event.input.KeyListener;
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
    private void onKeySTART(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        KeyListener.EVENT.invoker().onKeyPress(key, scancode, action, modifiers, info);
    }

    @Inject(at = @At("TAIL"), method = "onKey")
    private void onKeyTAIL(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        KeyCallback.EVENT.invoker().onKeyPress(key, scancode, action, modifiers);
    }
}
