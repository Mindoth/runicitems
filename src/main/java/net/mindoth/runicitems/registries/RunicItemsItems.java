package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.*;
import net.mindoth.runicitems.item.armor.*;
import net.mindoth.runicitems.item.rune.*;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.spellbook.SpellbookType;
import net.mindoth.runicitems.item.staff.StaffItem;
import net.mindoth.runicitems.spell.icicle.IcicleSpell;
import net.mindoth.runicitems.spell.tornado.TornadoSpell;
import net.mindoth.runicitems.spell.witherskull.WitherSkullSpell;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicItems.MOD_ID);
    
    
    
    //Other items
    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new Item(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1)));
    
    public static final RegistryObject<Item> MALLET = ITEMS.register("mallet",
            () -> new MalletItem(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).durability(500)));

    public static final RegistryObject<Item> ARCHER_BOOTS = ITEMS.register("archer_boots",
            () -> new ArcherBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));

    public static final RegistryObject<Item> FIGHTER_BOOTS = ITEMS.register("fighter_boots",
            () -> new FighterBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));

    public static final RegistryObject<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots",
            () -> new WizardBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));

    public static final RegistryObject<Item> EAGLE_BOOTS = ITEMS.register("eagle_boots",
            () -> new EagleBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));

    public static final RegistryObject<Item> WARRIOR_BOOTS = ITEMS.register("warrior_boots",
            () -> new WarriorBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));

    public static final RegistryObject<Item> SORCERER_BOOTS = ITEMS.register("sorcerer_boots",
            () -> new SorcererBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlot.FEET,
                    new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB)));
    
    
    
    //Wands
    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book",
            () -> new SpellbookItem(SpellbookType.SPELL_BOOK, 0));

    public static final RegistryObject<Item> STAFF = ITEMS.register("staff",
            () -> new StaffItem(new Item.Properties()));
    
    

    //Runes
    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties()));


    public static final RegistryObject<Item> ICICLE_RUNE = ITEMS.register("icicle_rune",
            () -> new SpellRuneItem(new Item.Properties(), new IcicleSpell()));

    public static final RegistryObject<Item> TORNADO_RUNE = ITEMS.register("tornado_rune",
            () -> new SpellRuneItem(new Item.Properties(), new TornadoSpell()));

    public static final RegistryObject<Item> WITHER_SKULL_RUNE = ITEMS.register("wither_skull_rune",
            () -> new SpellRuneItem(new Item.Properties(), new WitherSkullSpell()));


    public static final RegistryObject<Item> DISTANCE_CAST_RUNE = ITEMS.register("distance_cast_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TRIGGER_RUNE = ITEMS.register("trigger_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DEATH_TRIGGER_RUNE = ITEMS.register("death_trigger_rune",
            () -> new ModifierRuneItem(new Item.Properties()));


    public static final RegistryObject<Item> INCREASE_POWER_RUNE = ITEMS.register("increase_power_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_POWER_RUNE = ITEMS.register("decrease_power_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> BOUNCE_RUNE = ITEMS.register("bounce_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> GRAVITY_RUNE = ITEMS.register("gravity_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INCREASE_SPEED_RUNE = ITEMS.register("increase_speed_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_SPEED_RUNE = ITEMS.register("decrease_speed_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INCREASE_LIFE_RUNE = ITEMS.register("increase_life_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_LIFE_RUNE = ITEMS.register("decrease_life_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ENEMY_PIERCING_RUNE = ITEMS.register("enemy_piercing_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOCK_PIERCING_RUNE = ITEMS.register("block_piercing_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> PERMABOUNCE_RUNE = ITEMS.register("permabounce_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> HOMING_RUNE = ITEMS.register("homing_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INCREASE_RANGE_RUNE = ITEMS.register("increase_range_rune",
            () -> new ModifierRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DECREASE_RANGE_RUNE = ITEMS.register("decrease_range_rune",
            () -> new ModifierRuneItem(new Item.Properties()));
}
