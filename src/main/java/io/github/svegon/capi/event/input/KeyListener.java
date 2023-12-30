package io.github.svegon.capi.event.input;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface KeyListener {
    Event<KeyListener> KEY_HANDLE_START = EventFactory.createArrayBacked(KeyListener.class,
            (key, scancode, action, modifiers, info) -> {},
            (listeners) -> ((key, scancode, action, modifiers, info) -> {
                for (KeyListener listener : listeners) {
                    listener.onKeyPress(key, scancode, action, modifiers, info);

                    if (info.isCancelled()) {
                        return;
                    }
                }
            }));
    Event<KeyListener> EVENT = EventFactory.createArrayBacked(KeyListener.class,
            (key, scancode, action, modifiers, info) -> {},
            (listeners) -> ((key, scancode, action, modifiers, info) -> {
                for (KeyListener listener : listeners) {
                    listener.onKeyPress(key, scancode, action, modifiers, info);
                }
            }));

    void onKeyPress(int key, int scancode, int action, int modifiers, CallbackInfo info);
}
