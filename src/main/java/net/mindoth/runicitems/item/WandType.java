package net.mindoth.runicitems.item;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public enum WandType {
    TIER1("Tier1", Rarity.COMMON, 3, 61, 1, 3, "tier1_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER1_WAND),
    TIER2("Tier2", Rarity.COMMON, 5, 43, 1, 5, "tier2_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER2_WAND),
    TIER3("Tier3", Rarity.COMMON, 7, 25, 1, 7, "tier3_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER3_WAND),
    TIER4("Tier4", Rarity.COMMON, 9, 7, 1, 9, "tier4_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER4_WAND);

    public final Rarity rarity;
    public final int slots;
    public final int mySlotXOffset;

    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    //offset from left edge of texture, to left edge of first player inventory slot.
    public final int slotXOffset;
    //offset from left edge of texture, to left edge of first player inventory slot.
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
