package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.armor.*;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.spellbook.SpellbookType;
import net.mindoth.runicitems.item.weapon.MalletItem;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.mindoth.runicitems.spell.ghostwalk.GhostWalkSpell;
import net.mindoth.runicitems.spell.highalchemyspell.HighAlchemySpell;
import net.mindoth.runicitems.spell.raisedead.RaiseDeadSpell;
import net.mindoth.runicitems.spell.tornado.TornadoSpell;
import net.mindoth.runicitems.spell.waterbolt.WaterBoltSpell;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicItems.MOD_ID);

    //Misc
    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new Item(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1)));


    //Weapons
    public static final RegistryObject<Item> MALLET = ITEMS.register("mallet",
            () -> new MalletItem(new Item.Properties().durability(500)));


    //Boots
    public static final RegistryObject<Item> ARCHER_BOOTS = ITEMS.register("archer_boots",
            () -> new ArcherBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> FIGHTER_BOOTS = ITEMS.register("fighter_boots",
            () -> new FighterBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> WIZARD_BOOTS = ITEMS.register("wizard_boots",
            () -> new WizardBoots(BootsItem.MaterialBoots.BOOTS, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> EAGLE_BOOTS = ITEMS.register("eagle_boots",
            () -> new EagleBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> WARRIOR_BOOTS = ITEMS.register("warrior_boots",
            () -> new WarriorBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> SORCERER_BOOTS = ITEMS.register("sorcerer_boots",
            () -> new SorcererBoots(BootsItem.MaterialBoots.BOOTS2, EquipmentSlotType.FEET, new Item.Properties()));


    //Spellbooks
    public static final RegistryObject<Item> SPELLBOOK = ITEMS.register("spellbook",
            () -> new SpellbookItem(SpellbookType.SPELLBOOK));


    //Wands
    public static final RegistryObject<Item> ANCIENT_STAFF = ITEMS.register("ancient_staff",
            () -> new WandItem(new Item.Properties()));


    //Runes
    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties()));

    public static final RegistryObject<Item> FROST_RUNE = ITEMS.register("frost_rune",
            () -> new RuneItem(new Item.Properties()));

    public static final RegistryObject<Item> STORM_RUNE = ITEMS.register("storm_rune",
            () -> new RuneItem(new Item.Properties()));

    public static final RegistryObject<Item> FIRE_RUNE = ITEMS.register("fire_rune",
            () -> new RuneItem(new Item.Properties()));

    public static final RegistryObject<Item> NATURE_RUNE = ITEMS.register("nature_rune",
            () -> new RuneItem(new Item.Properties()));


    public static final RegistryObject<Item> BLIZZARD_RUNE = ITEMS.register("blizzard_rune",
            () -> new SpellRuneItem(new Item.Properties(), new BlizzardSpell()));

    public static final RegistryObject<Item> TORNADO_RUNE = ITEMS.register("tornado_rune",
            () -> new SpellRuneItem(new Item.Properties(), new TornadoSpell()));

    public static final RegistryObject<Item> FIREBALL_RUNE = ITEMS.register("fireball_rune",
            () -> new SpellRuneItem(new Item.Properties(), new FireballSpell()));

    public static final RegistryObject<Item> GHOST_WALK_RUNE = ITEMS.register("ghost_walk_rune",
            () -> new SpellRuneItem(new Item.Properties(), new GhostWalkSpell()));

    public static final RegistryObject<Item> RAISE_DEAD_RUNE = ITEMS.register("raise_dead_rune",
            () -> new SpellRuneItem(new Item.Properties(), new RaiseDeadSpell()));

    public static final RegistryObject<Item> WATER_BOLT_RUNE = ITEMS.register("water_bolt_rune",
            () -> new SpellRuneItem(new Item.Properties(), new WaterBoltSpell()));

    public static final RegistryObject<Item> HIGH_ALCHEMY_RUNE = ITEMS.register("high_alchemy_rune",
            () -> new SpellRuneItem(new Item.Properties(), new HighAlchemySpell()));
}
