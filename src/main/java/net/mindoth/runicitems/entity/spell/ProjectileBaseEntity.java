package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectileBaseEntity extends ThrowableProjectile {

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                                   HashMap<Item, Integer> effects, Item rune) {
        super(pEntityType, owner, pLevel);

        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;
        this.rune = rune;
        this.nextSpellSlot = SpellBuilder.getNextSpellSlot(slot, itemHandler);

        this.trigger = SpellBuilder.getTrigger(effects);
        this.deathTrigger = SpellBuilder.getDeathTrigger(effects);
        this.bounces = SpellBuilder.getBounce(effects);
        this.enemyPiercing = SpellBuilder.getEnemyPiercing(effects);
        this.blockPiercing = SpellBuilder.getBlockPiercing(effects);
        this.homing = SpellBuilder.getHoming(effects);

        this.range = SpellBuilder.getRange(effects);

        this.basePower = 1;
        this.baseSpeed = 1.0F;
        this.baseLife = 40;

        if ( rune == RunicItemsItems.MAGIC_SPARK_RUNE.get() ) this.basePower += 2;
        if ( rune == RunicItemsItems.HEALING_BOLT_RUNE.get() ) {
            this.basePower += 1;
            this.baseLife = 20;
        }
        if ( rune == RunicItemsItems.WITHER_SKULL_RUNE.get() ) this.basePower += 7;

        this.power = SpellBuilder.getPower(effects, this.basePower);
        this.speed = SpellBuilder.getSpeed(effects, this.baseSpeed);
        this.life = Math.max(10, baseLife + SpellBuilder.getLife(effects, this.baseLife));
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected int slot;
    protected Item rune;
    protected boolean trigger;
    protected boolean deathTrigger;
    protected int nextSpellSlot;
    protected int bounces;
    protected boolean enemyPiercing;
    protected boolean blockPiercing;
    protected boolean homing;
    protected int range;

    protected int basePower;
    protected int power;
    protected float baseSpeed;
    protected float speed;
    protected int baseLife;
    protected int life;

    @Override
    protected void onHit(HitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ) {
            hurtTarget(living);

            if ( trigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.getXRot(), this.getYRot());
            }
        }

        if ( result.getType() == HitResult.Type.BLOCK ) {
            doBlockEffects(result);

            BlockHitResult traceResult = (BlockHitResult) result;
            BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
            if ( !blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty() ) {
                if ( !blockPiercing ) {
                    if ( (trigger && nextSpellSlot >= 0) || bounces > 0 ) {
                        Direction face = traceResult.getDirection();
                        blockstate.onProjectileHit(this.level, blockstate, traceResult, this);
                        Vec3 motion = this.getDeltaMovement();
                        double motionX = motion.x();
                        double motionY = motion.y();
                        double motionZ = motion.z();
                        if (face == Direction.EAST) {
                            motionX = -motionX;
                        }
                        else if (face == Direction.SOUTH) {
                            motionZ = -motionZ;
                        }
                        else if (face == Direction.WEST) {
                            motionX = -motionX;
                        }
                        else if (face == Direction.NORTH) {
                            motionZ = -motionZ;
                        }
                        else if (face == Direction.UP) {
                            motionY = -motionY;
                        }
                        else if (face == Direction.DOWN) {
                            motionY = -motionY;
                        }
                        //this.setDeltaMovement(motionX, motionY, motionZ);

                        //This seems to work better with low velocity projectiles
                        shoot(motionX, motionY, motionZ, this.speed, 0);
                    }
                    if ( trigger && nextSpellSlot >= 0 ) SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.getXRot(), this.getYRot());
                    if ( bounces > 0 ) this.bounces -= 1;
                    else discard();
                }
            }
            if ( !blockPiercing && this.getDeltaMovement().length() > 0 ) level.playSound(null, this.getX(), this.getY(), this.getZ(), blockstate.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.2f, 2);
        }
    }

    protected void doBlockEffects(HitResult result) {
    }

    @Override
    public void tick() {
        super.tick();
        if ( level.isClientSide ) return;
        doTickEffects();
        spawnParticles();

        if ( this.homing && this.tickCount > 10 ) {
            Entity nearest = SpellBuilder.getNearestEntity(this, level, this.range);
            if ( nearest != null && this.speed != 0 ) {
                double mX = getDeltaMovement().x();
                double mY = getDeltaMovement().y();
                double mZ = getDeltaMovement().z();
                Vec3 spellPos = new Vec3(getX(), getY(), getZ());
                Vec3 targetPos = new Vec3(CommonEvents.getEntityCenter(nearest).x, CommonEvents.getEntityCenter(nearest).y, CommonEvents.getEntityCenter(nearest).z);
                Vec3 lookVec = targetPos.subtract(spellPos);
                Vec3 spellMotion = new Vec3(mX, mY, mZ);
                double arc = 0.25D;
                Vec3 lerpVec = new Vec3(spellMotion.x + lookVec.x * arc, spellMotion.y + lookVec.y * arc, spellMotion.z + lookVec.z * arc);
                shoot(lerpVec.x, lerpVec.y, lerpVec.z, speed, 0);
            }
        }

        if ( this.tickCount > life ) {
            if ( deathTrigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.getXRot(), this.getYRot());
            }
            this.discard();
        }
    }

    protected void hurtTarget(LivingEntity target) {
    }

    protected void addEffects(LivingEntity target) {
    }

    protected void splashDamage() {
        ArrayList<LivingEntity> list = SpellBuilder.getEntitiesAround(this, level, this.range);
        for ( LivingEntity target : list ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
            addEffects(target);
        }
        spawnSplashParticles();
    }

    protected void spawnSplashParticles() {
    }

    protected void doTickEffects() {
    }

    protected double getParticleHeight() {
        return this.getY();
    }

    protected void spawnParticles() {
        ServerLevel level = (ServerLevel)this.level;
        level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), getParticleHeight() + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
