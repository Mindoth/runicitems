package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.particle.ParticleColor;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class AbstractProjectileEntity extends ThrowableEntity {

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    protected int getBasePower() {
        return 1;
    }

    protected float getBaseSpeed() {
        return 1.0F;
    }

    protected int getBaseLife() {
        return 40;
    }

    protected int getBaseEnemyPiercing() {
        return 0;
    }

    protected AbstractProjectileEntity(EntityType<? extends AbstractProjectileEntity> entityType, World level) {
        super(entityType, level);
    }

    protected AbstractProjectileEntity(EntityType<? extends AbstractProjectileEntity> pEntityType, World pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                                       HashMap<Item, Integer> effects) {
        super(pEntityType, owner, pLevel);

        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;

        this.trigger = SpellBuilder.getTrigger(effects);
        this.deathTrigger = SpellBuilder.getDeathTrigger(effects);
        this.bounces = SpellBuilder.getBounce(effects);
        this.enemyPiercing = getBaseEnemyPiercing() + SpellBuilder.getEnemyPiercing(effects);
        this.blockPiercing = SpellBuilder.getBlockPiercing(effects);
        this.homing = SpellBuilder.getHoming(effects);

        this.range = SpellBuilder.getRange(effects);

        this.power = SpellBuilder.getPower(effects, getBasePower());
        this.speed = SpellBuilder.getSpeed(effects, getBaseSpeed());
        this.life = Math.max(10, SpellBuilder.getLife(effects, getBaseLife()));
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected int slot;
    protected boolean trigger;
    protected boolean deathTrigger;
    protected int bounces;
    protected int enemyPiercing;
    protected boolean blockPiercing;
    protected int homing;
    protected int range;

    protected int power;
    protected float speed;
    protected int life;

    @Override
    protected void onHit(RayTraceResult result) {
        if ( level.isClientSide ) return;
        if ( result.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult)result).getEntity() instanceof LivingEntity ) {
            LivingEntity living = (LivingEntity)((EntityRayTraceResult)result).getEntity();
            hurtTarget(living);
            if ( trigger ) SpellBuilder.cast((PlayerEntity)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            if ( this.enemyPiercing > 0 ) this.enemyPiercing -= 1;
            else this.remove();
        }

        if ( result.getType() == RayTraceResult.Type.BLOCK ) {
            doBlockEffects(result);
            BlockRayTraceResult traceResult = (BlockRayTraceResult) result;
            BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
            if ( !blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty() ) {
                if ( !blockPiercing ) {
                    if ( trigger || bounces > 0 ) {
                        Direction face = traceResult.getDirection();
                        blockstate.onProjectileHit(this.level, blockstate, traceResult, this);
                        Vector3d motion = this.getDeltaMovement();
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
                    if ( trigger ) SpellBuilder.cast((PlayerEntity)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
                    if ( bounces > 0 ) this.bounces -= 1;
                    else remove();
                }
            }
            if ( !blockPiercing && this.getDeltaMovement().length() > 0 ) level.playSound(null, this.getX(), this.getY(), this.getZ(), blockstate.getSoundType().getBreakSound(), SoundCategory.PLAYERS, 0.2f, 2);
        }
    }

    protected void doBlockEffects(RayTraceResult result) {
    }

    @Override
    public void tick() {
        super.tick();
        if ( level.isClientSide ) return;
        doTickEffects();
        spawnParticles();

        if ( this.homing > 0 && this.tickCount > 10 ) {
            Entity nearest = CommonEvents.getNearestEntity(this, level, this.homing * 2);
            if ( nearest != null && this.speed != 0 ) {
                double mX = getDeltaMovement().x();
                double mY = getDeltaMovement().y();
                double mZ = getDeltaMovement().z();
                Vector3d spellPos = new Vector3d(getX(), getY(), getZ());
                Vector3d targetPos = new Vector3d(CommonEvents.getEntityCenter(nearest).x, CommonEvents.getEntityCenter(nearest).y, CommonEvents.getEntityCenter(nearest).z);
                Vector3d lookVec = targetPos.subtract(spellPos);
                Vector3d spellMotion = new Vector3d(mX, mY, mZ);
                double arc = 0.25D;
                Vector3d lerpVec = new Vector3d(spellMotion.x + lookVec.x * arc, spellMotion.y + lookVec.y * arc, spellMotion.z + lookVec.z * arc);
                shoot(lerpVec.x, lerpVec.y, lerpVec.z, this.speed, 0);
            }
        }

        if ( this.tickCount > life ) {
            if ( deathTrigger ) SpellBuilder.cast((PlayerEntity)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            this.remove();
        }
        if ( this.getDeltaMovement().equals(new Vector3d(0, 0, 0)) ) this.remove();
    }

    protected void doTickEffects() {
    }

    protected void hurtTarget(LivingEntity target) {
    }

    protected void addEffects(LivingEntity target) {
    }

    protected void splashDamage() {
        ArrayList<LivingEntity> list = CommonEvents.getEntitiesAround(this, level, this.range);
        for ( LivingEntity target : list ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
            addEffects(target);
        }
        spawnSplashParticles();
    }

    protected void spawnSplashParticles() {
    }

    protected double getParticleHeight() {
        return this.getY();
    }

    protected void spawnBonusParticles() {
    }

    protected void spawnParticles() {
        spawnBonusParticles();
        ServerWorld level = (ServerWorld)this.level;
        level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), getParticleHeight() + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), 0, 0, 0, 0, 0);
    }

    protected BasicParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
