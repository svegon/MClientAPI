package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IAbstractSignEditScreen;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
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

    public SignBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void setBlockEntity(SignBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public SignText getSignText() {
        return text;
    }

    public void setSignText(SignText text) {
        this.text = text;
    }

    @Override
    public String[] getText() {
        return messages;
    }

    @Override
    public void setText(String... text) {
        System.arraycopy(text, 0, messages, 0, Math.min(text.length, messages.length));
    }

    @Override
    public boolean front() {
        return front;
    }

    @Override
    public void front(boolean front) {
        this.front = front;
    }

    @Override
    public WoodType getSignType() {
        return signType;
    }

    @Override
    public void setSignType(WoodType signType) {
        this.signType = signType;
    }

    @Override
    public int currentRow() {
        return currentRow;
    }

    @Override
    public void currentRow(int row) {
        currentRow = row;
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
