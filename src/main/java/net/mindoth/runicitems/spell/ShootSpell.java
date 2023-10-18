package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.entity.projectile.HealingBoltEntity;
import net.mindoth.runicitems.entity.projectile.MagicSparkEntity;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class ShootSpell {

    public static void causeExplosion(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 pos = CommonEvents.getEntityCenter(caster);
        level.explode(null, DamageSource.MAGIC, null, pos.x, pos.y, pos.z, 1 + SpellBuilder.getPower(effects), SpellBuilder.getFire(effects), Explosion.BlockInteraction.NONE);
    }

    public static void shootMagicSpark(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);
        MagicSparkEntity projectile = new MagicSparkEntity(level, owner, caster, itemHandler, slot, effects);
        if ( !SpellBuilder.getGravity(effects) ) {
            projectile.setNoGravity(true);
        }

        if ( caster != owner ) {
            projectile.setPos(center);
        }
        projectile.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, 1.5F, 1.0F);

        level.addFreshEntity(projectile);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }

    public static void shootHealingBolt(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);
        HealingBoltEntity projectile = new HealingBoltEntity(level, owner, caster, itemHandler, slot, effects);
        if ( !SpellBuilder.getGravity(effects) ) {
            projectile.setNoGravity(true);
        }

        if ( caster != owner ) {
            projectile.setPos(center);
        }
        projectile.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, 1.5F, 1.0F);

        level.addFreshEntity(projectile);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }
}
