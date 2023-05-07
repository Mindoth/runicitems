package net.mindoth.runicitems.enchantment;

import net.mindoth.runicitems.item.weapon.HammerItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Crush extends Enchantment {

   public Crush(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
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
      return 5;
   }

   @Override
   public boolean canEnchant(ItemStack pStack) {
      return pStack.getItem() instanceof HammerItem;
   }

   @Override
   public float getDamageBonus(int pLevel, MobType pCreatureType) {
      return pCreatureType == MobType.UNDEAD ? (float)pLevel * 2.5F : 0.0F;
   }
}
