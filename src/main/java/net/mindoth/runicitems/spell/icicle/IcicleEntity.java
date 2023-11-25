package net.mindoth.runicitems.spell.icicle;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractProjectileEntity;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;

public class IcicleEntity extends AbstractProjectileEntity {

    @Override
    protected int getBasePower() {
        return 6;
    }

    @Override
    protected int getBaseEnemyPiercing() {
        return 1;
    }

    public IcicleEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                        HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.ICICLE.get(), level, owner, caster, itemHandler, slot, effects);
    }

    public IcicleEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public IcicleEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(RunicItemsEntities.ICICLE.get(), level);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
    }

    @Override
    protected void addEffects(LivingEntity target) {
        int ticksToAdd = (48 * this.power) - target.getTicksFrozen();
        if ( ticksToAdd > 0 ) target.setTicksFrozen(ticksToAdd);
    }

    @Override
    protected void spawnParticles() {
        if ( this.tickCount % 4 != 0 ) return;
        spawnBonusParticles();
        ServerLevel level = (ServerLevel)this.level;
        level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x, getParticleHeight(), CommonEvents.getEntityCenter(this).z, 1, 0, 0, 0, 0);
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SNOWFLAKE;
    }
}
