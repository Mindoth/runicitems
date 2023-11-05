package net.mindoth.runicitems.item;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public enum WandType {
    TIER1("Tier1", Rarity.COMMON, 3, 61, 1, 3, "tier1_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER1_WAND),
    TIER2("Tier2", Rarity.COMMON, 6, 34, 1, 6, "tier2_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER2_WAND),
    TIER3("Tier3", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER3_WAND),
    TIER4("Tier4", Rarity.COMMON, 14, 25, 2, 7, "tier4_gui.png", 176, 150, 7, 67, RunicItemsItems.TIER4_WAND),
    TIER5("Tier5", Rarity.COMMON, 18, 7, 2, 9, "tier5_gui.png", 176, 150, 7, 67, RunicItemsItems.TIER5_WAND),
    TIER6("Tier6", Rarity.COMMON, 24, 16, 3, 8, "tier6_gui.png", 176, 168, 7, 85, RunicItemsItems.TIER6_WAND),
    TIER7("Tier7", Rarity.COMMON, 27, 7, 3, 9, "tier7_gui.png", 176, 168, 7, 85, RunicItemsItems.TIER7_WAND);

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

    WandType(String name, Rarity rarity, int slots, int mySlotXOffset, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
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
