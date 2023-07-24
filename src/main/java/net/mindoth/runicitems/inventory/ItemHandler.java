package net.mindoth.runicitems.inventory;

import net.minecraftforge.items.ItemStackHandler;

public class ItemHandler extends ItemStackHandler {
    public ItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        WandManager.get().setDirty();
    }

    /*@Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return WandUtils.filterItem(stack);
    }*/
}
