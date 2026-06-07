package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry

interface IScreen {
    val `mClientAPI$minecraft`: Minecraft

    fun <T> `mClientAPI$addRenderableWidget`(drawableElement: T): T where T : GuiEventListener, T : Renderable,
                                                                          T : NarratableEntry

    fun <T : Renderable> `mClientAPI$addRenderableOnly`(drawable: T): T

    fun <T> `mClientAPI$addWidget`(child: T): T where T : GuiEventListener, T : NarratableEntry

    fun `mClientAPI$removeWidget`(child: GuiEventListener)

    fun `mClientAPI$clearWidgets`()
}
