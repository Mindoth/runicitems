package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.entity.projectile.SparkBoltEntity;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class ShootProjectile {

    public static void shootSparkBolt(LivingEntity owner, Entity caster, HashMap<Item, Integer> effects) {
        Level level = caster.level;
        SparkBoltEntity sparkBolt = new SparkBoltEntity(level, owner, effects);
        sparkBolt.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, 1.5F, 1.0F);
        level.addFreshEntity(sparkBolt);

        Vec3 center = CommonEvents.getEntityCenter(caster);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }
}
