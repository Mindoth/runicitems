package net.mindoth.runicitems.item;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public enum WandType {
    TIER1("Tier1", Rarity.COMMON, 3, 1, 9, "tier1_gui.png", 176, 132, 7, 49, RunicItemsItems.TIER1_WAND),
    TIER2("Tier2", Rarity.COMMON, 18, 2, 9, "tier2_gui.png", 176, 150, 7, 67, RunicItemsItems.TIER2_WAND),
    TIER3("Tier3", Rarity.COMMON, 27, 3, 9, "tier3_gui.png", 176, 168, 7, 85, RunicItemsItems.TIER3_WAND);

    public final Rarity rarity;
    public final int slots;

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

    WandType(String name, Rarity rarity, int slots, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
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
