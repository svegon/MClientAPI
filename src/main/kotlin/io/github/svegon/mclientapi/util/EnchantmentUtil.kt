package io.github.svegon.mclientapi.util

import net.minecraft.component.EnchantmentEffectComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.math.random.Random

object EnchantmentUtil {
    private val random = Random.create()

    fun getProtection(enchantment: RegistryEntry<Enchantment>, stack: ItemStack): Float {
        val effects = enchantment.value().effects.get(EnchantmentEffectComponentTypes.DAMAGE_PROTECTION)!!.map {
            component -> component.effect
        }
        var value = 0f

        for (effect in effects) {
            value = effect.apply(EnchantmentHelper.getLevel(enchantment, stack), random, value)
        }

        return value
    }
}