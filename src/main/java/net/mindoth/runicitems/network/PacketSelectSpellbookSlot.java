package net.mindoth.runicitems.network;

import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSelectSpellbookSlot {

    public CompoundNBT nbt;

    public PacketSelectSpellbookSlot(PacketBuffer buf) {
        nbt = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeNbt(nbt);
    }

    public PacketSelectSpellbookSlot(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            ItemStack spellbook = WandItem.getSpellBook(player);
            if ( spellbook.getItem() instanceof SpellbookItem ) spellbook.setTag(nbt);
        });
        context.setPacketHandled(true);
    }
}
