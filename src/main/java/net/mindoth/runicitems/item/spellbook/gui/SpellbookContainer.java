package net.mindoth.runicitems.item.spellbook.gui;

import net.mindoth.runicitems.item.spellbook.inventory.SpellbookUtils;
import net.mindoth.runicitems.item.spellbook.SpellbookType;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SpellbookContainer extends Container {
    public final IItemHandler handler;
    private final SpellbookType tier;
    private final UUID uuid;


    public static SpellbookContainer fromNetwork(final int windowId, final PlayerInventory playerInventory, PacketBuffer data) {
        UUID uuidIn = data.readUUID();
        SpellbookType tier = SpellbookType.values()[data.readInt()];
        return new SpellbookContainer(windowId, playerInventory, uuidIn, tier, new ItemStackHandler(tier.slots));
    }

    public SpellbookContainer(final int windowId, final PlayerInventory playerInventory, UUID uuidIn, SpellbookType tierIn, IItemHandler handler) {
        super(RunicItemsContainers.SPELLBOOK_CONTAINER.get(), windowId);

        this.uuid = uuidIn;
        this.handler = handler;

        this.tier = tierIn;


        addPlayerSlots(playerInventory);
        addMySlots();
    }

    public SpellbookType getTier() {
        return this.tier;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity playerIn) {
        return true;
    }



    @Override
    @Nonnull
    public ItemStack clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull PlayerEntity player) {
        if (clickTypeIn == ClickType.SWAP)
            return ItemStack.EMPTY;
        if (slot >= 0) getSlot(slot).container.setChanged();
        return super.clicked(slot, dragType, clickTypeIn, player);
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        int originX = this.tier.slotXOffset;
        int originY = this.tier.slotYOffset;

        //Hotbar
        for ( int col = 0; col < 9; col++ ) {
            int x = originX + col * 18;
            int y = originY + 58;
            Optional<UUID> uuidOptional = SpellbookUtils.getUUID(playerInventory.items.get(col));
            boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
            this.addSlot(new LockableSlot(playerInventory, col, x+1, y+1, lockMe));
        }

        //Player Inventory
        for ( int row = 0; row < 3; row++ ) {
            for ( int col = 0; col < 9; col++ ) {
                int x = originX + col * 18;
                int y = originY + row * 18;
                int index = (col + row * 9) + 9;
                Optional<UUID> uuidOptional = SpellbookUtils.getUUID(playerInventory.items.get(index));
                boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
                this.addSlot(new LockableSlot(playerInventory, index, x+1, y+1, lockMe));
            }
        }
    }

    private void addMySlots() {
        if ( this.handler == null ) return;

        int cols = this.tier.slotCols;
        int rows = this.tier.slotRows;
        int originX = this.tier.mySlotXOffset;

        int slot_index = 0;

        for ( int row = 0; row < rows; row++ ) {
            for ( int col = 0; col < cols; col++ ) {
                int x = originX + col * 18;
                int y = 17 + row * 18;

                this.addSlot(new SpellBookContainerSlot(this.handler, slot_index, x + 1, y + 1));
                slot_index++;
                if (slot_index >= this.tier.slots)
                    break;
            }
        }

    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if ( slot != null && slot.hasItem() ) {
            int spellbookslotcount = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if ( index < playerIn.inventory.items.size() ) {
                if ( !this.moveItemStackTo(itemstack1, playerIn.inventory.items.size(), spellbookslotcount, false) )
                    return ItemStack.EMPTY;
            }
            else if ( !this.moveItemStackTo(itemstack1, 0, playerIn.inventory.items.size(), false) ) {
                return ItemStack.EMPTY;
            }
            if ( itemstack1.isEmpty() ) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }
}