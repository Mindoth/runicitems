package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.damagesource.DamageSource;
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
        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;
        this.effects = effects;
    }

    private LivingEntity owner;
    private Entity caster;
    private IItemHandler itemHandler;
    private int slot;
    private HashMap<Item, Integer> effects;

    @Override
    protected void hurtTarget(LivingEntity target) {
        int power = 1 + SpellBuilder.getPower(effects);
        if ( power > 0 ) {
            target.heal(power);
        }
        if ( SpellBuilder.getFire(effects) ) {
            target.setSecondsOnFire(5);
        }

        this.discard();
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }
}
