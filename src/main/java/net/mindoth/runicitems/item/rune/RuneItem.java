package net.mindoth.runicitems.item.rune;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class RuneItem extends Item {
    public RuneItem(Properties pProperties) {
        super(pProperties.tab(CreativeModeTab.TAB_COMBAT));
    }
}
