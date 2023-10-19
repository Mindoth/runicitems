package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class ProjectileBaseEntity extends SpellBaseEntity {

    public ProjectileBaseEntity(EntityType<ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileBaseEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.PROJECTILE_BASE.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( RunicItemsItems.MAGIC_SPARK_RUNE.get().equals(rune) ) {
            if ( power > 0 ) {
                target.hurt(DamageSource.indirectMagic(this, owner), power);
            }
        }
        else if ( RunicItemsItems.MAGIC_SPARK_RUNE.get().equals(rune) ) {
            if ( power > 0 ) {
                target.heal(power);
            }
        }
        this.discard();
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

    @Override
    protected void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);

        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        if ( rune == RunicItemsItems.MAGIC_SPARK_RUNE.get() ) {
            return ParticleTypes.END_ROD;
        }
        else if ( rune == RunicItemsItems.HEALING_BOLT_RUNE.get() ) {
            return ParticleTypes.HAPPY_VILLAGER;
        }
        else return ParticleTypes.ASH;
    }
}
