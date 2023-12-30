package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface CameraSubmersionTypeCallback {
    Event<CameraSubmersionTypeCallback> EVENT = EventFactory.createArrayBacked(CameraSubmersionTypeCallback.class,
            (camera, callback) -> {}, listeners -> (camera, callback) -> {
        for (CameraSubmersionTypeCallback listener : listeners) {
            listener.getCameraSubmersionType(camera, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void getCameraSubmersionType(Camera camera, CallbackInfoReturnable<CameraSubmersionType> callback);
}
