package net.mindoth.runicitems.network;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class RunicItemsNetwork {
    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RunicItems.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, PacketSelectSpellbookSlot.class, PacketSelectSpellbookSlot::encode, PacketSelectSpellbookSlot::new, PacketSelectSpellbookSlot::handle);
    }
}
