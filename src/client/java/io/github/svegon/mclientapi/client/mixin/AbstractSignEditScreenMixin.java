package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.mixinterface.IAbstractSignEditScreen;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin extends Screen implements IAbstractSignEditScreen {
    private AbstractSignEditScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    @Final
    @Mutable
    private SignBlockEntity blockEntity;
    @Shadow
    private SignText text;
    @Shadow
    @Final
    private String[] messages;
    @Shadow
    @Final
    @Mutable
    private boolean front;
    @Shadow
    @Final
    @Mutable
    protected WoodType signType;
    @Shadow
    private int currentRow;
    @Nullable
    private SelectionManager selectionManager;

    @Override
    public @NotNull SignBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void setBlockEntity(@NotNull SignBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public @NotNull SignText getSignText() {
        return text;
    }

    @Override
    public void setSignText(@NotNull SignText text) {
        this.text = text;
    }

    @Override
    public String @NotNull [] getText() {
        return messages;
    }

    @Override
    public void setText(String @NotNull ... text) {
        System.arraycopy(text, 0, messages, 0, Math.min(text.length, messages.length));
    }

    @Override
    public boolean getFront() {
        return front;
    }

    @Override
    public void setFront(boolean front) {
        this.front = front;
    }

    @Override
    public @NotNull WoodType getSignType() {
        return signType;
    }

    @Override
    public void setSignType(@NotNull WoodType signType) {
        this.signType = signType;
    }

    @Override
    public int getCurrentRow() {
        return currentRow;
    }

    @Override
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    @Override
    public @Nullable SelectionManager getSelectionManager() {
        return selectionManager;
    }

    @Override
    public void setSelectionManager(@Nullable SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }
}
