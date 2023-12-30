package io.github.svegon.capi.mixininterface;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public interface IClientPlayerInteractionManager {
    void gameMode(GameMode gameMode);

    void previousGameMode(GameMode gameMode);

    BlockPos getCurrentBreakingPos();

    void setCurrentBreakingPos(BlockPos pos);

    ItemStack getSelectedStack();

    void setSelectedStack(ItemStack stack);

    float getCurrentBreakingProgress();

    void setCurrentBreakingProgress(float currentBreakingProgress);

    float getBlockBreakingSoundCooldown();

    void setBlockBreakingSoundCooldown(float cooldown);

    int getBlockBreakingCooldown();

    void setBlockBreakingCooldown(int cooldown);

    void setBreakingBlock(boolean breaking);

    int getLastSelectedSlot();

    void setLastSelectedSlot(int lastSelectedSlot);
}
