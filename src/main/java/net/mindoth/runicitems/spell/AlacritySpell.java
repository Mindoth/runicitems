package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class AlacritySpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(caster);
        int duration = 400;
        int life = SpellBuilder.getLife(effects, duration);
        float range = SpellBuilder.getRange(effects) * 2;
        int amplifier = Math.round(SpellBuilder.getPower(effects, 0)) - 1;
        playMagicSound(level, center);
        MobEffectInstance buff = new MobEffectInstance(MobEffects.DIG_SPEED, life, amplifier, false, false, true);
        doParticleEffects(level, owner);

        if ( caster == owner ) {
            LivingEntity target = (LivingEntity)CommonEvents.getPointedEntity(level, caster, range, 0.25F, true);
            target.addEffect(buff);
            doParticleEffects(level, target);
        }
        else if ( CommonEvents.getNearestEntity(caster, level, range) != null ) {
            LivingEntity target = (LivingEntity)CommonEvents.getNearestEntity(caster, level, range);
            target.addEffect(buff);
            doParticleEffects(level, target);
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
