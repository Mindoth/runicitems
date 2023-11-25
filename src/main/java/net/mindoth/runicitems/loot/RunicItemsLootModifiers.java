package net.mindoth.runicitems.loot;

import com.mojang.serialization.Codec;
import net.mindoth.runicitems.RunicItems;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RunicItems.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ARCHER_BOOTS_FROM_BURIED_TREASURE =
            LOOT_MODIFIER_SERIALIZERS.register("archer_boots_from_buried_treasure", ArcherbootsAdditionModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FIGHTER_BOOTS_FROM_RAVAGER =
            LOOT_MODIFIER_SERIALIZERS.register("fighter_boots_from_ravager", FighterbootsAdditionModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> TABLET_FROM_EVOKER =
            LOOT_MODIFIER_SERIALIZERS.register("tablet_from_evoker", StonetabletAdditionModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
