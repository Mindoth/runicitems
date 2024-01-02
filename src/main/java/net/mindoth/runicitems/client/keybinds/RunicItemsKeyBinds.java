package net.mindoth.runicitems.client.keybinds;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class RunicItemsKeyBinds {
    public static KeyBinding spellSelector;

    public static void register(final FMLClientSetupEvent event) {
        spellSelector = create("spell_selector", KeyEvent.VK_V);

        ClientRegistry.registerKeyBinding(spellSelector);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + RunicItems.MOD_ID + "." + name, key, "key.category." + RunicItems.MOD_ID);
    }
}
