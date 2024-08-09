package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.mixinterface.IKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements Comparable<KeyBinding>, IKeyBinding {
    @Shadow
    private InputUtil.Key boundKey;
    @Shadow
    private int timesPressed;

    @NotNull
    @Override
    public InputUtil.Key getBoundKey() {
        return boundKey;
    }

    @Override
    public int getTimesPressed() {
        return timesPressed;
    }

    @Override
    public void setTimesPressed(int timesPressed) {
        this.timesPressed = timesPressed;
    }

    @Override
    public boolean isReallyPressed() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), boundKey.getCode());
    }
}
