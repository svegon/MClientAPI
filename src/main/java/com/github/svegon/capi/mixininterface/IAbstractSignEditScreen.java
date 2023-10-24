package com.github.svegon.capi.mixininterface;

import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.util.SelectionManager;
import org.jetbrains.annotations.Nullable;

public interface IAbstractSignEditScreen {
    SignBlockEntity getBlockEntity();

    void setBlockEntity(SignBlockEntity blockEntity);

    SignText getSignText();

    void setSignText(SignText text);

    String[] getText();

    void setText(String... text);

    boolean front();

    void front(boolean front);

    WoodType getSignType();

    void setSignType(WoodType signType);

    int currentRow();

    void currentRow(int row);

    @Nullable
    SelectionManager getSelectionManager();

    void setSelectionManager(@Nullable SelectionManager selectionManager);
}
