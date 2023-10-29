package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.ClientReference;
import net.mindoth.runicitems.event.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

public class SpellBaseEntity extends ThrowableProjectile {

    protected SpellBaseEntity(EntityType<? extends SpellBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected SpellBaseEntity(EntityType<? extends SpellBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                              HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        super(pEntityType, owner, pLevel);
        this.xRot = xRot;
        this.yRot = yRot;

        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;
        this.rune = rune;
        this.trigger = SpellBuilder.getTrigger(effects);
        this.deathTrigger = SpellBuilder.getDeathTrigger(effects);
        this.nextSpellSlot = SpellBuilder.getNextSpellSlot(slot, itemHandler);
        this.power = SpellBuilder.getPower(effects);
        this.bounces = SpellBuilder.getBounce(effects);
        this.life = Math.max(10, 160 + SpellBuilder.getLife(effects));
        this.fire = SpellBuilder.getFire(effects);
        this.ice = SpellBuilder.getIce(effects);
        this.enemyPiercing = SpellBuilder.getEnemyPiercing(effects);
        this.blockPiercing = SpellBuilder.getBlockPiercing(effects);
        this.speed = SpellBuilder.getSpeed(effects, 0.5f);
    }

    protected float xRot;
    protected float yRot;

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected int slot;
    protected Item rune;
    protected boolean trigger;
    protected boolean deathTrigger;
    protected int nextSpellSlot;
    protected int power;
    protected int bounces;
    protected int life;
    protected boolean fire;
    protected boolean ice;
    protected boolean enemyPiercing;
    protected boolean blockPiercing;
    protected float speed;

    @Override
    protected void onHit(HitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ) {
            hurtTarget(living);

            if ( trigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            }
        }

        if ( result.getType() == HitResult.Type.BLOCK ) {
            doBlockEffects(result);

            if ( trigger && nextSpellSlot >= 0 ) {
                BlockHitResult traceResult = (BlockHitResult) result;
                BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
                if ( !blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty() ) {
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

                    this.setDeltaMovement(motionX, motionY, motionZ);
                }
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            }
        }
    }

    protected void doBlockEffects(HitResult result) {
        BlockHitResult traceResult = (BlockHitResult) result;
        BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
        if ( bounces > 0 ) {
            if ( !blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty() ) {
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

                this.setDeltaMovement(motionX, motionY, motionZ);
            }
            this.bounces -= 1;
        }
        else if ( !blockPiercing ) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(),
                    blockstate.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.2f, 2);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) return;
        doTickEffects();
        spawnParticles();
        if ( this.random.nextDouble() > 0.5d ) spawnEffectParticles();

        if ( this.tickCount > life ) {
            if ( deathTrigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            }
            this.discard();
        }
    }

    protected void hurtTarget(LivingEntity target) {
    }

    protected void addEffects(LivingEntity target) {
    }

    protected void doTickEffects() {
    }

    protected void spawnParticles() {
    }

    protected void spawnEffectParticles() {
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    public ResourceLocation getSpellTexture() {
        return ClientReference.CLEAR;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}