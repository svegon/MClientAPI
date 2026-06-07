package io.github.svegon.mclientapi.client.mixin.screen;

import io.github.svegon.mclientapi.client.mixinterface.IScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable, IScreen {
    @Shadow protected @Final Minecraft minecraft;
    @Shadow protected abstract  <T extends GuiEventListener & Renderable & NarratableEntry> T
    addRenderableWidget(final T widget);
    @Shadow protected abstract  <T extends Renderable> T addRenderableOnly(final T renderable);
    @Shadow protected abstract  <T extends GuiEventListener & NarratableEntry> T addWidget(final T widget);
    @Shadow protected abstract void removeWidget(final GuiEventListener widget);
    @Shadow protected abstract void clearWidgets();

    @Override
    public @NotNull Minecraft getMClientAPI$minecraft() {
        return minecraft;
    }

    @Override
    public <T extends Renderable> @NotNull T mClientAPI$addRenderableOnly(@NotNull T drawable) {
        return addRenderableOnly(drawable);
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry>
    @NotNull T mClientAPI$addRenderableWidget(@NotNull T drawableElement) {
        return addRenderableWidget(drawableElement);
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> @NotNull T mClientAPI$addWidget(@NotNull T child) {
        return addWidget(child);
    }

    @Override
    public void mClientAPI$removeWidget(@NotNull GuiEventListener child) {

    }

    @Override
    public void mClientAPI$clearWidgets() {

    }
}
