package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.*;
import net.mindoth.runicitems.item.rune.*;
import net.mindoth.runicitems.item.wand.BasicWandItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicItems.MOD_ID);

    //Runes
    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties()));


    public static final RegistryObject<Item> MAGIC_SPARK_RUNE = ITEMS.register("magic_spark_rune",
            () -> new ProjectileRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> EXPLOSION_RUNE = ITEMS.register("explosion_rune",
            () -> new SpellRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> HEALING_BOLT_RUNE = ITEMS.register("healing_bolt_rune",
            () -> new ProjectileRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> STORMY_CLOUD_RUNE = ITEMS.register("stormy_cloud_rune",
            () -> new FamiliarRuneItem(new Item.Properties()));


    public static final RegistryObject<Item> TRIGGER_RUNE = ITEMS.register("trigger_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DEATH_TRIGGER_RUNE = ITEMS.register("death_trigger_rune",
            () -> new EffectRuneItem(new Item.Properties()));


    public static final RegistryObject<Item> INCREASE_POWER_RUNE = ITEMS.register("increase_power_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_POWER_RUNE = ITEMS.register("decrease_power_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> FIRE_RUNE = ITEMS.register("fire_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> BOUNCE_RUNE = ITEMS.register("bounce_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> GRAVITY_RUNE = ITEMS.register("gravity_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INCREASE_SPEED_RUNE = ITEMS.register("increase_speed_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_SPEED_RUNE = ITEMS.register("decrease_speed_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ICE_RUNE = ITEMS.register("ice_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INCREASE_LIFE_RUNE = ITEMS.register("increase_life_rune",
            () -> new EffectRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_LIFE_RUNE = ITEMS.register("decrease_life_rune",
            () -> new EffectRuneItem(new Item.Properties()));



    //Wands
    public static final RegistryObject<Item> BASIC_WAND = ITEMS.register("basic_wand",
            () -> new BasicWandItem(WandType.BASIC));



    //Other items
    public static final RegistryObject<Item> MALLET = ITEMS.register("mallet",
            () -> new MalletItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(500)));

    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1)));

    public static final RegistryObject<Item> ARCHER_BOOTS = ITEMS.register("archer_boots",
            () -> new ArcherBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> FIGHTER_BOOTS = ITEMS.register("fighter_boots",
            () -> new FighterBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots",
            () -> new WizardBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> EAGLE_BOOTS = ITEMS.register("eagle_boots",
            () -> new EagleBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> WARRIOR_BOOTS = ITEMS.register("warrior_boots",
            () -> new WarriorBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));

    public static final RegistryObject<Item> SORCERER_BOOTS = ITEMS.register("sorcerer_boots",
            () -> new SorcererBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).durability(0)));
}
