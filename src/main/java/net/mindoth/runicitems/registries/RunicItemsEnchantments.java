package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RunicItems.MOD_ID);

    public static RegistryObject<Enchantment> TARGET_CRACKER =
            ENCHANTMENTS.register("target_cracker",
                    () -> new TargetCrackerEnchantment(Enchantment.Rarity.RARE,
                            EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND));
}
