package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.CameraSubmersionTypeCallback;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
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
