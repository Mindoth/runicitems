package net.mindoth.runicitems.loot;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RunicItemsLootModifiers {
    @SubscribeEvent
    public static void registerLootModifiers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> ev) {
        ev.getRegistry().registerAll(
                new ArcherbootsAdditionModifier.Serializer().setRegistryName
                        (new ResourceLocation(RunicItems.MOD_ID, "archer_boots_from_buried_treasure")),
                new FighterbootsAdditionModifier.Serializer().setRegistryName
                        (new ResourceLocation(RunicItems.MOD_ID, "fighter_boots_from_ravager")),
                new StonetabletAdditionModifier.Serializer().setRegistryName
                        (new ResourceLocation(RunicItems.MOD_ID, "tablet_from_evoker"))
        );
    }
}
