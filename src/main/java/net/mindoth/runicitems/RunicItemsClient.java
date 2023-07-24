package net.mindoth.runicitems;

import net.mindoth.runicitems.client.gui.WandGui;
import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RunicItemsClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(RunicItemsClient::clientSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(RunicItemsClient::registerLayerDefinitions);
        });
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(RunicItemsContainers.WAND_CONTAINER.get(), WandGui::new);
    }


    public static final ModelLayerLocation BOOTS2_LAYER = new ModelLayerLocation(new ResourceLocation("modid", "boots2_armor"), "main");

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BOOTS2_LAYER, Boots2Model::createBodyLayer);
    }
}
