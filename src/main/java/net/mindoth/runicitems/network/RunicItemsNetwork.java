package net.mindoth.runicitems.network;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class RunicItemsNetwork {
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RunicItems.MOD_ID, "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL = net;

        net.messageBuilder(PacketSelectSpellbookSlot.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSelectSpellbookSlot::new)
                .encoder(PacketSelectSpellbookSlot::encode)
                .consumer(PacketSelectSpellbookSlot::handle)
                .add();

        net.messageBuilder(PacketSendSpellbookData.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSendSpellbookData::new)
                .encoder(PacketSendSpellbookData::encode)
                .consumer(PacketSendSpellbookData::handle)
                .add();

        net.messageBuilder(PacketReceiveSpellbookData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketReceiveSpellbookData::new)
                .encoder(PacketReceiveSpellbookData::encode)
                .consumer(PacketReceiveSpellbookData::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    /*public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RunicItems.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, PacketSelectSpellbookSlot.class, PacketSelectSpellbookSlot::encode, PacketSelectSpellbookSlot::new, PacketSelectSpellbookSlot::handle);
        CHANNEL.registerMessage(1, PacketSendSpellbookData.class, PacketSendSpellbookData::encode, PacketSendSpellbookData::new, PacketSendSpellbookData::handle);
        CHANNEL.registerMessage(2, PacketReceiveSpellbookData.class, PacketReceiveSpellbookData::encode, PacketReceiveSpellbookData::new, PacketReceiveSpellbookData::handle);
    }*/
}
