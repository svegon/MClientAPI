package io.github.svegon.capi.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public final class ItemUtil {
    private ItemUtil() {
        throw new UnsupportedOperationException();
    }

    public static final Predicate<Slot> PERMANENT_SLOT = (slot) -> slot.getClass() == Slot.class;
    public static final Predicate<Slot> ITEM_CONTAINER_SLOT = (slot) -> !(slot instanceof FurnaceOutputSlot
            || slot instanceof CraftingResultSlot || slot instanceof TradeOutputSlot);

    public static int getSlotMatching(@NotNull final Inventory inv,
                                      @NotNull final Predicate<ItemStack> itemPredicate) {
        for (int i = 0; i < inv.size(); i++) {
            if (itemPredicate.test(inv.getStack(i)))
                return i;
        }

        return -1;
    }

    public static int getSlotWithItem(@NotNull final Inventory inv, @NotNull final Item item) {
        return getSlotMatching(inv, (stack) -> !stack.isEmpty() && stack.isOf(item));
    }

    public static int getSlotMatching(@NotNull final ScreenHandler handler, @NotNull final Predicate<Slot>
            slotPredicate, @NotNull final Predicate<ItemStack> itemPredicate) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);

            if (slotPredicate.test(slot)) {
                ItemStack stack = slot.getStack();

                if (itemPredicate.test(stack)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int getSlotMatching(@NotNull final ScreenHandler handler,
                                      @NotNull final Predicate<ItemStack> itemPredicate) {
        return getSlotMatching(handler, ITEM_CONTAINER_SLOT, itemPredicate);
    }

    public static int getSlotMatching(@NotNull final ScreenHandler handler, @NotNull final Predicate<Slot>
            slotPredicate, final @NotNull Item item) {
        return getSlotMatching(handler, slotPredicate, (stack) -> !stack.isEmpty() && stack.isOf(item));
    }

    public static int getSlotWithItem(@NotNull final ScreenHandler handler, final @NotNull Item item) {
        return getSlotMatching(handler, ITEM_CONTAINER_SLOT, item);
    }

    public static int getSlotWithAny(@NotNull final Inventory inv, @NotNull final Collection<Item> items) {
        return getSlotMatching(inv, (stack) -> !stack.isEmpty() && items.contains(stack.getItem()));
    }

    public static int getSlotWithAny(@NotNull final ScreenHandler handler, @NotNull final Collection<Item> items) {
        return getSlotMatching(handler, (stack) -> !stack.isEmpty() && items.contains(stack.getItem()));
    }

    public static int getEmptySlot(@NotNull final ScreenHandler handler) {
        return getSlotMatching(handler, ItemStack::isEmpty);
    }

    /**
     * taken from Item.raycast
     *
     * @param world
     * @param player
     * @param fluidHandling
     * @return
     */
    public static BlockHitResult raycast(@NotNull final World world, @NotNull final PlayerEntity player,
                                         @NotNull final RaycastContext.FluidHandling fluidHandling) {
        float f = player.getPitch();
        float g = player.getYaw();
        Vec3d vec3d = player.getCameraPosVec(1.0F);
        float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
        float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
        float j = -MathHelper.cos(-f * 0.017453292F);
        float k = MathHelper.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        Vec3d vec3d2 = vec3d.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
        return world.raycast(new RaycastContext(vec3d, vec3d2,
                RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
    }

    public static int countItems(@NotNull final Inventory inv, @NotNull final ToIntFunction<ItemStack> stackToCount) {
        int count = 0;

        for (int slot = 0; slot < inv.size(); ++slot) {
            count += stackToCount.applyAsInt(inv.getStack(slot));
        }

        return count;
    }

    public static int countItems(@NotNull final Inventory inv, @NotNull final Item item) {
        return countItems(inv, (stack) -> !stack.isEmpty() && stack.isOf(item) ? stack.getCount() : 0);
    }

    public static List<ItemStack> asList(@NotNull final Inventory inv) {
        return new AbstractList<>() {
            @Override
            public ItemStack get(int index) {
                return inv.getStack(index);
            }

            @Override
            public ItemStack set(int index, ItemStack element) {
                ItemStack result = get(index);
                inv.setStack(index, element);
                return result;
            }

            @Override
            public ItemStack remove(int index) {
                return Inventories.removeStack(this, index);
            }

            @Override
            public int size() {
                return inv.size();
            }

            @Override
            public boolean isEmpty() {
                return inv.isEmpty();
            }
        };
    }

    public static Inventory asInv(@NotNull final ScreenHandler screenHandler) {
        return new Inventory() {
            @Override
            public int size() {
                return screenHandler.slots.size();
            }

            @Override
            public boolean isEmpty() {
                return screenHandler.slots.isEmpty();
            }

            @Override
            public ItemStack getStack(int slot) {
                return screenHandler.getSlot(slot).getStack();
            }

            @Override
            public ItemStack removeStack(int slot, int amount) {
                ItemStack stack = getStack(slot);
                stack.decrement(amount);
                return stack;
            }

            @Override
            public ItemStack removeStack(int slot) {
                if (slot < 0 || slot >= size()) {
                    return ItemStack.EMPTY;
                }

                Slot s = screenHandler.getSlot(slot);
                ItemStack result = s.getStack();
                s.setStack(ItemStack.EMPTY);
                return result;
            }

            @Override
            public void setStack(int slot, ItemStack stack) {
                screenHandler.getSlot(slot).setStack(stack);
            }

            @Override
            public void markDirty() {

            }

            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return screenHandler.canUse(player);
            }

            @Override
            public void clear() {
                screenHandler.slots.forEach(slot -> slot.setStack(ItemStack.EMPTY));
            }
        };
    }

    public static List<ItemStack> asList(@NotNull final ScreenHandler screenHandler) {
        return asList(asInv(screenHandler));
    }
}
