package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicItems.MOD_ID);

    public static final RegistryObject<Item> MALLET = ITEMS.register("mallet",
            () -> new MalletItem(new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(500)));

    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1)));

    public static final RegistryObject<Item> ARCHER_BOOTS = ITEMS.register("archer_boots",
            () -> new ArcherBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> FIGHTER_BOOTS = ITEMS.register("fighter_boots",
            () -> new FighterBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots",
            () -> new WizardBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> EAGLE_BOOTS = ITEMS.register("eagle_boots",
            () -> new EagleBoots(Boots2Item.MaterialBoots.BOOTS2, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> WARRIOR_BOOTS = ITEMS.register("warrior_boots",
            () -> new WarriorBoots(Boots2Item.MaterialBoots.BOOTS2, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> SORCERER_BOOTS = ITEMS.register("sorcerer_boots",
            () -> new SorcererBoots(Boots2Item.MaterialBoots.BOOTS2, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(0)));
}
