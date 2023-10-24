package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.entity.EntityStatusEffectCallback;
import com.github.svegon.capi.mixininterface.ILivingEntity;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntity {
    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "hasStatusEffect", at = @At("RETURN"), cancellable = true)
    private void onHasStatusEffect(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        EntityStatusEffectCallback.EVENT.invoker().hasStatusEffect((LivingEntity) (Object) this, effect, cir);
    }
}
