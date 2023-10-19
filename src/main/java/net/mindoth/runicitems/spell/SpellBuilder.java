package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.item.rune.EffectRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

import static java.lang.Math.max;
import static net.mindoth.runicitems.spell.ShootSpell.*;

public class SpellBuilder {

    public static void cast(Player owner, Entity caster, IItemHandler itemHandler, int slot) {
        HashMap<Item, Integer> effects = new HashMap<>();
        for (int i = slot; i < itemHandler.getSlots(); i++ ) {
            Item rune = getRune(itemHandler, i);
            if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                if ( rune instanceof SpellRuneItem ) {
                    doSpell(owner, caster, itemHandler, i, effects);
                    break;
                }
                if ( rune instanceof EffectRuneItem ) {
                    effects.merge(rune, 1, Integer::sum);
                }
            }
        }
    }

    public static void doSpell(Player owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        Item rune = getRune(itemHandler, slot);

        if ( rune == RunicItemsItems.MAGIC_SPARK_RUNE.get() ) {
            shootMagicSpark(owner, caster, itemHandler, slot, effects);
        }
        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            causeExplosion(owner, caster, itemHandler, slot, effects);
        }
        if ( rune == RunicItemsItems.HEALING_BOLT_RUNE.get() ) {
            shootHealingBolt(owner, caster, itemHandler, slot, effects);
        }
    }

    private static Item getRune(IItemHandler itemHandler, int slot) {
        return itemHandler.getStackInSlot(slot).getItem();
    }

    public static int getNextSpellSlot(int i, IItemHandler itemHandler) {
        int nextRune = -1;
        for ( int j = i + 1; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = j;
            }
        }
        return nextRune;
    }

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
        return power;
    }

    public static Float getSpeed(HashMap<Item, Integer> effects, float speed) {
        float power = 0;
        if ( effects.containsKey(RunicItemsItems.INCREASE_SPEED_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.INCREASE_SPEED_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.INCREASE_SPEED_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.DECREASE_SPEED_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.DECREASE_SPEED_RUNE.get()) != null ) {
                power -= effects.get(RunicItemsItems.DECREASE_SPEED_RUNE.get());
            }
        }
        if ( power != 0 ) {
            if ( power > 0 ) {
                speed += power;
            }
            else if ( power < 0  ) {
                speed /= Math.abs(power);
            }
        }
        return speed;
    }

    public static boolean getTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.TRIGGER_RUNE.get());
    }

    public static boolean getFire(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.FIRE_RUNE.get());
    }

    public static boolean getIce(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.ICE_RUNE.get());
    }

    public static boolean getBounce(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.BOUNCE_RUNE.get());
    }

    public static boolean getGravity(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.GRAVITY_RUNE.get());
    }
}
