package net.mindoth.runicitems.item.spellbook.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SpellBookContainerSlot extends SlotItemHandler {
    private final int index;
    public SpellBookContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return super.mayPlace(stack);
    }

    @Override
    public void initialize(ItemStack itemStack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, itemStack);
    }
}
