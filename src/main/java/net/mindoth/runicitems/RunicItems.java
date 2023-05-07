package net.mindoth.runicitems;

import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
    }

    private void addRegistries(final IEventBus modEventBus) {
        RunicItemsItems.ITEMS.register(modEventBus);
        RunicItemsEnchantments.ENCHANTMENTS.register(modEventBus);
    }
}
