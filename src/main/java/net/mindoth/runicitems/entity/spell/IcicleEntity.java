package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
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
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SNOWFLAKE;
    }
}
