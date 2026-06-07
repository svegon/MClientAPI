package io.github.svegon.mclientapi.client.mixin.input;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.svegon.mclientapi.client.mixinterface.IKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements Comparable<KeyMapping>, IKeyMapping {
    @Shadow protected InputConstants.Key key;
    @Shadow private int clickCount;

    @NotNull
    @Override
    public InputConstants.Key getBoundKey() {
        return key;
    }

    @Override
    public int getTimesPressed() {
        return clickCount;
    }

    @Override
    public void setTimesPressed(int timesPressed) {
        this.clickCount = timesPressed;
    }

    @Override
    public boolean isReallyPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key.getValue());
    }
}
