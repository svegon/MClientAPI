package com.github.svegon.capi.event.input;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.hit.HitResult;

import java.util.function.Function;

public interface CrosshairTargetTypeCallback {
    CrosshairTargetTypeCallback EMPTY_LISTENER = HitResult::getType;
    Function<CrosshairTargetTypeCallback[], CrosshairTargetTypeCallback> INVOKER_FACTORY = listeners -> hitResult -> {
        HitResult.Type type = hitResult.getType();

        for (CrosshairTargetTypeCallback listener : listeners) {
            HitResult.Type newType = listener.getCrosshairTargetType(hitResult);

            if (newType != type) {
                return newType;
            }
        }

        return type;
    };

    Event<CrosshairTargetTypeCallback> BLOCK_BREAK = EventFactory.createArrayBacked(CrosshairTargetTypeCallback.class,
            EMPTY_LISTENER, INVOKER_FACTORY);
    Event<CrosshairTargetTypeCallback> ATTACK = EventFactory.createArrayBacked(CrosshairTargetTypeCallback.class,
            EMPTY_LISTENER, INVOKER_FACTORY);
    Event<CrosshairTargetTypeCallback> ITEM_USE = EventFactory.createArrayBacked(CrosshairTargetTypeCallback.class,
            EMPTY_LISTENER, INVOKER_FACTORY);
    Event<CrosshairTargetTypeCallback> ITEM_PICK = EventFactory.createArrayBacked(CrosshairTargetTypeCallback.class,
            EMPTY_LISTENER, INVOKER_FACTORY);

    HitResult.Type getCrosshairTargetType(HitResult hitResult);
}
