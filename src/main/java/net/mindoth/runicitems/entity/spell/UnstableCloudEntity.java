package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEntities;
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
        if ( this.tickCount % 30 != 0 ) return;
        level.playSound(null, center.x, center.y, center.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.5F, 1.25F);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.HOE_TILL, SoundSource.PLAYERS, 1, 0.5F);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 1, 2);
        if ( !(level instanceof ServerLevel serverLevel) ) return;
        serverLevel.sendParticles(ParticleTypes.FIREWORK, center.x, center.y, center.z, 1, 0, 0, 0, (double)this.range / 6);
    }

    @Override
    protected void doDecayEffects() {
        Level level = this.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(this);
        ArrayList<LivingEntity> targets = SpellBuilder.getEntitiesAround(this, level, this.range);
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
    protected SimpleParticleType getParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }
}
