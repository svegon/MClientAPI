package com.github.svegon.capi.mixin;

import com.github.svegon.capi.mixininterface.IKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements Comparable<KeyBinding>, IKeyBinding {
    @Shadow
    private InputUtil.Key boundKey;
    @Shadow
    private int timesPressed;

    @Override
    public InputUtil.Key boundKey() {
        return boundKey;
    }

    @Override
    public void boundKey(InputUtil.Key key) {
        this.boundKey = key;
    }

    @Override
    public int timesPressed() {
        return timesPressed;
    }

    @Override
    public void timesPressed(int count) {
        timesPressed = count;
    }

    @Override
    public boolean isPressedOverride() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), boundKey.getCode());
    }
}
