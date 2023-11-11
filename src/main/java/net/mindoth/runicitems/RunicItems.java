package net.mindoth.runicitems;

import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.mindoth.runicitems.entity.minion.BlazeMinionEntity;
import net.mindoth.runicitems.entity.minion.SkeletonMinionEntity;
import net.mindoth.runicitems.loot.RunicItemsLootModifiers;
import net.mindoth.runicitems.particle.GlowFireParticle;
import net.mindoth.runicitems.registries.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(RunicItems.MOD_ID)
public class RunicItems {
    public static final String MOD_ID = "runicitems";

    public RunicItems() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            RunicItemsClient.registerHandlers();
        }
        addRegistries(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RunicItemsCommonConfig.SPEC, "runicitems-common.toml");
    }

    private void addRegistries(final IEventBus modEventBus) {
        RunicItemsItems.ITEMS.register(modEventBus);
        RunicItemsEntities.ENTITIES.register(modEventBus);
        RunicItemsEnchantments.ENCHANTMENTS.register(modEventBus);
        RunicItemsLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        RunicItemsContainers.CONTAINERS.register(modEventBus);
        RunicItemsEffects.MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = RunicItems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventBusEvents {

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(RunicItemsEntities.BLAZE_MINION.get(), BlazeMinionEntity.setAttributes());
            event.put(RunicItemsEntities.SKELETON_MINION.get(), SkeletonMinionEntity.setAttributes());
        }

        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
            Minecraft.getInstance().particleEngine.register(RunicItemsParticles.GLOW_FIRE_PARTICLE.get(), GlowFireParticle.Provider::new);
        }
    }
}
