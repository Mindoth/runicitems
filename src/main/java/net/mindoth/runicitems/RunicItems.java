package net.mindoth.runicitems;

import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.mindoth.runicitems.registries.RunicItemsContainers;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
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
        RunicItemsContainers.CONTAINERS.register(modEventBus);
    }
}
