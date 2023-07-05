package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.enchantment.FreezingEnchantment;
import net.mindoth.runicitems.enchantment.TargetCrackerEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RunicItems.MOD_ID);

    public static RegistryObject<Enchantment> FREEZING =
            ENCHANTMENTS.register("freezing",
                    () -> new FreezingEnchantment(Enchantment.Rarity.VERY_RARE,
                            EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND));

    public static RegistryObject<Enchantment> TARGET_CRACKER =
            ENCHANTMENTS.register("target_cracker",
                    () -> new TargetCrackerEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND));
}
