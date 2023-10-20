package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.spell.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class FamiliarBaseEntity extends SpellBaseEntity {

    public FamiliarBaseEntity(EntityType<FamiliarBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FamiliarBaseEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                              HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        super(RunicItemsEntities.FAMILIAR_BASE.get(), level, owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
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
        else if ( !blockPiercing ) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(),
                    blockstate.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.2f, 2);
            this.discard();
        }
    }

    protected void zapTarget(Entity player, Level pLevel, double size, int power) {
        Entity target = SpellBuilder.getNearestEntity(player, pLevel, size);
        if ( target == null ) return;
        target.hurt(DamageSource.MAGIC, power);
        ServerLevel level = (ServerLevel) pLevel;
        double playerX = CommonEvents.getEntityCenter(player).x;
        double playerY = CommonEvents.getEntityCenter(player).y;
        double playerZ = CommonEvents.getEntityCenter(player).z;
        double listedEntityX = CommonEvents.getEntityCenter(target).x();
        double listedEntityY = CommonEvents.getEntityCenter(target).y();
        double listedEntityZ = CommonEvents.getEntityCenter(target).z();
        int particleInterval = (int)Math.round(player.distanceTo(target)) * 5;
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, lineX, lineY, lineZ, 1, 0, 0, 0, 0);
        }
        pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.1f, 2);
        pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
    }
    @Override
    public void doTickEffects() {
        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() && this.tickCount % 10 == 0 && this.tickCount >= 30 ) {
            Entity nearest = SpellBuilder.getNearestEntity(this, level, 3);
            if ( nearest == null ) return;
            zapTarget( this, level, 3, power);
        }
        if ( rune == RunicItemsItems.MAGICAL_CLOUD_RUNE.get() && nextSpellSlot >= 0 && this.tickCount % 40 == 0 && this.tickCount >= 40 ) {
            Entity nearest = SpellBuilder.getNearestEntity(this, level, 3);
            if ( nearest == null ) return;
            SpellBuilder.lookAt(this, nearest);
            SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1);
        }
    }

    @Override
    protected void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            for (int i = 0; i < 8; ++i) {
                level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + 0.1 + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        if ( rune == RunicItemsItems.STORMY_CLOUD_RUNE.get() ) {
            return ParticleTypes.SMOKE;
        }
        else if ( rune == RunicItemsItems.MAGICAL_CLOUD_RUNE.get() ) {
            return ParticleTypes.WITCH;
        }
        else return ParticleTypes.ASH;
    }
}
