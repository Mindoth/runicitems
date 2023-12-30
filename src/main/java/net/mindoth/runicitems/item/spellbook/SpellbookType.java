package net.mindoth.runicitems.item.spellbook;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public enum SpellbookType {
    SPELLBOOK("spell_book", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, RunicItemsItems.SPELLBOOK)/*,
    TIER1("Tier1", Rarity.COMMON, 3, 61, 1, 3, "tier1_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER1_SPELLBOOK),
    TIER2("Tier2", Rarity.COMMON, 6, 34, 1, 6, "tier2_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER2_SPELLBOOK),
    TIER3("Tier3", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER3_SPELLBOOK),
    TIER4("Tier4", Rarity.COMMON, 14, 25, 2, 7, "tier4_gui.png", 176, 150, 7, 67, RunicItemsItems.TIER4_SPELLBOOK),
    TIER5("Tier5", Rarity.COMMON, 18, 7, 2, 9, "tier5_gui.png", 176, 150, 7, 67, RunicItemsItems.TIER5_SPELLBOOK),
    TIER6("Tier6", Rarity.COMMON, 24, 16, 3, 8, "tier6_gui.png", 176, 168, 7, 85, RunicItemsItems.TIER6_SPELLBOOK),
    TIER7("Tier7", Rarity.COMMON, 27, 7, 3, 9, "tier7_gui.png", 176, 168, 7, 85, RunicItemsItems.TIER7_SPELLBOOK)*/;

    public final Rarity rarity;
    public final int slots;
    public final int mySlotXOffset;
    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    public final int slotXOffset;
    public final int slotYOffset;
    public final int slotRows;
    public final int slotCols;
    public final String name;
    public final RegistryObject<Item> item;

    SpellbookType(String name, Rarity rarity, int slots, int mySlotXOffset, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
        this.mySlotXOffset = mySlotXOffset;
        this.slotRows = rows;
        this.slotCols = cols;
        this.texture = new ResourceLocation(RunicItems.MOD_ID, "textures/gui/" + location);
        this.xSize = xSize;
        this.ySize = ySize;
        this.slotXOffset = slotXOffset;
        this.slotYOffset = slotYOffset;
        this.item = itemIn;
    }
}
