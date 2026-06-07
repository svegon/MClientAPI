package io.github.svegon.mclientapi.client.mixin.input;

import io.github.svegon.mclientapi.client.event.input.KeyCallback;
import io.github.svegon.mclientapi.client.event.input.KeyListener;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Shadow private @Final Minecraft minecraft;

    @Inject(at = @At(value = "FIELD", opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/KeyboardHandler;debugCrashKeyTime:J"), method = "keyPress",
            cancellable = true)
    private void onKeySTART(long handle, int action, KeyEvent event, CallbackInfo ci) {
        KeyListener.Companion.getEVENT().invoker().onKeyPress(minecraft, action, event, ci);
    }

    @Inject(at = @At("TAIL"), method = "keyPress")
    private void onKeyTAIL(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (minecraft.getWindow().handle() == handle) {
            KeyCallback.Companion.getEVENT().invoker().onKeyPress(minecraft, action, event);
        }
    }
}
