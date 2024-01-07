package net.mindoth.runicitems.item.spellbook.inventory;

import net.mindoth.runicitems.item.rune.ModifierRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SpellbookItemHandler extends ItemStackHandler {
    public SpellbookItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        SpellbookManager.get().setDirty();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return ( stack.getItem() instanceof SpellRuneItem || stack.getItem() instanceof ModifierRuneItem );
    }
}
