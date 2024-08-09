package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.entity.EntityAttributeBaseValueCallback;
import io.github.svegon.mclientapi.event.entity.EntityAttributeCallback;
import io.github.svegon.mclientapi.event.entity.EntityStatusEffectCallback;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "hasStatusEffect", at = @At("RETURN"), cancellable = true)
    private void onHasStatusEffect(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        EntityStatusEffectCallback.EVENT.invoker().hasStatusEffect((LivingEntity) (Object) this, effect, cir);
    }

    @Inject(method = "getAttributeValue", at = @At("TAIL"), cancellable = true)
    private void onGetAttributeValue(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        EntityAttributeCallback.EVENT.invoker().interceptAttributeValue((LivingEntity) (Object) this, attribute, cir);
    }

    @Inject(method = "getAttributeBaseValue", at = @At("TAIL"), cancellable = true)
    private void onGetAttributeBaseValue(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        EntityAttributeBaseValueCallback.EVENT.invoker().interceptAttributeBaseValue((LivingEntity) (Object)
                this, attribute, cir);
    }
}
