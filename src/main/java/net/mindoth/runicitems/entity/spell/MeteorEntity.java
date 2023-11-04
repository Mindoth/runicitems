package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;

public class MeteorEntity extends ProjectileBaseEntity {

    public MeteorEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                            HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.METEOR.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    public MeteorEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public MeteorEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(RunicItemsEntities.METEOR.get(), level);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        splashDamage();
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
        if ( !enemyPiercing ) {
            this.discard();
        }
    }

    @Override
    protected void doBlockEffects(HitResult result) {
        if ( !blockPiercing ) {
            splashDamage();
        }
    }

    @Override
    protected void spawnSplashParticles() {
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 2.0f, 0.5f);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 2.0f, 0.5f);
        ServerLevel level = (ServerLevel)this.level;
        for (int i = 0; i < this.power * 2; ++i) {
            level.sendParticles(ParticleTypes.FLAME, this.getBoundingBox().getCenter().x, this.getBoundingBox().getCenter().y, this.getBoundingBox().getCenter().z, 1, 0, 0, 0, 0.5f);
        }
        for (int i = 0; i < this.power * 4; ++i) {
            level.sendParticles(ParticleTypes.SMOKE, this.getBoundingBox().getCenter().x, this.getBoundingBox().getCenter().y, this.getBoundingBox().getCenter().z, 1, 0, 0, 0, 0.5f);
        }
    }

    @Override
    protected void addEffects(LivingEntity target) {
        if ( !ice ) target.setSecondsOnFire(this.power);
    }

    @Override
    protected void spawnParticles() {
        ServerLevel level = (ServerLevel)this.level;
        for (int i = 0; i < this.power / 2; ++i) {
            level.sendParticles(ParticleTypes.FLAME, CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
        }
        for (int i = 0; i < this.power; ++i) {
            level.sendParticles(ParticleTypes.SMOKE, CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.90F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
        }
    }
}
