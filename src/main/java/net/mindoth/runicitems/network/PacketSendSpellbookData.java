package net.mindoth.runicitems.network;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendSpellbookData {

    ItemStack spellbook;
    //CompoundNBT nbt;

    public PacketSendSpellbookData(ItemStack spellbook) {
        this.spellbook = spellbook;
        //this.nbt = nbt;
    }

    public PacketSendSpellbookData(PacketBuffer buf) {
        this.spellbook = buf.readItem();
        //this.nbt = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeItem(this.spellbook);
        //buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( RunicItems.proxy.getMinecraft().screen instanceof GuiSpellSelector ) {
                //((GuiSpellSelector)RunicItems.proxy.getMinecraft().screen).itemHandler = SpellbookItem.getSpellbookHandler(this.spellbook);
            }
        });
        context.setPacketHandled(true);
    }
}
