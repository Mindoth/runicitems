package net.mindoth.runicitems.entity;

import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

public class ProjectileBaseEntity extends ThrowableItemProjectile {

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        super(pEntityType, owner, pLevel);
        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;
        this.trigger = SpellBuilder.getTrigger(effects);
        this.deathTrigger = SpellBuilder.getDeathTrigger(effects);
        this.nextSpellSlot = SpellBuilder.getNextSpellSlot(slot, itemHandler);
        this.power = SpellBuilder.getPower(effects);
        this.bounces = SpellBuilder.getBounce(effects);
        this.life = Math.max(10, 160 + SpellBuilder.getLife(effects));
        this.fire = SpellBuilder.getFire(effects);
        this.ice = SpellBuilder.getIce(effects);
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected int slot;
    protected boolean trigger;
    protected boolean deathTrigger;
    protected int nextSpellSlot;
    protected int power;
    protected int bounces;
    protected int life;
    protected boolean fire;
    protected boolean ice;

    @Override
    protected void onHit(HitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ) {
            hurtTarget(living);
            classHurtTarget(living);
            //splashParticles(living);
        }

        if ( result.getType() == HitResult.Type.BLOCK ) {
            doBlockEffects(result);
        }

        if ( trigger && nextSpellSlot >= 0 ) {
            caster = this;
            SpellBuilder.cast((Player)owner, caster, itemHandler, slot + 1);
        }
    }

    protected void hurtTarget(LivingEntity target) {
        if ( fire && !ice ) {
            target.setSecondsOnFire(5);
        }
        if ( ice && !fire ) {
            target.setTicksFrozen(560);
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


                if (face == Direction.EAST)
                    motionX = -motionX;
                else if (face == Direction.SOUTH)
                    motionZ = -motionZ;
                else if (face == Direction.WEST)
                    motionX = -motionX;
                else if (face == Direction.NORTH)
                    motionZ = -motionZ;
                else if (face == Direction.UP)
                    motionY = -motionY;
                else if (face == Direction.DOWN)
                    motionY = -motionY;

                this.setDeltaMovement(motionX, motionY, motionZ);
            }
            this.bounces -= 1;
        }
        else this.discard();
        level.playSound(null, this.getX(), this.getY(), this.getZ(),
                blockstate.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.2f, 2);
    }

    protected void classHurtTarget(LivingEntity target) {
    }



    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }
        if ( this.tickCount > life ) {
            if ( deathTrigger && nextSpellSlot >= 0 ) {
                caster = this;
                SpellBuilder.cast((Player)owner, caster, itemHandler, slot + 1);
            }
            this.discard();
        }
        spawnParticles();
    }

    /*private void splashParticles(LivingEntity target) {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            for (int i = 0; i < 1 + 2 * SpellBuilder.getPower(effects); ++i) {
                level.sendParticles(this.getParticle(), target.getBoundingBox().getCenter().x, target.getBoundingBox().getCenter().y, target.getBoundingBox().getCenter().z, 1, target.getBoundingBox().getXsize() / 2 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getYsize() / 4 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getZsize() / 2 + this.random.nextDouble() * 0.15F, 0);
            }
            if ( SpellBuilder.getFire(effects) ) {
                for (int i = 0; i < 8; ++i) {
                    level.sendParticles(ParticleTypes.FLAME, target.getBoundingBox().getCenter().x, target.getBoundingBox().getCenter().y, target.getBoundingBox().getCenter().z, 1, target.getBoundingBox().getXsize() / 2 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getYsize() / 4 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getZsize() / 2 + this.random.nextDouble() * 0.15F, 0);
                }
            }
            if ( SpellBuilder.getIce(effects) ) {
                for (int i = 0; i < 8; ++i) {
                    level.sendParticles(ParticleTypes.SNOWFLAKE, target.getBoundingBox().getCenter().x, target.getBoundingBox().getCenter().y, target.getBoundingBox().getCenter().z, 1, target.getBoundingBox().getXsize() / 2 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getYsize() / 4 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getZsize() / 2 + this.random.nextDouble() * 0.15F, 0);
                }
            }
        }
    }*/

    private void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);

        }
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    protected float getGravity() {
        return 0.025F;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
