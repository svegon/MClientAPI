package io.github.svegon.capi.mixininterface;

import net.minecraft.client.util.InputUtil;

public interface IKeyBinding {
    InputUtil.Key boundKey();

    void boundKey(InputUtil.Key key);

    int timesPressed();

    void timesPressed(int count);

    boolean isPressedOverride();
}
