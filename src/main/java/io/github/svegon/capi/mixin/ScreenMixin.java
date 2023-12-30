package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IScreen;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable, IScreen {
    @Shadow(aliases = {"method_37063"})
    protected abstract <T extends Element & Drawable> T addDrawableChild(T drawableElement);

    @Shadow protected abstract <T extends Drawable> T addDrawable(T drawable);

    @Shadow(aliases = {"method_25429"})
    protected abstract <T extends Element & Selectable> T addSelectableChild(T child);

    @Shadow protected abstract void remove(Element child);

    @Shadow protected abstract void clearChildren();

    @Override
    public <T extends Element & Drawable> T addDrawableElement(T drawableElement) {
        return addDrawableChild(drawableElement);
    }

    @Override
    public <T extends Drawable> T addDrawableOnly(T drawable) {
        return addDrawable(drawable);
    }

    @Override
    public <T extends Element & Selectable> T addSelectableElement(T child) {
        return addSelectableChild(child);
    }

    @Override
    public void removeElement(Element child) {
        remove(child);
    }

    @Override
    public void clearElements() {
        clearChildren();
    }
}
