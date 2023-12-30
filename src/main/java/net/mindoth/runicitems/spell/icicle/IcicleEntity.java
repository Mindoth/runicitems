package net.mindoth.runicitems.spell.icicle;

import net.mindoth.runicitems.particle.GlowParticleData;
import net.mindoth.runicitems.particle.ParticleColor;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.mindoth.runicitems.spell.abstractspell.AbstractProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.items.IItemHandler;

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

    public IcicleEntity(World level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                        HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.ICICLE.get(), level, owner, caster, itemHandler, slot, effects);
    }

    public IcicleEntity(EntityType entityType, World level) {
        super(entityType, level);
    }

    public IcicleEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
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
    protected void spawnParticles() {
        Vector3d vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for ( int i = 0; i < 4; ++i ) {
            ServerWorld level = (ServerWorld)this.level;
            level.sendParticles(GlowParticleData.createData(getParticleColor()), this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, 0, -d5, -d6 + 0.2D, -d1, 0);
        }
    }

    @Override
    protected ParticleColor getParticleColor(){
        return new ParticleColor(49F, 119F, 249F);
    }
}
