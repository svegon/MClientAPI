package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.CameraSubmersionTypeCallback;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    private void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> callback) {
        CameraSubmersionTypeCallback.EVENT.invoker().getCameraSubmersionType((Camera) (Object) this, callback);
    }
}
