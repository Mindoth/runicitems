package net.mindoth.runicitems;

import net.mindoth.runicitems.client.keybinds.RunicItemsKeyBinds;
import net.mindoth.runicitems.client.gui.GuiSpellbook;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.blizzard.BlizzardRenderer;
import net.mindoth.runicitems.spell.fireball.FireballRenderer;
import net.mindoth.runicitems.spell.tornado.TornadoRenderer;
import net.mindoth.runicitems.spell.waterbolt.WaterBoltRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
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
        ScreenManager.register(RunicItemsContainers.SPELLBOOK_CONTAINER.get(), GuiSpellbook::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.BLIZZARD.get(), BlizzardRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.FIREBALL.get(), FireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.TORNADO.get(), TornadoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.SKELETON_MINION.get(), SkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RunicItemsEntities.WATERBOLT.get(), WaterBoltRenderer::new);
    }
}
