package net.mindoth.runicitems.enchantment;

import net.mindoth.runicitems.item.AncientStaffItem;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class IceBarrageEnchantment extends Enchantment {
    public IceBarrageEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
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
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof AncientStaffItem;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.canApplyAtEnchantingTable(this);
    }

    @Override
    public boolean checkCompatibility(Enchantment ench) {
        return super.checkCompatibility(ench) && ench != RunicItemsEnchantments.BLOOD_BARRAGE.get();
    }
}
