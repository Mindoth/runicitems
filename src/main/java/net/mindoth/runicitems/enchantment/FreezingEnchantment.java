package net.mindoth.runicitems.enchantment;

import net.mindoth.runicitems.item.MalletItem;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FreezingEnchantment extends Enchantment {
    public FreezingEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
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
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof MalletItem;
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if ( !pAttacker.level.isClientSide ) {
            if ( pTarget instanceof LivingEntity ) {
                LivingEntity target = (LivingEntity)pTarget;
                Item item = pAttacker.getMainHandItem().getItem();
                if ( item == RunicItemsItems.MALLET.get() && pAttacker.getMainHandItem().getEnchantmentLevel(RunicItemsEnchantments.FREEZING.get()) > 0 ) {
                    if ( target.canFreeze() && !pAttacker.isAlliedTo(target) && (!(target instanceof ArmorStand) || !((ArmorStand)target).isMarker()) ) {
                        if ( pAttacker instanceof Player ) {
                            Player source = (Player)pAttacker;
                            target.setTicksFrozen(target.getTicksFrozen() + (Math.round(source.getAttackStrengthScale(0.5f) * 280)));
                        }
                        else {
                            target.setTicksFrozen(target.getTicksFrozen() + 280);
                        }
                    }
                }
            }
        }
    }
}
