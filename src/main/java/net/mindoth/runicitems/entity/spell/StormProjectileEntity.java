package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsParticles;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;

public class StormProjectileEntity extends AbstractProjectileEntity {

    @Override
    protected int getBasePower() {
        return 6;
    }

    public StormProjectileEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                        HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.STORM_PROJECTILE.get(), level, owner, caster, itemHandler, slot, effects);
    }

    public StormProjectileEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
    }

    @Override
    protected void spawnBonusParticles() {
        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for ( int i = 0; i < 4; ++i ) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(RunicItemsParticles.GLOW_STORM_PARTICLE.get(), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, 0, -d5, -d6 + 0.2D, -d1, 0);
        }
    }

    @Override
    protected void spawnParticles() {
        spawnBonusParticles();
        ServerLevel level = (ServerLevel)this.level;
        level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), getParticleHeight() + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.20F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 1);
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }
}
