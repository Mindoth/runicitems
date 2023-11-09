package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class AlacritySpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(caster);
        int duration = 400;
        int life = SpellBuilder.getLife(effects, duration);
        double range = SpellBuilder.getRange(effects) * 2;
        int amplifier = Math.round(SpellBuilder.getPower(effects, 0)) - 1;
        playMagicSound(level, center);
        owner.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, life, amplifier, false, false, true));
        doParticleEffects(level, owner);

        MobEffectInstance buff = new MobEffectInstance(MobEffects.DIG_SPEED, SpellBuilder.getLife(effects, duration), Math.round(amplifier), false, false, true);
        if ( caster == owner ) {
            Vec3 direction = caster.getLookAngle().normalize();
            direction = direction.multiply(range, range, range);
            center = caster.getEyePosition().add(direction);
            doForAllPointsOnVec(level, caster, center, buff);
        }
        else if ( SpellBuilder.getNearestEntity(caster, level, range) != null ) {
            LivingEntity target = (LivingEntity)SpellBuilder.getNearestEntity(caster, level, range);
            target.addEffect(buff);
            doParticleEffects(level, target);
        }
    }

    private static void doForAllPointsOnVec(Level level, Entity caster, Vec3 center, MobEffectInstance buff) {
        double playerX = CommonEvents.getEntityCenter(caster).x;
        double playerY = CommonEvents.getEntityCenter(caster).y;
        double playerZ = CommonEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            float error = 0.25F;
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) {
                        target = closestSoFar;
                    }
                }
            }
            if ( target != null ) {
                ((LivingEntity)target).addEffect(buff);
                doParticleEffects(level, target);
                break;
            }
        }
    }

    private static void doParticleEffects(Level level, Entity target) {
        Vec3 targetPos = CommonEvents.getEntityCenter(target);
        if ( !(level instanceof ServerLevel serverLevel) ) return;
        for ( int i = 0; i < 360; i++ ) {
            if ( i % 10 == 0 ) {
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, targetPos.x + Math.cos(i) * target.getBbWidth(), targetPos.y, targetPos.z + Math.sin(i) * target.getBbWidth(), 0, -Math.cos(i), 0, -Math.sin(i), 0.75F);
            }
        }
    }
}
