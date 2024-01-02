package net.mindoth.runicitems.client.keybinds;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.gui.GuiSpellSelector;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RunicItems.MOD_ID)
public class KeyHandler {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static @Nonnull ItemStack getHeldSpellbook(PlayerEntity playerEntity) {
        ItemStack spellbook = playerEntity.getMainHandItem().getItem() instanceof SpellbookItem ? playerEntity.getMainHandItem() : null;
        return spellbook == null ? (playerEntity.getOffhandItem().getItem() instanceof SpellbookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : spellbook;
    }

    public static void checkKeysPressed(int key) {
        ItemStack stack = getHeldSpellbook(MINECRAFT.player);

        if ( key == RunicItemsKeyBinds.spellSelector.getKey().getValue() ) {
            if ( MINECRAFT.screen instanceof GuiSpellSelector ) {
                MINECRAFT.player.closeContainer();
                return;
            }
            if ( stack.getItem() instanceof SpellbookItem && stack.hasTag() && MINECRAFT.screen == null ) {
                MINECRAFT.setScreen(new GuiSpellSelector(SpellbookItem.getSpellData(stack)));
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
