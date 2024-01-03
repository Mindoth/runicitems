package net.mindoth.runicitems.network;

import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PacketSelectSpellbookSlot {

    public CompoundNBT nbt;

    public PacketSelectSpellbookSlot(PacketBuffer buf) {
        nbt = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeNbt(nbt);
    }

    public PacketSelectSpellbookSlot(CompoundNBT tag) {
        this.nbt = tag;
    }

    public static @Nonnull ItemStack getHeldSpellbook(PlayerEntity playerEntity){
        ItemStack spellbook = playerEntity.getMainHandItem().getItem() instanceof SpellbookItem ? playerEntity.getMainHandItem() : null;
        return spellbook == null ? (playerEntity.getOffhandItem().getItem() instanceof SpellbookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : spellbook;
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            ItemStack spellbook = getHeldSpellbook(player);
            if ( spellbook.getItem() instanceof SpellbookItem ) spellbook.setTag(nbt);
        });
        context.setPacketHandled(true);
    }
}
