package net.mindoth.runicitems.item;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.WandContainer;
import net.mindoth.runicitems.inventory.WandManager;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class BasicWandItem extends WandItem {
    public BasicWandItem(WandType tier) {
        super(tier);
    }

    @SubscribeEvent
    public static void wandUse(PlayerInteractEvent.RightClickItem event) {
        if ( !event.getEntity().level.isClientSide ) {
            ItemStack stack = event.getItemStack();
            Player player = event.getEntity();
            if ( player.isShiftKeyDown() ) {
                if ( stack.getItem() instanceof WandItem ) {
                    if ( WandManager.get().getCapability(stack).isPresent() ) {
                        IItemHandler itemHandler = WandManager.get().getCapability(stack).resolve().get();
                        Item slot0 = itemHandler.getStackInSlot(0).getItem();
                        Item slot1 = itemHandler.getStackInSlot(1).getItem();
                        Item slot2 = itemHandler.getStackInSlot(2).getItem();
                        Item slot3 = itemHandler.getStackInSlot(3).getItem();
                        Item slot4 = itemHandler.getStackInSlot(4).getItem();
                        Item slot5 = itemHandler.getStackInSlot(5).getItem();
                        Item slot6 = itemHandler.getStackInSlot(6).getItem();
                        Item slot7 = itemHandler.getStackInSlot(7).getItem();
                        Item slot8 = itemHandler.getStackInSlot(8).getItem();
                    }
                }
            }
        }
    }
}
