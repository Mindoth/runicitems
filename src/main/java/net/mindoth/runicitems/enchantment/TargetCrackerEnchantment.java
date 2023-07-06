package net.mindoth.runicitems.enchantment;

import net.mindoth.runicitems.item.MalletItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TargetCrackerEnchantment extends Enchantment {
    public TargetCrackerEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int pEnchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxCost(int pEnchantmentLevel) {
        return 50;
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
        return true;
    }
}