package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.enchantment.Crush;
import net.mindoth.runicitems.enchantment.Static;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RunicItems.MOD_ID);

    public static RegistryObject<Enchantment> STATIC =
            ENCHANTMENTS.register("static",
                    () -> new Static(Enchantment.Rarity.VERY_RARE,
                            EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND));

    public static RegistryObject<Enchantment> CRUSH =
            ENCHANTMENTS.register("crush",
                    () -> new Crush(Enchantment.Rarity.RARE,
                            EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND));
}
