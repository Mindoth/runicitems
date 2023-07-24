package net.mindoth.runicitems.client.gui;

import net.mindoth.runicitems.inventory.WandUtils;
import net.mindoth.runicitems.item.WandType;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class WandContainer extends AbstractContainerMenu {
    public final IItemHandler handler;
    private final WandType tier;
    private final UUID uuid;


    public static WandContainer fromNetwork(final int windowId, final Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuidIn = data.readUUID();
        WandType tier = WandType.values()[data.readInt()];
        return new WandContainer(windowId, playerInventory, uuidIn, tier, new ItemStackHandler(tier.slots));
    }

    public WandContainer(final int windowId, final Inventory playerInventory, UUID uuidIn, WandType tierIn, IItemHandler handler) {
        super(RunicItemsContainers.WAND_CONTAINER.get(), windowId);

        this.uuid = uuidIn;
        this.handler = handler;

        this.tier = tierIn;


        addPlayerSlots(playerInventory);
        addMySlots();
    }

    public WandType getTier() {
        return this.tier;
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }



    @Override
    @Nonnull
    public void clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (clickTypeIn == ClickType.SWAP)
            return;
        if (slot >= 0) getSlot(slot).container.setChanged();
        super.clicked(slot, dragType, clickTypeIn, player);
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int originX = this.tier.slotXOffset;
        int originY = this.tier.slotYOffset;

        //Hotbar
        for (int col = 0; col < 9; col++) {
            int x = originX + col * 18;
            int y = originY + 58;
            Optional<UUID> uuidOptional = WandUtils.getUUID(playerInventory.items.get(col));
            boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
            this.addSlot(new LockableSlot(playerInventory, col, x+1, y+1, lockMe));
        }

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = originX + col * 18;
                int y = originY + row * 18;
                int index = (col + row * 9) + 9;
                Optional<UUID> uuidOptional = WandUtils.getUUID(playerInventory.items.get(index));
                boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
                this.addSlot(new LockableSlot(playerInventory, index, x+1, y+1, lockMe));
            }
        }
    }

    private void addMySlots() {
        if (this.handler == null) return;

        int cols = this.tier.slotCols;
        int rows = this.tier.slotRows;

        int slot_index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = 7 + col * 18;
                int y = 17 + row * 18;

                this.addSlot(new WandContainerSlot(this.handler, slot_index, x + 1, y + 1));
                slot_index++;
                if (slot_index >= this.tier.slots)
                    break;
            }
        }

    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            int bagslotcount = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < playerIn.getInventory().items.size()) {
                if (!this.moveItemStackTo(itemstack1, playerIn.getInventory().items.size(), bagslotcount, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, 0, playerIn.getInventory().items.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }
}
