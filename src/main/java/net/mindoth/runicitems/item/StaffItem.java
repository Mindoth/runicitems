package net.mindoth.runicitems.item;

import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class StaffItem extends Item {
    public StaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean flag = this.getSpell(itemstack);

        if ( !flag ) {
            return InteractionResultHolder.fail(itemstack);
        }
        else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public boolean getSpell(ItemStack staff) {
        return staff.getEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get()) > 0
                || staff.getEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get()) > 0;
    }
}
