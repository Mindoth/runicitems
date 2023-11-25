package net.mindoth.runicitems.spell.stormycloud;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractCloudEntity;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class StormyCloudEntity extends AbstractCloudEntity {

    public StormyCloudEntity(EntityType<StormyCloudEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected int getBasePower() {
        return 2;
    }

    public StormyCloudEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                             HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.STORMY_CLOUD.get(), level, owner, caster, itemHandler, slot, effects);
    }

    protected void zapTarget(Entity caster, Level pLevel, double size, int power) {
        Entity target = CommonEvents.getNearestEntity(caster, pLevel, size);
        if ( target == null ) return;
        target.hurt(DamageSource.MAGIC, power);
        ServerLevel level = (ServerLevel) pLevel;
        double playerX = CommonEvents.getEntityCenter(caster).x;
        double playerY = CommonEvents.getEntityCenter(caster).y;
        double playerZ = CommonEvents.getEntityCenter(caster).z;
        double listedEntityX = CommonEvents.getEntityCenter(target).x();
        double listedEntityY = CommonEvents.getEntityCenter(target).y();
        double listedEntityZ = CommonEvents.getEntityCenter(target).z();
        int particleInterval = (int)Math.round(caster.distanceTo(target)) * 5;
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, lineX, lineY, lineZ, 1, 0, 0, 0, 0);
        }
        pLevel.playSound(null, caster.getBoundingBox().getCenter().x, caster.getBoundingBox().getCenter().y, caster.getBoundingBox().getCenter().z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.1f, 2);
        pLevel.playSound(null, caster.getBoundingBox().getCenter().x, caster.getBoundingBox().getCenter().y, caster.getBoundingBox().getCenter().z,
                SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
    }

    @Override
    public void doTickEffects() {
        if ( this.tickCount % 10 == 0 ) {
            Entity nearest = CommonEvents.getNearestEntity(this, level, this.range);
            if ( nearest == null ) return;
            zapTarget(this, level, this.range, this.power);
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SMOKE;
    }
}
