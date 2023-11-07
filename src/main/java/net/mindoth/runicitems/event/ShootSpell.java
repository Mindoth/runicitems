package net.mindoth.runicitems.event;

import net.mindoth.runicitems.entity.minion.SkeletonMinionEntity;
import net.mindoth.runicitems.entity.spell.*;
import net.mindoth.runicitems.entity.minion.BlazeMinionEntity;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

    public static void summonCloud(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        Level level = caster.level;
        Vec3 center = CommonEvents.getEntityCenter(caster);

        CloudBaseEntity cloud;
        float speed = 0.25F;
        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() ) {
            cloud = new StormyCloudEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSummonSound(level, center);
        }
        else if ( rune == RunicItemsItems.MAGICAL_CLOUD_RUNE.get() ) {
            cloud = new MagicalCloudEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSummonSound(level, center);
        }
        else if ( rune == RunicItemsItems.TORNADO_RUNE.get() ) {
            cloud = new TornadoEntity(level, owner, caster, itemHandler, slot, effects, rune);
            playMagicSummonSound(level, center);
        }
        else return;

        if ( SpellBuilder.getGravity(effects) ) cloud.setNoGravity(true);
        if ( caster != owner ) {
            cloud.setPos(center);
            cloud.shootFromRotation(caster, xRot * -1, yRot * -1, 0F, SpellBuilder.getSpeed(effects, speed), 0);
        }
        else cloud.shootFromRotation(caster, xRot, yRot, 0F, SpellBuilder.getSpeed(effects, speed), 0);
        if ( SpellBuilder.getSpeed(effects, speed) == 0.25F ) cloud.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(cloud);
    }

    public static void summonMinion(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune) {
        Level level = caster.getLevel();
        if ( level.isClientSide ) return;
        Vec3 center = CommonEvents.getEntityCenter(caster);
        Mob minion;
        int duration = 600;

        if ( rune == RunicItemsItems.FORGE_SPIRIT_RUNE.get() ) {
            playFireSound(level, center);
            minion = new BlazeMinionEntity(level, owner);
            minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(SpellBuilder.getPower(effects, 6));
            minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(SpellBuilder.getPower(effects, 20));
            minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(SpellBuilder.getSpeed(effects, 0.23F));
            minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(SpellBuilder.getRange(effects) * 12.0D);
            minion.moveTo(center);
            minion.addEffect(new MobEffectInstance(RunicItemsEffects.BLAZE_TIMER.get(), SpellBuilder.getLife(effects, duration), 0, false, false, true));
        }
        else if ( rune == RunicItemsItems.RAISE_DEAD_RUNE.get() ) {
            playEvilSound(level, center);
            minion = new SkeletonMinionEntity(level, owner);
            minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(SpellBuilder.getPower(effects, 4));
            minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(SpellBuilder.getPower(effects, 20));
            minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(SpellBuilder.getSpeed(effects, 0.25F));
            minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(SpellBuilder.getRange(effects) * 12.0D);
            minion.moveTo(center);
            minion.addEffect(new MobEffectInstance(RunicItemsEffects.SKELETON_TIMER.get(), SpellBuilder.getLife(effects, duration), 0, false, false, true));
        }
        else {
            playMagicSummonSound(level, center);
            System.out.println("OOPS. Something went wrong so you get a sheep instead. Report to the mod author.");
            minion = new Sheep(EntityType.SHEEP, level);
        }
        minion.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(minion.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }

    public static void causeExplosion(Entity caster, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(caster);
        Explosion.BlockInteraction destroyBlocks;
        if ( SpellBuilder.getBlockPiercing(effects) ) destroyBlocks = Explosion.BlockInteraction.NONE;
        else destroyBlocks = Explosion.BlockInteraction.DESTROY;
        level.explode(null, DamageSource.MAGIC, null, center.x, center.y, center.z, SpellBuilder.getPower(effects, 1), false, destroyBlocks);
    }

    public static void startGhostWalk(LivingEntity caster, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(caster);
        int duration = 1200;
        playMagicSound(level, center);
        caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, SpellBuilder.getLife(effects, duration), 0, false, false, true));
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
