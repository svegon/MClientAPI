package io.github.svegon.mclientapi.util

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.FurnaceResultSlot
import net.minecraft.world.inventory.MerchantResultSlot
import net.minecraft.world.inventory.ResultSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.ToIntFunction

object ItemUtil {
    val PERMANENT_SLOT = { slot: Slot -> slot::class == Slot::class }
    val OUTPUT_SLOT = { slot: Slot ->
        !(slot is FurnaceResultSlot || slot is ResultSlot || slot is MerchantResultSlot)
    }
    val ITEM_CONTAINER_SLOT = { slot: Slot -> !OUTPUT_SLOT(slot)}

    fun Container.getSlotMatching(
        itemPredicate: (ItemStack) -> Boolean,
    ): Int {
        for (i in 0 until containerSize) {
            if (itemPredicate(getItem(i))) return i
        }

        return -1
    }

    fun Container.getSlotWithItem(item: Item): Int {
        return getSlotMatching { stack: ItemStack -> stack.`is`(item) }
    }

    fun AbstractContainerMenu.getSlotMatching(
        slotPredicate: Predicate<Slot>,
        itemPredicate: Predicate<ItemStack>,
    ): Int {
        for (i in slots.indices) {
            val slot = slots[i]

            if (slotPredicate.test(slot)) {
                val stack = slot.item

                if (itemPredicate.test(stack)) {
                    return i
                }
            }
        }

        return -1
    }

    fun AbstractContainerMenu.getSlotMatching(
        itemPredicate: Predicate<ItemStack>,
    ): Int {
        return getSlotMatching(ITEM_CONTAINER_SLOT, itemPredicate)
    }

    fun AbstractContainerMenu.getSlotMatching(slotPredicate: Predicate<Slot>, item: Item): Int {
        return getSlotMatching(slotPredicate) { stack: ItemStack -> stack.`is`(item) }
    }

    fun AbstractContainerMenu.getSlotWithItem(item: Item): Int {
        return getSlotMatching(ITEM_CONTAINER_SLOT, item)
    }

    fun Container.getSlotWithAny(items: Collection<Item>): Int {
        return getSlotMatching { stack: ItemStack -> items.contains(stack.item) }
    }

    fun AbstractContainerMenu.getSlotWithAny(items: Collection<Item>): Int {
        return getSlotMatching { stack: ItemStack -> items.contains(stack.item) }
    }

    fun AbstractContainerMenu.getEmptySlot(): Int {
        return getSlotMatching { obj: ItemStack -> obj.isEmpty }
    }

    fun Container.countItems(stackToCount: ToIntFunction<ItemStack>): Int {
        var count = 0

        for (slot in 0 until containerSize) {
            count += stackToCount.applyAsInt(getItem(slot))
        }

        return count
    }

    fun Container.countItems(item: Item): Int {
        return countItems { stack: ItemStack -> if (!stack.isEmpty && stack.`is`(item)) stack.count else 0 }
    }

    fun Inventory.findHotbarSlotMatching(itemPredicate: (ItemStack) -> Boolean): Int {
        for (i in 0..<Inventory.getSelectionSize()) {
            if (itemPredicate(getItem(i))) {
                return i
            }
        }

        return -1
    }

    fun Inventory.findHotbarSlotWith(item: Item): Int {
        return findHotbarSlotMatching { stack: ItemStack -> stack.`is`(item) }
    }

    fun Inventory.findHotbarSlotWith(items: Collection<Item>): Int {
        return findHotbarSlot { item: Item -> items.contains(item) }
    }

    fun Inventory.findHotbarSlot(itemPredicate: (Item) -> Boolean): Int {
        return findHotbarSlotMatching { stack: ItemStack -> itemPredicate(stack.item) }
    }

    fun Inventory.getEmptyHotbarSlot(): Int {
        return findHotbarSlotMatching { stack: ItemStack -> stack.isEmpty }
    }

    /**
     * taken from Item.raycast
     *
     * @param level
     * @param player
     * @param fluidHandling
     * @return
     */
    fun raycast(
        level: Level, player: Player,
        fluidHandling: ClipContext.Fluid,
    ): BlockHitResult {
        val vec3d = player.eyePosition
        val vec3d2 = vec3d.add(player.calculateViewVector(player.xRot, player.yRot)
            .scale(player.blockInteractionRange()))
        return level.clip(ClipContext(vec3d, vec3d2, ClipContext.Block.OUTLINE, fluidHandling, player))
    }

    fun AbstractContainerMenu.asInv(): Container {
        return object : Container {
            override fun getContainerSize(): Int {
                return slots.size
            }

            override fun isEmpty(): Boolean {
                return slots.isEmpty()
            }

            override fun getItem(slot: Int): ItemStack {
                return getSlot(slot)!!.get();
            }

            override fun removeItem(slot: Int, count: Int): ItemStack {
                val s = getSlot(slot) ?: return ItemStack.EMPTY

                val result = s.get();
                s.set(ItemStack.EMPTY)
                return result
            }

            override fun removeItemNoUpdate(slot: Int): ItemStack {
                val s = getSlot(slot) ?: return ItemStack.EMPTY
                val result = s.get();
                s.set(ItemStack.EMPTY)
                return result
            }

            override fun setItem(slot: Int, itemStack: ItemStack) {
                val s = getSlot(slot) ?: return
                s.set(itemStack)
            }

            override fun setChanged() {}

            override fun stillValid(player: Player): Boolean {
                return true
            }

            override fun clearContent() {
                slots.forEach(Consumer { slot: Slot -> slot.set(ItemStack.EMPTY) })
            }
        }
    }
}
