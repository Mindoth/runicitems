package net.mindoth.runicitems.spell;

import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

import static java.lang.Math.abs;

public class SpawnEffect {

    public static void causeExplosion(LivingEntity owner, Entity caster, HashMap<String, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 pos = CommonEvents.getEntityCenter(caster);
        level.explode(null, DamageSource.MAGIC, null, pos.x, pos.y, pos.z, abs(effects.get("power")), false, Explosion.BlockInteraction.NONE);
    }
}
