package net.mindoth.runicitems;

import net.mindoth.runicitems.client.RunicItemsLayers;
import net.mindoth.runicitems.item.spellbook.gui.SpellBookGui;
import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.mindoth.runicitems.client.models.spells.BlockSpellModel;
import net.mindoth.runicitems.client.models.spells.CubeSpellModel;
import net.mindoth.runicitems.client.models.spells.DeafeningBlastModel;
import net.mindoth.runicitems.client.models.spells.IcicleSpellModel;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.mindoth.runicitems.spell.fireprojectile.FireProjectileRenderer;
import net.mindoth.runicitems.spell.deafening_blast.DeafeningBlastRenderer;
import net.mindoth.runicitems.spell.healingbolt.HealingBoltRenderer;
import net.mindoth.runicitems.spell.iceprojectile.IceProjectileRenderer;
import net.mindoth.runicitems.spell.icicle.IcicleRenderer;
import net.mindoth.runicitems.spell.magicalcloud.MagicalCloudRenderer;
import net.mindoth.runicitems.spell.magicspark.MagicSparkRenderer;
import net.mindoth.runicitems.spell.meteor.MeteorRenderer;
import net.mindoth.runicitems.spell.stormprojectile.StormProjectileRenderer;
import net.mindoth.runicitems.spell.stormycloud.StormyCloudRenderer;
import net.mindoth.runicitems.spell.tornado.TornadoRenderer;
import net.mindoth.runicitems.spell.unstablecloud.UnstableCloudRenderer;
import net.mindoth.runicitems.spell.witherskull.WitherSkullRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
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
        modBus.addListener(RunicItemsClient::registerEntityRenderers);
        RunicItemsParticles.PARTICLE_TYPES.register(modBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(RunicItemsClient::registerLayerDefinitions));
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(RunicItemsContainers.WAND_CONTAINER.get(), SpellBookGui::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RunicItemsLayers.BOOTS2_LAYER, Boots2Model::createBodyLayer);
        event.registerLayerDefinition(RunicItemsLayers.METEOR_LAYER, BlockSpellModel::createBodyLayer);
        event.registerLayerDefinition(RunicItemsLayers.WITHER_SKULL_LAYER, CubeSpellModel::createBodyLayer);
        event.registerLayerDefinition(RunicItemsLayers.ICICLE_LAYER, IcicleSpellModel::createBodyLayer);
        event.registerLayerDefinition(RunicItemsLayers.DEAFENING_BLAST_LAYER, DeafeningBlastModel::createBodyLayer);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(RunicItemsEntities.ICE_PROJECTILE.get(), IceProjectileRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.STORM_PROJECTILE.get(), StormProjectileRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.FIRE_PROJECTILE.get(), FireProjectileRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.MAGIC_SPARK.get(), MagicSparkRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.HEALING_BOLT.get(), HealingBoltRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.STORMY_CLOUD.get(), StormyCloudRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.MAGICAL_CLOUD.get(), MagicalCloudRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.METEOR.get(), MeteorRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.WITHER_SKULL.get(), WitherSkullRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.BLAZE_MINION.get(), BlazeRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.SKELETON_MINION.get(), SkeletonRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.TORNADO.get(), TornadoRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.UNSTABLE_CLOUD.get(), UnstableCloudRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.ICICLE.get(), IcicleRenderer::new);
        event.registerEntityRenderer(RunicItemsEntities.DEAFENING_BLAST.get(), DeafeningBlastRenderer::new);
    }
}
