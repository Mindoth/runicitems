package net.mindoth.runicitems.inventory;

import net.mindoth.runicitems.item.rune.ComponentRuneItem;
import net.mindoth.runicitems.item.rune.ModifierRuneItem;
import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
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
        return ( stack.getItem() instanceof ComponentRuneItem || stack.getItem() instanceof ModifierRuneItem );
    }
}
