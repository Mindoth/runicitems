package net.mindoth.runicitems.item.rune;

import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.minecraft.world.item.Item;

public class RuneItem extends Item {

    public RuneItem(Properties pProperties) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
    }
}
