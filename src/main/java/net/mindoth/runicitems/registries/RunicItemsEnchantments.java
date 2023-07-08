package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.enchantment.*;
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
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static RegistryObject<Enchantment> TARGET_CRACKER =
            ENCHANTMENTS.register("target_cracker",
                    () -> new TargetCrackerEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static RegistryObject<Enchantment> RADIUS =
            ENCHANTMENTS.register("radius",
                    () -> new RadiusEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

    public static RegistryObject<Enchantment> ICE_BARRAGE =
            ENCHANTMENTS.register("ice_barrage",
                    () -> new IceBarrageEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

    public static RegistryObject<Enchantment> BLOOD_BARRAGE =
            ENCHANTMENTS.register("blood_barrage",
                    () -> new BloodBarrageEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
}
