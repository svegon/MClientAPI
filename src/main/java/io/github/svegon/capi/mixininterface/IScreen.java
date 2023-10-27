package io.github.svegon.capi.mixininterface;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public interface IScreen {
    <T extends Element & Drawable> T addDrawableElement(T drawableElement);

    <T extends Drawable> T addDrawableOnly(T drawable);

    <T extends Element & Selectable> T addSelectableElement(T child);

    void removeElement(Element child);

    void clearElements();
}
