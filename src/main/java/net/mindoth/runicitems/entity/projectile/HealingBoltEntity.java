package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.entity.ProjectileBaseEntity;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class HealingBoltEntity extends ProjectileBaseEntity {

    public HealingBoltEntity(EntityType<HealingBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public HealingBoltEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.MAGIC_SPARK.get(), level, owner, caster, itemHandler, slot, effects);
    }

    @Override
    protected void classHurtTarget(LivingEntity target) {
        int heal = 1 + power;
        if ( heal > 0 ) {
            target.heal(heal);
        }
        this.discard();
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }
}
