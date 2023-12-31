package net.mindoth.runicitems.item.weapon;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class StaffItem extends Item {
    public StaffItem(Properties pProperties) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
    }

    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
    }

    private int bookSlot(PlayerInventory playerInventory) {
        int bookSlot = -1;
        for ( int i = 0; i < playerInventory.getContainerSize(); i++ ) {
            if ( playerInventory.getItem(i).getItem() instanceof SpellbookItem ) {
                bookSlot = i;
                break;
            }
        }
        return bookSlot;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        if ( bookSlot(player.inventory) >= 0
                && !(player.getMainHandItem().getItem() instanceof SpellbookItem)
                && !(player.getOffhandItem().getItem() instanceof SpellbookItem) ) {
            ItemStack staff = player.getItemInHand(handIn);
            ItemStack spellbook = player.inventory.getItem(bookSlot(player.inventory));
            if ( !level.isClientSide && player instanceof ServerPlayerEntity && staff.getItem() instanceof StaffItem ) {
                SpellBuilder.castFromWand(player, spellbook);
                return ActionResult.success(player.getItemInHand(handIn));
            }
        }
        return ActionResult.pass(player.getItemInHand(handIn));
    }
}
