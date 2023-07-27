package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.item.Item;

import java.util.HashMap;

import static java.lang.Math.max;

public class PowerCalculation {

    public static Integer getPower(HashMap<Item, Integer> effects) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.AMPLIFICATION_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.AMPLIFICATION_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.AMPLIFICATION_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.NULLIFICATION_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.NULLIFICATION_RUNE.get()) != null ) {
                power -= effects.get(RunicItemsItems.NULLIFICATION_RUNE.get());
            }
        }
        power = max(power, 0);
        return power;
    }
}
