package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;

public class WitherSkullEntity extends ProjectileBaseEntity {

    public WitherSkullEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                            HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.WITHER_SKULL.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    public WitherSkullEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public WitherSkullEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(RunicItemsEntities.WITHER_SKULL.get(), level);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
        if ( !enemyPiercing ) {
            this.discard();
        }
        explode();
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
    }

    @Override
    protected void doBlockEffects(HitResult result) {
        if ( !blockPiercing ) {
            explode();
        }
    }

    private void explode() {
        level.explode(null, DamageSource.MAGIC, null, this.getX(), this.getY(), this.getZ(), 1, false, Explosion.BlockInteraction.NONE);
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SMOKE;
    }
}
