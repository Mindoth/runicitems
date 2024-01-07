package net.mindoth.runicitems.network;

import com.google.common.collect.Lists;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.function.Supplier;

public class PacketSendSpellbookData {

    List<ItemStack> itemList = Lists.newArrayList();

    public PacketSendSpellbookData() {
    }

    public PacketSendSpellbookData(PacketBuffer buf) {
    }

    public void encode(PacketBuffer buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayerEntity player = context.getSender();
                if ( SpellbookItem.bookSlot(player.inventory) >= 0 ) {
                    ItemStack spellbook = SpellbookItem.getSpellBook(player);
                    IItemHandler itemHandler = SpellbookItem.getHandler(spellbook);
                    for ( int i = 0; i < itemHandler.getSlots(); i++ ) {
                        this.itemList.add(itemHandler.getStackInSlot(i));
                    }
                    RunicItemsNetwork.sendToPlayer(new PacketReceiveSpellbookData(this.itemList), player);
                }
            }
        });
        return true;
    }
}
