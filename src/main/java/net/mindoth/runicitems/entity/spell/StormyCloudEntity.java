package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.event.SpellBuilder;
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

public class StormyCloudEntity extends FamiliarBaseEntity {

    public StormyCloudEntity(EntityType<StormyCloudEntity> entityType, Level level) {
        super(entityType, level);
    }

    public StormyCloudEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                             HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.STORMY_CLOUD.get(), level, owner, caster, itemHandler, slot, effects, rune);
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
        if ( this.tickCount % 10 == 0 && this.tickCount > Math.min(Math.max(10 / this.speed, 1), 40) ) {
            Entity nearest = SpellBuilder.getNearestEntity(this, level, this.range);
            if ( nearest == null ) return;
            zapTarget(this, level, this.range, this.power);
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SMOKE;
    }
}
