package io.github.svegon.mclientapi.util

import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource
import kotlin.random.Random

object EnchantmentUtil {
    private val random = Random(System.currentTimeMillis())

    fun getProtection(enchantment: Holder<Enchantment>, stack: ItemStack): Float {
        val random = SingleThreadedRandomSource(System.nanoTime())
        val level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
        var value = 0f

        for (effect in enchantment.value().getEffects(EnchantmentEffectComponents.DAMAGE_PROTECTION)) {
            value = effect.effect().process(level, random, value)
        }

        return value
    }
}