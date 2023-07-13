package net.mindoth.runicitems.enchantment;

import net.mindoth.runicitems.item.MalletItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class TargetCrackerEnchantment extends Enchantment {
    public TargetCrackerEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int pEnchantmentLevel) {
        return 1 + (pEnchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof MalletItem;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
