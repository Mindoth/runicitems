package net.mindoth.runicitems.client.keybinds;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.mindoth.runicitems.network.PacketSendSpellbookData;
import net.mindoth.runicitems.network.RunicItemsNetwork;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RunicItems.MOD_ID)
public class KeyHandler {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    @SubscribeEvent
    public static void mouseEvent(final InputEvent.MouseInputEvent event) {
        if ( MINECRAFT.player == null || MINECRAFT.screen != null || event.getAction() != 1 ) return;
        checkKeysPressed(event.getButton());
    }

    @SubscribeEvent
    public static void keyEvent(final InputEvent.KeyInputEvent event) {
        if ( MINECRAFT.player == null || MINECRAFT.screen != null || event.getAction() != 1 ) return;
        checkKeysPressed(event.getKey());
    }

    public static void checkKeysPressed(int key) {
        if ( key == RunicItemsKeyBinds.spellSelector.getKey().getValue() ) {
            RunicItemsNetwork.sendToServer(new PacketSendSpellbookData());
        }
    }
}
