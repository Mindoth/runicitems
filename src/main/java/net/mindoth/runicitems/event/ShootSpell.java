package net.mindoth.runicitems.event;

import net.mindoth.runicitems.entity.spell.*;
import net.mindoth.runicitems.entity.summon.BlazeMinionEntity;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class ShootSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);

        ProjectileBaseEntity projectile;
        float speed = 1.0F;
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
            speed = 1.5F;
        }
        else if ( rune == RunicItemsItems.COMET_RUNE.get() ) {
            projectile = new CometEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSound(level, center);
            speed = 1.5F;
        }
        else if ( rune == RunicItemsItems.WITHER_SKULL_RUNE.get() ) {
            projectile = new WitherSkullEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playEvilSound(level, center);
        }
        else return;

        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        if ( caster != owner ) {
            projectile.setPos(center);
            projectile.shootFromRotation(caster, xRot * -1, yRot * -1, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
        }
        else {
            projectile.shootFromRotation(caster, xRot, yRot, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
        }
        level.addFreshEntity(projectile);
    }

    public static void summonFamiliar(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);

        FamiliarBaseEntity familiar;
        float speed = 0.125F;
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
            familiar.shootFromRotation(caster, xRot * -1, yRot * -1, 0F, SpellBuilder.getSpeed(effects, speed), 0);
        }
        else {
            familiar.shootFromRotation(caster, xRot, yRot, 0F, SpellBuilder.getSpeed(effects, speed), 0);
        }
        level.addFreshEntity(familiar);
    }

    //TODO add Skeleton summon entity and code
    public static void summonMinion(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune) {
        Level level = caster.getLevel();
        if ( level.isClientSide ) return;
        Vec3 pos = CommonEvents.getEntityCenter(caster);
        Mob minion;
        int duration = 600;

        if ( rune == RunicItemsItems.FORGE_SPIRIT_RUNE.get() ) {
            playFireSound(level, pos);
            minion = new BlazeMinionEntity(level, owner);
            minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(SpellBuilder.getPower(effects, 6));
            minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(SpellBuilder.getSpeed(effects, 0.23F));
            minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(48.0D);
            minion.moveTo(pos);
            minion.addEffect(new MobEffectInstance(RunicItemsEffects.BLAZE_TIMER.get(), duration + SpellBuilder.getLife(effects), 0, false, false, true));
        }
        else {
            playMagicSummonSound(level, pos);
            System.out.println("OOPS. Something went wrong so you get a sheep instead. Report to the mod author.");
            minion = new Sheep(EntityType.SHEEP, level);
        }
        minion.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(minion.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
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
