package io.github.svegon.mclientapi.client.mixin.screen;

import io.github.svegon.mclientapi.client.mixinterface.IAbstractSignEditScreen;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin extends Screen implements IAbstractSignEditScreen {
    @Mutable @Shadow protected @Final SignBlockEntity sign;
    @Shadow private SignText text;
    @Shadow private @Final String[] messages;
    @Mutable @Shadow private @Final boolean isFrontText;
    @Mutable @Shadow protected @Final WoodType woodType;
    @Shadow private int line;
    @Shadow private @Nullable TextFieldHelper signField;

    @Override
    public @NotNull SignBlockEntity getBlockEntity() {
        return sign;
    }

    @Override
    public void setBlockEntity(@NotNull SignBlockEntity blockEntity) {
        this.sign = blockEntity;
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
        return isFrontText;
    }

    @Override
    public void setFront(boolean front) {
        this.isFrontText = front;
    }

    @Override
    public @NotNull WoodType getSignType() {
        return woodType;
    }

    @Override
    public void setSignType(@NotNull WoodType signType) {
        this.woodType = signType;
    }

    @Override
    public int getCurrentRow() {
        return line;
    }

    @Override
    public void setCurrentRow(int currentRow) {
        this.line = currentRow;
    }

    @Override
    public @Nullable TextFieldHelper getSelectionManager() {
        return signField;
    }

    @Override
    public void setSelectionManager(@Nullable TextFieldHelper selectionManager) {
        this.signField = selectionManager;
    }

    private AbstractSignEditScreenMixin(Component title) {
        super(title);
    }
}
