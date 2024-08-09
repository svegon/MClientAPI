package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.ShouldEntityRenderListener;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements SynchronousResourceReloader {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    private <E extends Entity> void onShouldRender(E entity, Frustum frustum, double x, double y, double z,
                                                   CallbackInfoReturnable<Boolean> cir) {
        ShouldEntityRenderListener.EVENT.invoker().shouldRenderEntity((EntityRenderDispatcher) (Object)
                this, entity, frustum, new Vec3d(x, y, z), cir);
    }
}
