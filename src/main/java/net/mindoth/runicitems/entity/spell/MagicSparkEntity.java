package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
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

public class MagicSparkEntity extends ProjectileBaseEntity {

    public MagicSparkEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                            HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.MAGIC_SPARK.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    public MagicSparkEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public MagicSparkEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(RunicItemsEntities.MAGIC_SPARK.get(), level);
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
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }
}
