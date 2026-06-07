package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.entity.EntityAttributeBaseValueCallback;
import io.github.svegon.mclientapi.event.entity.EntityAttributeCallback;
import io.github.svegon.mclientapi.event.entity.EntityStatusEffectCallback;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, WaypointTransmitter {
    @Inject(method = "hasEffect", at = @At("RETURN"), cancellable = true)
    private void onHasStatusEffect(Holder<@NotNull MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        EntityStatusEffectCallback.Companion.getEVENT().invoker().hasStatusEffect((LivingEntity) (Object) this, effect, cir);
    }

    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    private void onGetAttributeValue(Holder<@NotNull Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        EntityAttributeCallback.Companion.getEVENT().invoker().interceptAttributeValue((LivingEntity) (Object)
                this, attribute, cir);
    }

    @Inject(method = "getAttributeBaseValue", at = @At("RETURN"), cancellable = true)
    private void onGetAttributeBaseValue(Holder<@NotNull Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        EntityAttributeBaseValueCallback.Companion.getEVENT().invoker().interceptAttributeBaseValue((LivingEntity) (Object)
                this, attribute, cir);
    }

    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }
}
