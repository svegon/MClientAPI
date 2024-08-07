package io.github.svegon.capi.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.CraftingResultSlot
import net.minecraft.screen.slot.FurnaceOutputSlot
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.TradeOutputSlot
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext
import net.minecraft.world.RaycastContext.FluidHandling
import net.minecraft.world.World
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.ToIntFunction

class ItemUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        val PERMANENT_SLOT: Predicate<Slot> = Predicate { slot: Slot -> slot.javaClass == Slot::class.java }
        val ITEM_CONTAINER_SLOT: Predicate<Slot> = Predicate { slot: Slot? ->
            !(slot is FurnaceOutputSlot
                    || slot is CraftingResultSlot || slot is TradeOutputSlot)
        }

        fun getSlotMatching(
            inv: Inventory,
            itemPredicate: Predicate<ItemStack>
        ): Int {
            for (i in 0 until inv.size()) {
                if (itemPredicate.test(inv.getStack(i))) return i
            }

            return -1
        }

        fun getSlotWithItem(inv: Inventory, item: Item): Int {
            return getSlotMatching(inv) { stack: ItemStack -> !stack.isEmpty && stack.isOf(item) }
        }

        fun getSlotMatching(
            handler: ScreenHandler,
            slotPredicate: Predicate<Slot>,
            itemPredicate: Predicate<ItemStack>
        ): Int {
            for (i in handler.slots.indices) {
                val slot = handler.slots[i]

                if (slotPredicate.test(slot)) {
                    val stack = slot.stack

                    if (itemPredicate.test(stack)) {
                        return i
                    }
                }
            }

            return -1
        }

        fun getSlotMatching(
            handler: ScreenHandler,
            itemPredicate: Predicate<ItemStack>
        ): Int {
            return getSlotMatching(handler, ITEM_CONTAINER_SLOT, itemPredicate)
        }

        fun getSlotMatching(handler: ScreenHandler, slotPredicate: Predicate<Slot>, item: Item): Int {
            return getSlotMatching(handler, slotPredicate) { stack: ItemStack -> !stack.isEmpty && stack.isOf(item) }
        }

        fun getSlotWithItem(handler: ScreenHandler, item: Item): Int {
            return getSlotMatching(handler, ITEM_CONTAINER_SLOT, item)
        }

        fun getSlotWithAny(inv: Inventory, items: Collection<Item?>): Int {
            return getSlotMatching(inv) { stack: ItemStack -> !stack.isEmpty && items.contains(stack.item) }
        }

        fun getSlotWithAny(handler: ScreenHandler, items: Collection<Item?>): Int {
            return getSlotMatching(handler) { stack: ItemStack -> !stack.isEmpty && items.contains(stack.item) }
        }

        fun getEmptySlot(handler: ScreenHandler): Int {
            return getSlotMatching(handler) { obj: ItemStack -> obj.isEmpty }
        }

        /**
         * taken from Item.raycast
         *
         * @param world
         * @param player
         * @param fluidHandling
         * @return
         */
        fun raycast(
            world: World, player: PlayerEntity,
            fluidHandling: FluidHandling
        ): BlockHitResult {
            val f = player.pitch
            val g = player.yaw
            val vec3d = player.getCameraPosVec(1.0f)
            val h = MathHelper.cos(-g * 0.017453292f - 3.1415927f)
            val i = MathHelper.sin(-g * 0.017453292f - 3.1415927f)
            val j = -MathHelper.cos(-f * 0.017453292f)
            val k = MathHelper.sin(-f * 0.017453292f)
            val l = i * j
            val n = h * j
            val vec3d2 = vec3d.add(l.toDouble() * 5.0, k.toDouble() * 5.0, n.toDouble() * 5.0)
            return world.raycast(
                RaycastContext(
                    vec3d, vec3d2,
                    RaycastContext.ShapeType.OUTLINE, fluidHandling, player
                )
            )
        }

        fun countItems(inv: Inventory, stackToCount: ToIntFunction<ItemStack>): Int {
            var count = 0

            for (slot in 0 until inv.size()) {
                count += stackToCount.applyAsInt(inv.getStack(slot))
            }

            return count
        }

        fun countItems(inv: Inventory, item: Item): Int {
            return countItems(inv) { stack: ItemStack -> if (!stack.isEmpty && stack.isOf(item)) stack.count else 0 }
        }

        fun asList(inv: Inventory): List<ItemStack> {
            return object : AbstractList<ItemStack>() {
                override fun get(index: Int): ItemStack {
                    return inv.getStack(index)
                }

                override fun set(index: Int, element: ItemStack): ItemStack {
                    val result = get(index)
                    inv.setStack(index, element)
                    return result
                }

                override fun remove(index: Int): ItemStack {
                    return Inventories.removeStack(this, index)
                }

                override fun size(): Int {
                    return inv.size()
                }

                override fun isEmpty(): Boolean {
                    return inv.isEmpty
                }
            }
        }

        fun asInv(screenHandler: ScreenHandler): Inventory {
            return object : Inventory {
                override fun size(): Int {
                    return screenHandler.slots.size
                }

                override fun isEmpty(): Boolean {
                    return screenHandler.slots.isEmpty()
                }

                override fun getStack(slot: Int): ItemStack {
                    return screenHandler.getSlot(slot).stack
                }

                override fun removeStack(slot: Int, amount: Int): ItemStack {
                    val stack = getStack(slot)
                    stack.decrement(amount)
                    return stack
                }

                override fun removeStack(slot: Int): ItemStack {
                    if (slot < 0 || slot >= size()) {
                        return ItemStack.EMPTY
                    }

                    val s = screenHandler.getSlot(slot)
                    val result = s.stack
                    s.stack = ItemStack.EMPTY
                    return result
                }

                override fun setStack(slot: Int, stack: ItemStack) {
                    screenHandler.getSlot(slot).stack = stack
                }

                override fun markDirty() {
                }

                override fun canPlayerUse(player: PlayerEntity): Boolean {
                    return screenHandler.canUse(player)
                }

                override fun clear() {
                    screenHandler.slots.forEach(Consumer { slot: Slot -> slot.stack = ItemStack.EMPTY })
                }
            }
        }

        fun asList(screenHandler: ScreenHandler): List<ItemStack> {
            return asList(asInv(screenHandler))
        }
    }
}
