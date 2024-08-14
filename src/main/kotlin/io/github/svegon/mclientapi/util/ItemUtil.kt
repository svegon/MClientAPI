package io.github.svegon.mclientapi.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
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
import net.minecraft.world.RaycastContext
import net.minecraft.world.RaycastContext.FluidHandling
import net.minecraft.world.RaycastContext.ShapeType
import net.minecraft.world.World
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.ToIntFunction

object ItemUtil {
    val PERMANENT_SLOT = { slot: Slot -> slot::class == Slot::class }
    val OUTPUT_SLOT = { slot: Slot ->
        !(slot is FurnaceOutputSlot || slot is CraftingResultSlot || slot is TradeOutputSlot)
    }
    val ITEM_CONTAINER_SLOT = { slot: Slot -> !OUTPUT_SLOT(slot)}

    fun Inventory.getSlotMatching(
        itemPredicate: (ItemStack) -> Boolean,
    ): Int {
        for (i in 0 until size()) {
            if (itemPredicate(getStack(i))) return i
        }

        return -1
    }

    fun Inventory.getSlotWithItem(item: Item): Int {
        return getSlotMatching { stack: ItemStack -> stack.isOf(item) }
    }

    fun ScreenHandler.getSlotMatching(
        slotPredicate: Predicate<Slot>,
        itemPredicate: Predicate<ItemStack>,
    ): Int {
        for (i in slots.indices) {
            val slot = slots[i]

            if (slotPredicate.test(slot)) {
                val stack = slot.stack

                if (itemPredicate.test(stack)) {
                    return i
                }
            }
        }

        return -1
    }

    fun ScreenHandler.getSlotMatching(
        itemPredicate: Predicate<ItemStack>,
    ): Int {
        return getSlotMatching(ITEM_CONTAINER_SLOT, itemPredicate)
    }

    fun ScreenHandler.getSlotMatching(slotPredicate: Predicate<Slot>, item: Item): Int {
        return getSlotMatching(slotPredicate) { stack: ItemStack -> stack.isOf(item) }
    }

    fun ScreenHandler.getSlotWithItem(item: Item): Int {
        return getSlotMatching(ITEM_CONTAINER_SLOT, item)
    }

    fun Inventory.getSlotWithAny(items: Collection<Item>): Int {
        return getSlotMatching { stack: ItemStack -> items.contains(stack.item) }
    }

    fun ScreenHandler.getSlotWithAny(items: Collection<Item>): Int {
        return getSlotMatching { stack: ItemStack -> items.contains(stack.item) }
    }

    fun ScreenHandler.getEmptySlot(): Int {
        return getSlotMatching { obj: ItemStack -> obj.isEmpty }
    }

    fun Inventory.countItems(stackToCount: ToIntFunction<ItemStack>): Int {
        var count = 0

        for (slot in 0..size()) {
            count += stackToCount.applyAsInt(getStack(slot))
        }

        return count
    }

    fun Inventory.countItems(item: Item): Int {
        return countItems { stack: ItemStack -> if (!stack.isEmpty && stack.isOf(item)) stack.count else 0 }
    }

    fun PlayerInventory.findHotbarSlotMatching(itemPredicate: (ItemStack) -> Boolean): Int {
        for (i in 0..<PlayerInventory.getHotbarSize()) {
            if (itemPredicate(main[i])) {
                return i
            }
        }

        return -1
    }

    fun PlayerInventory.findHotbarSlotWith(item: Item): Int {
        return findHotbarSlotMatching { stack: ItemStack -> stack.isOf(item) }
    }

    fun PlayerInventory.findHotbarSlotWith(items: Collection<Item>): Int {
        return findHotbarSlot { item: Item -> items.contains(item) }
    }

    fun PlayerInventory.findHotbarSlot(itemPredicate: (Item) -> Boolean): Int {
        return findHotbarSlotMatching { stack: ItemStack -> itemPredicate(stack.item) }
    }

    fun PlayerInventory.getEmptyHotbarSlot(): Int {
        return findHotbarSlotMatching { stack: ItemStack -> stack.isEmpty }
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
        fluidHandling: FluidHandling,
    ): BlockHitResult {
        val vec3d = player.eyePos
        val vec3d2 = vec3d.add(player.getRotationVector(player.pitch, player.yaw)
            .multiply(player.blockInteractionRange))
        return world.raycast(RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, fluidHandling, player))
    }

    fun Inventory.asList(): MutableList<ItemStack> {
        val inv = this
        return object : AbstractMutableList<ItemStack>() {
            override fun get(index: Int): ItemStack {
                return getStack(index)
            }

            override val size: Int
                get() = size()

            override fun add(index: Int, element: ItemStack) {
                throw UnsupportedOperationException()
            }

            override fun set(index: Int, element: ItemStack): ItemStack {
                val result = get(index)
                setStack(index, element)
                return result
            }

            override fun isEmpty(): Boolean {
                return inv.isEmpty
            }

            override fun removeAt(index: Int): ItemStack {
                return Inventories.removeStack(this, index)
            }
        }
    }

    fun ScreenHandler.asInv(): Inventory {
        return object : Inventory {
            override fun size(): Int {
                return slots.size
            }

            override fun isEmpty(): Boolean {
                return slots.isEmpty()
            }

            override fun getStack(slot: Int): ItemStack {
                return getSlot(slot).stack
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

                val s = getSlot(slot)
                val result = s.stack
                s.stack = ItemStack.EMPTY
                return result
            }

            override fun setStack(slot: Int, stack: ItemStack) {
                getSlot(slot).stack = stack
            }

            override fun markDirty() {
            }

            override fun canPlayerUse(player: PlayerEntity): Boolean {
                return canUse(player)
            }

            override fun clear() {
                slots.forEach(Consumer { slot: Slot -> slot.stack = ItemStack.EMPTY })
            }
        }
    }

    fun ScreenHandler.asList(): List<ItemStack> {
        return asInv().asList()
    }
}
