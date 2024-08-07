package io.github.svegon.capi.mixininterface

import net.minecraft.client.gui.Drawable

interface IScreen {
    fun <T> addDrawableElement(drawableElement: T): T where T : Element?, T : Drawable?

    fun <T : Drawable?> addDrawableOnly(drawable: T): T

    fun <T> addSelectableElement(child: T): T where T : Element?, T : Selectable?

    fun removeElement(child: Element?)

    fun clearElements()
}
