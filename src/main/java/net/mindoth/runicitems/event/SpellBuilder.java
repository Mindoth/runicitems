package net.mindoth.runicitems.event;

import net.mindoth.runicitems.inventory.WandData;
import net.mindoth.runicitems.item.WandItem;
import net.mindoth.runicitems.item.rune.ComponentRuneItem;
import net.mindoth.runicitems.item.rune.ModifierRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.spells.*;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class SpellBuilder {

    public static void castFromWand(Player owner, ItemStack ogStack) {
        WandData ogData = WandItem.getData(ogStack);
        final IItemHandler handler = ogData.getHandler();
        cast(owner, owner, handler, 0, owner.getXRot(), owner.getYRot());
    }

    public static void cast(Player owner, Entity caster, IItemHandler itemHandler, int slot, float xRot, float yRot) {
        HashMap<Item, Integer> effects = new HashMap<>();
        int componentCounter = 0;
        for ( int i = slot; i < itemHandler.getSlots(); i++ ) {
            Item rune = getRune(itemHandler, i);
            if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                if ( rune instanceof ModifierRuneItem || rune instanceof ComponentRuneItem ) {
                    effects.merge(rune, 1, Integer::sum);
                    if ( rune instanceof ComponentRuneItem ) componentCounter += 1;
                }
                if ( componentCounter == 3 ) {
                    doSpell(owner, caster, itemHandler, i, effects, xRot, yRot);
                    effects.clear();
                    break;
                }
            }
        }
    }

    private static void doSpell(Player owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, float xRot, float yRot) {
        final AbstractSpell spell = getSpell(effects);
        Vec3 center;
        int distance = SpellBuilder.getDistance(effects);
        if ( distance > 0 ) {
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition().add(direction);
        }
        else center = CommonEvents.getEntityCenter(caster);
        AbstractSpell.routeSpell(owner, caster, itemHandler, slot, effects, spell, center, xRot, yRot);
    }

    private static AbstractSpell getSpell(HashMap<Item, Integer> effects) {
        int ice = 0;
        int storm = 0;
        int fire = 0;
        AbstractSpell spell = new ExplosionSpell();
        if ( effects.containsKey(RunicItemsItems.ICE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.ICE_RUNE.get()) != null ) {
                ice += effects.get(RunicItemsItems.ICE_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.STORM_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.STORM_RUNE.get()) != null ) {
                storm += effects.get(RunicItemsItems.STORM_RUNE.get());
            }
        }
        if ( effects.containsKey(RunicItemsItems.FIRE_RUNE.get()) ) {
            if ( effects.get(RunicItemsItems.FIRE_RUNE.get()) != null ) {
                fire += effects.get(RunicItemsItems.FIRE_RUNE.get());
            }
        }
        if ( ice == 1 && storm == 1 && fire == 1 ) spell = new DeafeningBlastSpell();
        //if ( ice == 3 && storm == 0 && fire == 0 ) spell = new IcicleSpell();
        if ( ice == 3 && storm == 0 && fire == 0 ) spell = new IceProjectileSpell();
        //if ( ice == 0 && storm == 3 && fire == 0 ) spell = new UnstableCloudSpell();
        if ( ice == 0 && storm == 3 && fire == 0 ) spell = new StormProjectileSpell();
        //if ( ice == 0 && storm == 0 && fire == 3 ) spell = new SunStrikeSpell();
        if ( ice == 0 && storm == 0 && fire == 3 ) spell = new FireProjectileSpell();
        if ( ice == 2 && storm == 1 && fire == 0 ) spell = new GhostWalkSpell();
        if ( ice == 1 && storm == 2 && fire == 0 ) spell = new TornadoSpell();
        if ( ice == 0 && storm == 2 && fire == 1 ) spell = new AlacritySpell();
        if ( ice == 0 && storm == 1 && fire == 2 ) spell = new MeteorSpell();
        if ( ice == 1 && storm == 0 && fire == 2 ) spell = new ForgeSpiritSpell();
        return spell;
    }

    private static Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
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
                power += effects.get(RunicItemsItems.ENEMY_PIERCING_RUNE.get());
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

    public static boolean getBlockPiercing(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.BLOCK_PIERCING_RUNE.get());
    }

    public static boolean getHoming(HashMap<Item, Integer> effects) {
        return effects.containsKey(RunicItemsItems.HOMING_RUNE.get());
    }

    public static ArrayList<LivingEntity> getEntitiesAround(Entity caster, Level pLevel, double size) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(size));
        targets.removeIf(entry -> entry == caster || !(entry.isAttackable()) || !(entry.isAlive() || (entry instanceof Player && ((Player)entry).getAbilities().instabuild)));
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
