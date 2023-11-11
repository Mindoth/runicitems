package net.mindoth.runicitems.item.itemgroup;

import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RunicItemsItemGroup extends CreativeModeTab {

    public static final RunicItemsItemGroup RUNIC_ITEMS_TAB = new RunicItemsItemGroup(CreativeModeTab.TABS.length, "runic_items_tab");

    public RunicItemsItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(RunicItemsItems.STONE_TABLET.get());
    }
}
