package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.ClientReference;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class ProjectileBaseEntity extends SpellBaseEntity {

    //TODO make each projectile spell their own class so sprites work in Renderer

    public ProjectileBaseEntity(EntityType<ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileBaseEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                                HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        super(RunicItemsEntities.PROJECTILE_BASE.get(), level, owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( RunicItemsItems.MAGIC_SPARK_RUNE.get().equals(rune) ) {
            if ( power > 0 ) {
                target.hurt(DamageSource.indirectMagic(this, owner), power);
            }
        }
        else if ( RunicItemsItems.HEALING_BOLT_RUNE.get().equals(rune) ) {
            if ( power > 0 ) {
                target.heal(power);
            }
        }
        if ( !enemyPiercing ) {
            this.discard();
        }
    }

    @Override
    protected void addEffects(LivingEntity target) {
        if ( fire && !ice ) {
            target.setSecondsOnFire(5);
        }
        if ( ice && !fire ) {
            target.setTicksFrozen(560);
        }
    }

    @Override
    protected void spawnParticles() {
        ServerLevel level = (ServerLevel)this.level;
        level.sendParticles(this.getParticle(), CommonEvents.getEntityCenter(this).x + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).y + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), CommonEvents.getEntityCenter(this).z + this.random.nextDouble() * 0.10F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
    }

    @Override
    protected void spawnEffectParticles() {
        ServerLevel level = (ServerLevel)this.level;
        if (fire && !ice) level.sendParticles(ParticleTypes.FLAME, CommonEvents.getEntityCenter(this).x, CommonEvents.getEntityCenter(this).y, CommonEvents.getEntityCenter(this).z, 1, 0, 0, 0, 0);
        if (ice && !fire) level.sendParticles(ParticleTypes.SNOWFLAKE, CommonEvents.getEntityCenter(this).x, CommonEvents.getEntityCenter(this).y, CommonEvents.getEntityCenter(this).z, 1, 0, 0, 0, 0);
    }

    @Override
    protected SimpleParticleType getParticle() {
        if ( rune == RunicItemsItems.MAGIC_SPARK_RUNE.get() ) {
            return ParticleTypes.END_ROD;
        }
        else if ( rune == RunicItemsItems.HEALING_BOLT_RUNE.get() ) {
            return ParticleTypes.HAPPY_VILLAGER;
        }
        else return ParticleTypes.ASH;
    }

    @Override
    public ResourceLocation getSpellTexture() {
        return ClientReference.MAGIC_SPARK;
    }
}
