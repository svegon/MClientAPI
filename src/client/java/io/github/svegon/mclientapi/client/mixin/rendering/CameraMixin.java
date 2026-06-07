package io.github.svegon.mclientapi.client.mixin.rendering;

import io.github.svegon.mclientapi.client.event.render.overlay.CameraSubmersionTypeCallback;
import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "getFluidInCamera", at = @At("RETURN"), cancellable = true)
    private void onGetSubmersionType(CallbackInfoReturnable<FogType> callback) {
        CameraSubmersionTypeCallback.Companion.getEVENT().invoker().getCameraSubmersionType((Camera) (Object) this,
                callback);
    }
}
