package io.github.svegon.mclientapi.client.mixin.rendering;

import io.github.svegon.mclientapi.client.event.render.entity.ShouldEntityRenderListener;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements ResourceManagerReloadListener {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    private <E extends Entity> void onShouldRender(E entity, Frustum culler, double camX, double camY, double camZ,
                                                   CallbackInfoReturnable<Boolean> cir) {
        ShouldEntityRenderListener.Companion.getEVENT().invoker().shouldRenderEntity((EntityRenderDispatcher) (Object)
                this, entity, culler, new Vec3(camX, camY, camZ), cir);
    }
}
