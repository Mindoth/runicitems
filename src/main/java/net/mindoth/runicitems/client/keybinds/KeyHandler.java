package net.mindoth.runicitems.client.keybinds;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RunicItems.MOD_ID)
public class KeyHandler {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static @Nonnull ItemStack getHeldWand(PlayerEntity playerEntity) {
        ItemStack wand = playerEntity.getMainHandItem().getItem() instanceof WandItem ? playerEntity.getMainHandItem() : null;
        return wand == null ? (playerEntity.getOffhandItem().getItem() instanceof WandItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : wand;
    }

    public static void checkKeysPressed(int key) {
        PlayerEntity player = MINECRAFT.player;
        if ( WandItem.bookSlot(player.inventory) >= 0 ) {
            ItemStack spellbook = WandItem.getSpellBook(player);
            ItemStack wand = getHeldWand(player);

            if ( key == RunicItemsKeyBinds.spellSelector.getKey().getValue() ) {
                if ( MINECRAFT.screen instanceof GuiSpellSelector ) {
                    player.closeContainer();
                    return;
                }
                if ( wand.getItem() instanceof WandItem && spellbook.hasTag() && MINECRAFT.screen == null ) {
                    MINECRAFT.setScreen(new GuiSpellSelector(SpellbookItem.getSpellData(spellbook), spellbook.getTag()));
                }
            }
        }
    }

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
}
