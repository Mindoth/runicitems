package net.mindoth.runicitems.event;

import net.mindoth.runicitems.entity.spell.*;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class ShootSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);

        ProjectileBaseEntity projectile;
        if ( rune == RunicItemsItems.MAGIC_SPARK_RUNE.get() ) {
            projectile = new MagicSparkEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSound(level, center);
        }
        else if ( rune == RunicItemsItems.HEALING_BOLT_RUNE.get() ) {
            projectile = new HealingBoltEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSound(level, center);
        }
        else if ( rune == RunicItemsItems.METEOR_RUNE.get() ) {
            projectile = new MeteorEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playFireSound(level, center);
        }
        else if ( rune == RunicItemsItems.COMET_RUNE.get() ) {
            projectile = new CometEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSound(level, center);
        }
        else if ( rune == RunicItemsItems.WITHER_SKULL_RUNE.get() ) {
            projectile = new WitherSkullEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playEvilSound(level, center);
        }
        else return;

        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        if ( caster != owner ) {
            projectile.setPos(center);
            projectile.shootFromRotation(caster, xRot * -1, yRot * -1, 0F, SpellBuilder.getSpeed(effects, 1.0F), 1.0F);
        }
        else {
            projectile.shootFromRotation(caster, xRot, yRot, 0F, SpellBuilder.getSpeed(effects, 1.0F), 1.0F);
        }
        level.addFreshEntity(projectile);
    }

    public static void summonFamiliar(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);

        FamiliarBaseEntity familiar;
        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() ) {
            familiar = new StormyCloudEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSummonSound(level, center);
        }
        else if ( rune == RunicItemsItems.MAGICAL_CLOUD_RUNE.get() ) {
            familiar = new MagicalCloudEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSummonSound(level, center);
        }
        else return;

        if ( SpellBuilder.getGravity(effects) ) familiar.setNoGravity(true);
        if ( caster != owner ) {
            familiar.setPos(center);
            familiar.shootFromRotation(caster, xRot * -1, yRot * -1, 0F, SpellBuilder.getSpeed(effects, 0.125F), 0);
        }
        else {
            familiar.shootFromRotation(caster, xRot, yRot, 0F, SpellBuilder.getSpeed(effects, 0.125F), 0);
        }
        level.addFreshEntity(familiar);
    }

    public static void causeExplosion(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 pos = CommonEvents.getEntityCenter(caster);
        Explosion.BlockInteraction destroyBlocks;
        if ( SpellBuilder.getBlockPiercing(effects) ) destroyBlocks = Explosion.BlockInteraction.NONE;
        else destroyBlocks = Explosion.BlockInteraction.DESTROY;
        level.explode(null, DamageSource.MAGIC, null, pos.x, pos.y, pos.z, SpellBuilder.getPower(effects, 1), SpellBuilder.getFire(effects), destroyBlocks);
    }



    private static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }

    private static void playFireSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5f, 1);
    }

    private static void playEvilSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, 1);
    }

    private static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25f, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }
}
