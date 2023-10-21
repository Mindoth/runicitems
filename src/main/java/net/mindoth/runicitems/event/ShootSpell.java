package net.mindoth.runicitems.event;

import net.mindoth.runicitems.entity.spell.FamiliarBaseEntity;
import net.mindoth.runicitems.entity.spell.ProjectileBaseEntity;
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
        level.explode(null, DamageSource.MAGIC, null, pos.x, pos.y, pos.z, SpellBuilder.getPower(effects), SpellBuilder.getFire(effects), Explosion.BlockInteraction.NONE);
    }

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);
        ProjectileBaseEntity projectile = new ProjectileBaseEntity(level, owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        if ( caster != owner ) projectile.setPos(center);

        projectile.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, SpellBuilder.getSpeed(effects, 0.5F), 1.0F);
        level.addFreshEntity(projectile);

        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }

    public static void summonFamiliar(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);
        FamiliarBaseEntity familiar = new FamiliarBaseEntity(level, owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
        if ( SpellBuilder.getGravity(effects) ) familiar.setNoGravity(true);
        if ( caster != owner ) familiar.setPos(center);

        familiar.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, SpellBuilder.getSpeed(effects, 0), 0);
        if ( SpellBuilder.getSpeed(effects, 0) == 0 ) familiar.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(familiar);

        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25f, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }
}
