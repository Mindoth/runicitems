package net.mindoth.runicitems.client.gui;

import net.mindoth.runicitems.inventory.WandUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class WandContainerSlot extends SlotItemHandler {
    private final int index;
    public WandContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return super.getMaxStackSize();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return super.mayPlace(stack);
    }

    //bandage till forge PR fixes this
    @Override
    public void initialize(ItemStack itemStack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, itemStack);
    }
}
