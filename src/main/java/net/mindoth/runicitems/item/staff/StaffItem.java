package net.mindoth.runicitems.item.staff;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class StaffItem extends Item {
    public StaffItem(Properties pProperties) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        if ( bookSlot(player.getInventory()) >= 0
                && !(player.getMainHandItem().getItem() instanceof SpellbookItem)
                && !(player.getOffhandItem().getItem() instanceof SpellbookItem) ) {
            ItemStack staff = player.getItemInHand(handIn);
            ItemStack spellbook = player.getInventory().getItem(bookSlot(player.getInventory()));
            if ( !level.isClientSide && player instanceof ServerPlayer && staff.getItem() instanceof StaffItem ) {
                SpellBuilder.castFromWand(player, spellbook);
                return InteractionResultHolder.success(player.getItemInHand(handIn));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(handIn));
    }

    private int bookSlot(Inventory playerInventory) {
        int bookSlot = -1;
        for ( int i = 0; i < playerInventory.getContainerSize(); i++ ) {
            if ( playerInventory.getItem(i).getItem() instanceof SpellbookItem ) {
                bookSlot = i;
                break;
            }
        }
        return bookSlot;
    }
}
