package net.mindoth.runicitems.event;

import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.rune.ModifierRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class SpellBuilder {

    public static void castFromWand(Player owner, ItemStack ogStack) {
        SpellbookData ogData = SpellbookItem.getData(ogStack);
        final IItemHandler handler = ogData.getHandler();
        cast(owner, owner, handler, 0, owner.getXRot(), owner.getYRot());
    }

    public static void cast(Player owner, Entity caster, IItemHandler itemHandler, int slot, float xRot, float yRot) {
        HashMap<Item, Integer> effects = new HashMap<>();
        for ( int i = slot; i < itemHandler.getSlots(); i++ ) {
            if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                Item rune = getRune(itemHandler, i);
                if ( rune instanceof ModifierRuneItem ) {
                    effects.merge(rune, 1, Integer::sum);
                }
                else if ( rune instanceof SpellRuneItem ) {
                    doSpell(owner, caster, itemHandler, i, effects, (SpellRuneItem)rune, xRot, yRot);
                    break;
                }
            }
        }
    }

    private static void doSpell(Player owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, SpellRuneItem rune, float xRot, float yRot) {
        final AbstractSpell spell = rune.spell;
        Vec3 center;
        int distance = SpellBuilder.getDistance(effects);
        if ( distance > 0 ) {
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = CommonEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition().add(direction);
        }
        else center = new Vec3(CommonEvents.getEntityCenter(caster).x, CommonEvents.getEntityCenter(caster).y + 0.5F, CommonEvents.getEntityCenter(caster).z);
        AbstractSpell.routeSpell(owner, caster, itemHandler, slot, effects, spell, center, xRot, yRot);
    }

    public static Item getRune(IItemHandler itemHandler, int slot) {
        return itemHandler.getStackInSlot(slot).getItem();
    }

    public static Integer getPower(HashMap<Item, Integer> effects, double basePower) {
        int power = 0;
        basePower += 1;
        if ( effects.containsKey(RunicItemsItems.INCREASE_POWER_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.INCREASE_POWER_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.INCREASE_POWER_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.DECREASE_POWER_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.DECREASE_POWER_RUNE.get()) != null ) {
                power -= effects.get(RunicItemsItems.DECREASE_POWER_RUNE.get());
            }
        }
        if ( power > 0 ) {
            for ( int i = 0; i < power; i++ ) {
                basePower *= 1.5F;
            }
        }
        else if ( power < 0 ) {
            for ( int i = 0; i > power; i-- ) {
                basePower *= 0.5F;
            }
        }
        return (int)basePower;
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
        if ( power > 0 ) {
            for ( int i = 0; i < power; i++ ) {
                speed *= 1.25F;
            }
        }
        else if ( power < 0 ) {
            for ( int i = 0; i > power; i-- ) {
                speed *= 0.5F;
            }
        }
        return speed;
    }

    public static Integer getLife(HashMap<Item, Integer> effects, int life) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.INCREASE_LIFE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.INCREASE_LIFE_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.INCREASE_LIFE_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.DECREASE_LIFE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.DECREASE_LIFE_RUNE.get()) != null ) {
                power -= effects.get(RunicItemsItems.DECREASE_LIFE_RUNE.get());
            }
        }
        if ( power > 0 ) {
            for ( int i = 0; i < power; i++ ) {
                life *= 1.5F;
            }
        }
        else if ( power < 0 ) {
            for ( int i = 0; i > power; i-- ) {
                life *= 0.5F;
            }
        }
        return life;
    }

    public static Integer getRange(HashMap<Item, Integer> effects) {
        int power = 3;
        if ( effects.containsKey(RunicItemsItems.INCREASE_RANGE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.INCREASE_RANGE_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.INCREASE_RANGE_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.DECREASE_RANGE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.DECREASE_RANGE_RUNE.get()) != null ) {
                power -= effects.get(RunicItemsItems.DECREASE_RANGE_RUNE.get());
            }
        }
        return Math.max(power, 1);
    }

    public static Integer getBounce(HashMap<Item, Integer> effects) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.BOUNCE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.BOUNCE_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.BOUNCE_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.PERMABOUNCE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.PERMABOUNCE_RUNE.get()) != null ) {
                power += 99999;
            }
        }
        return power;
    }

    public static int getDistance(HashMap<Item, Integer> effects) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.DISTANCE_CAST_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.DISTANCE_CAST_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.DISTANCE_CAST_RUNE.get()) * 4;
            }
        }
        return power;
    }

    public static int getEnemyPiercing(HashMap<Item, Integer> effects) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.ENEMY_PIERCING_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.ENEMY_PIERCING_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.ENEMY_PIERCING_RUNE.get()) * 99999;
            }
        }
        return power;
    }

    public static int getHoming(HashMap<Item, Integer> effects) {
        int power = 0;
        if ( effects.containsKey(RunicItemsItems.HOMING_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.HOMING_RUNE.get()) != null ) {
                power += effects.get(RunicItemsItems.HOMING_RUNE.get());
            }
        }
        return power;
    }

    public static boolean getTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.TRIGGER_RUNE.get());
    }

    public static boolean getDeathTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.DEATH_TRIGGER_RUNE.get());
    }

    public static boolean getGravity(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.GRAVITY_RUNE.get());
    }

    public static boolean getBlockPiercing(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.BLOCK_PIERCING_RUNE.get());
    }
}
