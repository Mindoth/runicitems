package net.mindoth.runicitems.network;

import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Supplier;

public class PacketUpdateSpellbook {

    ItemStack spellbook;

    public PacketUpdateSpellbook(/*ItemStack spellbook*/) {
        //this.spellbook = spellbook;
    }

    public PacketUpdateSpellbook(PacketBuffer buf) {
        //this.spellbook = buf.readItem();
    }

    public void encode(PacketBuffer buf) {
        //buf.writeItem(this.spellbook);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayerEntity player = context.getSender();
                if ( WandItem.bookSlot(player.inventory) >= 0 ) {
                    ItemStack spellbook = WandItem.getSpellBook(player);
                    RunicItemsNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(context::getSender), new PacketSendSpellbookData(spellbook));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
