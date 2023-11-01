package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

public class FamiliarBaseEntity extends ThrowableProjectile {

    protected FamiliarBaseEntity(EntityType<? extends FamiliarBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected FamiliarBaseEntity(EntityType<? extends FamiliarBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
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
        this.range = SpellBuilder.getRange(effects);
        this.fire = SpellBuilder.getFire(effects);
        this.ice = SpellBuilder.getIce(effects);
        this.enemyPiercing = SpellBuilder.getEnemyPiercing(effects);
        this.blockPiercing = SpellBuilder.getBlockPiercing(effects);
        this.speed = SpellBuilder.getSpeed(effects, 0.5f);
        this.homing = SpellBuilder.getHoming(effects);
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
    protected int range;
    protected boolean fire;
    protected boolean ice;
    protected boolean enemyPiercing;
    protected boolean blockPiercing;
    protected float speed;
    protected boolean homing;

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

            BlockHitResult traceResult = (BlockHitResult) result;
            BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
            if ( !blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty() ) {
                if ( bounces > 0 ) {
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
                    this.bounces -= 1;
                }
                else if ( !blockPiercing ) {
                    this.setDeltaMovement(0, 0, 0);
                    level.playSound(null, this.getX(), this.getY(), this.getZ(),
                            blockstate.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.2f, 2);
                }
            }
            if ( trigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            }
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
        if ( this.random.nextDouble() > 0.5d ) spawnEffectParticles();

        if ( this.homing && this.tickCount > 20 ) {
            Entity nearest = SpellBuilder.getNearestEntity(this, level, this.range);
            if ( nearest != null && this.distanceTo(nearest) > 1 ) {
                SpellBuilder.lookAt(this, nearest);
                double factor = this.getDeltaMovement().length();
                this.setDeltaMovement(this.getLookAngle().normalize().multiply(factor, factor, factor));
            }
        }

        if ( this.tickCount > life ) {
            if ( deathTrigger && nextSpellSlot >= 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
            }
            this.discard();
        }
    }

    protected void hurtTarget(LivingEntity target) {
    }

    protected void doTickEffects() {
    }

    protected void spawnParticles() {
        ServerLevel level = (ServerLevel)this.level;
        for (int i = 0; i < 8; ++i) {
            level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).y + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), 2, 0, 0, 0, 0);
        }
    }

    protected void spawnEffectParticles() {
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    public ResourceLocation getSpellTexture() {
        return new ResourceLocation(RunicItems.MOD_ID, "textures/spells/clear.png");
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
