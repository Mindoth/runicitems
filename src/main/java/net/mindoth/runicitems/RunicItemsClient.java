package net.mindoth.runicitems;

import net.mindoth.runicitems.item.spellbook.gui.SpellBookGui;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellRenderer;
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
        ScreenManager.register(RunicItemsContainers.SPELLBOOK_CONTAINER.get(), SpellBookGui::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.PROJECTILE.get(), AbstractSpellRenderer::new);
    }
}
