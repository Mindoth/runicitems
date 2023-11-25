package net.mindoth.runicitems.spell.unstablecloud;

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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class UnstableCloudEntity extends AbstractCloudEntity {

    public UnstableCloudEntity(EntityType<UnstableCloudEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected int getBasePower() {
        return 10;
    }

    public UnstableCloudEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                         HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.UNSTABLE_CLOUD.get(), level, owner, caster, itemHandler, slot, effects);
    }

    @Override
    protected void doTickEffects() {
        Level level = this.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(this);
        if ( this.tickCount % 5 != 0 ) return;
        level.playSound(null, center.x, center.y, center.z, SoundEvents.HOE_TILL, SoundSource.PLAYERS, 0.75F, 0.5F);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 0.75F, 2);
    }

    @Override
    protected void doDecayEffects() {
        Level level = this.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(this);
        ArrayList<LivingEntity> targets = CommonEvents.getEntitiesAround(this, level, this.range);
        for ( LivingEntity target : targets ) {
            target.hurt(DamageSource.MAGIC, power);
        }
        level.playSound(null, center.x, center.y, center.z, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 2, 0.5F);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 2, 1);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 2, 1);
        if ( !(level instanceof ServerLevel serverLevel) ) return;
        for ( int i = 0; i < 360; i++ ) {
            serverLevel.sendParticles(ParticleTypes.CRIT, center.x, center.y, center.z, 0, Math.cos(i), 0, Math.sin(i), 0.2D + this.range);
        }
    }

    @Override
    protected void spawnParticles() {
        ServerLevel level = (ServerLevel)this.level;
        for (int i = 0; i < 8; ++i) {
            level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x, CommonEvents.getEntityCenter(this).y, CommonEvents.getEntityCenter(this).z, 1, 0, 0, 0, 1);
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }
}
