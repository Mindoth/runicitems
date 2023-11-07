package net.mindoth.runicitems.event;

import net.mindoth.runicitems.item.rune.*;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static net.mindoth.runicitems.event.ShootSpell.*;

public class SpellBuilder {

    public static void cast(Player owner, Entity caster, IItemHandler itemHandler, int slot, float xRot, float yRot) {
        HashMap<Item, Integer> effects = new HashMap<>();
        for (int i = slot; i < itemHandler.getSlots(); i++ ) {
            Item rune = getRune(itemHandler, i);
            if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                if ( rune instanceof SpellRuneItem ) {
                    doSpell(owner, caster, itemHandler, i, effects, xRot, yRot);
                    break;
                }
                if ( rune instanceof ModifierRuneItem) {
                    effects.merge(rune, 1, Integer::sum);
                }
            }
        }
    }

    public static void doSpell(Player owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, float xRot, float yRot) {
        Item rune = getRune(itemHandler, slot);

        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) causeExplosion(caster, effects);
        else if ( rune == RunicItemsItems.GHOST_WALK_RUNE.get() ) startGhostWalk(owner, effects);
        else if ( rune instanceof ProjectileRuneItem ) shootMagic(owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        else if ( rune instanceof CloudRuneItem) summonCloud(owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        else if ( rune instanceof MinionRuneItem ) summonMinion(owner, caster, itemHandler, slot, effects, rune);
    }

    public static Item getRune(IItemHandler itemHandler, int slot) {
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

    public static Integer getPower(HashMap<Item, Integer> effects, int basePower) {
        int power = 0;
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
        return basePower + power;
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

    public static boolean getTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.TRIGGER_RUNE.get());
    }

    public static boolean getDeathTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.DEATH_TRIGGER_RUNE.get());
    }

    public static boolean getGravity(HashMap<Item, Integer> effects) {
        return !effects.containsKey(RunicItemsItems.GRAVITY_RUNE.get());
    }

    public static boolean getEnemyPiercing(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.ENEMY_PIERCING_RUNE.get());
    }

    public static boolean getBlockPiercing(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.BLOCK_PIERCING_RUNE.get());
    }

    public static boolean getHoming(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.HOMING_RUNE.get());
    }

    public static ArrayList<LivingEntity> getEntitiesAround(Entity player, Level pLevel, double size) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(size));
        targets.removeIf(entry -> entry == player || !(entry.isAttackable()) || !(entry.isAlive() || (entry instanceof Player && ((Player)entry).getAbilities().instabuild)));
        return targets;
    }

    public static Entity getNearestEntity(Entity player, Level pLevel, double size) {
        ArrayList<LivingEntity> targets = getEntitiesAround(player, pLevel, size);
        LivingEntity target = null;
        double lowestSoFar = Double.MAX_VALUE;
        for ( LivingEntity closestSoFar : targets ) {
            if ( !closestSoFar.isAlliedTo(player) ) {
                double testDistance = player.distanceTo(closestSoFar);
                if ( testDistance < lowestSoFar ) {
                    target = closestSoFar;
                }
            }
        }
        return target;
    }
}
