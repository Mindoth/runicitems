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

import java.util.HashMap;

public class IceProjectileEntity extends AbstractProjectileEntity {

    @Override
    protected int getBasePower() {
        return 6;
    }

    public IceProjectileEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                        HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.ICE_PROJECTILE.get(), level, owner, caster, itemHandler, slot, effects);
    }

    public IceProjectileEntity(EntityType entityType, Level level) {
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
    protected void spawnParticles() {
        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for(int i = 0; i < 4; ++i) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(this.getParticle(), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, 0, -d5, -d6 + 0.2D, -d1, 0);
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return RunicItemsParticles.GLOW_ICE_PARTICLE.get();
    }
}
