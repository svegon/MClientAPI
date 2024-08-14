package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable

interface IScreen {
    val client: MinecraftClient

    fun <T> addDrawableElement(drawableElement: T): T where T : Element, T : Drawable, T : Selectable

    fun <T : Drawable> addDrawableOnly(drawable: T): T

    fun <T> addSelectableElement(child: T): T where T : Element, T : Selectable

    fun removeElement(child: Element)

    fun clearElements()
}
