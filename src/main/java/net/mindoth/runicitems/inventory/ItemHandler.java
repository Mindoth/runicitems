package net.mindoth.runicitems.inventory;

import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandler extends ItemStackHandler {
    public ItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        WandManager.get().setDirty();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return ( stack.getItem() instanceof RuneItem && stack.getItem() != RunicItemsItems.EMPTY_RUNE.get() && this.getStackInSlot(slot).isEmpty() );
    }
}
