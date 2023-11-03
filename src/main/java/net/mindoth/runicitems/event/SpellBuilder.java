package net.mindoth.runicitems.event;

import net.mindoth.runicitems.item.rune.ModifierRuneItem;
import net.mindoth.runicitems.item.rune.FamiliarRuneItem;
import net.mindoth.runicitems.item.rune.ProjectileRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

        if ( rune instanceof ProjectileRuneItem ) {
            shootMagic(owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        }
        if ( rune instanceof FamiliarRuneItem) {
            summonFamiliar(owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        }
        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            causeExplosion(owner, caster, itemHandler, slot, effects);
        }
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

    public static Integer getPower(HashMap<Item, Integer> effects) {
        int power = 1;
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
            speed += (power / 4);
        }
        return speed;
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

    public static Integer getLife(HashMap<Item, Integer> effects) {
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
        return power * 40;
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

    public static boolean getTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.TRIGGER_RUNE.get());
    }

    public static boolean getDeathTrigger(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.DEATH_TRIGGER_RUNE.get());
    }

    public static boolean getFire(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.FIRE_RUNE.get());
    }

    public static boolean getIce(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.ICE_RUNE.get());
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
        targets.removeIf(entry -> entry == player || !(entry.isAttackable()) || !(entry.isAlive()));
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

    public static void lookAt(Entity spell, Entity target) {
        Vec3 vec3 = CommonEvents.getEntityCenter(spell);
        Vec3 pTarget = CommonEvents.getEntityCenter(target);
        double d0 = pTarget.x - vec3.x;
        double d1 = pTarget.y - vec3.y;
        double d2 = pTarget.z - vec3.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        spell.setXRot(Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)))));
        spell.setYRot(Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
        spell.setYHeadRot(spell.getYRot());
        spell.xRotO = spell.getXRot();
        spell.yRotO = spell.getYRot();
    }
}
