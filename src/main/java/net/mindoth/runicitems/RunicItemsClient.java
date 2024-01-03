package net.mindoth.runicitems;

import net.mindoth.runicitems.client.keybinds.RunicItemsKeyBinds;
import net.mindoth.runicitems.client.gui.GuiSpellBook;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.SpellProjectileRenderer;
import net.mindoth.runicitems.spell.tornado.TornadoRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RunicItemsClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(RunicItemsClient::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        RunicItemsKeyBinds.register(event);
        ScreenManager.register(RunicItemsContainers.SPELLBOOK_CONTAINER.get(), GuiSpellBook::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.TORNADO.get(), TornadoRenderer::new);
    }
}
