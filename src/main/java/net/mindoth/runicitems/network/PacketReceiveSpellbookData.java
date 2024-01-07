package net.mindoth.runicitems.network;

import com.google.common.collect.Lists;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.mindoth.runicitems.client.keybinds.KeyHandler;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketReceiveSpellbookData {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();

    public PacketReceiveSpellbookData(List<ItemStack> oldList) {
        this.size = oldList.size();
        this.oldList = oldList;
    }

    public PacketReceiveSpellbookData(PacketBuffer buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) {
            this.itemList.add(buf.readItem());
        }
    }

    public void encode(PacketBuffer buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) {
            buf.writeItem(itemStack);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> GuiSpellSelector.open(this.itemList));
        contextSupplier.get().setPacketHandled(true);
    }
}
