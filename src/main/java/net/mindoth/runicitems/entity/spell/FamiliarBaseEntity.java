package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class FamiliarBaseEntity extends SpellBaseEntity {

    public FamiliarBaseEntity(EntityType<FamiliarBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FamiliarBaseEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.FAMILIAR_BASE.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        int potency = 1 + power;
        if ( RunicItemsItems.MAGIC_SPARK_RUNE.get().equals(rune) ) {
            if ( potency > 0 ) {
                target.hurt(DamageSource.indirectMagic(this, owner), potency);
            }
        }
        else if ( RunicItemsItems.MAGIC_SPARK_RUNE.get().equals(rune) ) {
            if ( potency > 0 ) {
                target.heal(potency);
            }
        }
    }

    @Override
    protected void addEffects(LivingEntity target) {
        if ( fire && !ice ) {
            target.setSecondsOnFire(5);
        }
        if ( ice && !fire ) {
            target.setTicksFrozen(560);
        }
    }

    @Override
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

    protected void zapTarget(Entity player, Level pLevel, double size, int power) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(2 + size));
        if ( targets.size() > 0 ) {
            pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                    SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25f, 0.25f);
            pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                    SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
        }
        for ( LivingEntity target : targets ) {
            if ( target != player && !target.isAlliedTo(player) && target.isAttackable() ) {
                target.hurt(DamageSource.MAGIC, power);
                ServerLevel level = (ServerLevel) pLevel;
                double playerX = player.getBoundingBox().getCenter().x;
                double playerY = player.getBoundingBox().getCenter().y;
                double playerZ = player.getBoundingBox().getCenter().z;
                double listedEntityX = target.getBoundingBox().getCenter().x();
                double listedEntityY = target.getBoundingBox().getCenter().y();
                double listedEntityZ = target.getBoundingBox().getCenter().z();
                int particleInterval = (int)Math.round(player.distanceTo(target)) * 5;
                for ( int k = 1; k < (1 + particleInterval); k++ ) {
                    double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
                    double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
                    double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK, lineX, lineY, lineZ, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }

        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() && this.tickCount % 20 == 0 && this.tickCount >= 40 ) {
            zapTarget( this, level, ((double)power / 2), power);
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

    @Override
    protected void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            for (int i = 0; i < 8; ++i) {
                level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() ) {
            return ParticleTypes.SMOKE;
        }
        else return ParticleTypes.ASH;
    }
}
