package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

public class SpellBaseEntity extends ThrowableItemProjectile {

    protected SpellBaseEntity(EntityType<? extends SpellBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected SpellBaseEntity(EntityType<? extends SpellBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Item rune) {
        super(pEntityType, owner, pLevel);
        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.slot = slot;
        this.rune = rune;
        this.trigger = SpellBuilder.getTrigger(effects);
        this.deathTrigger = SpellBuilder.getDeathTrigger(effects);
        this.nextSpellSlot = SpellBuilder.getNextSpellSlot(slot, itemHandler);
        this.power = SpellBuilder.getPower(effects);
        this.bounces = SpellBuilder.getBounce(effects);
        this.life = Math.max(10, 160 + SpellBuilder.getLife(effects));
        this.fire = SpellBuilder.getFire(effects);
        this.ice = SpellBuilder.getIce(effects);
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected int slot;
    protected Item rune;
    protected boolean trigger;
    protected boolean deathTrigger;
    protected int nextSpellSlot;
    protected int power;
    protected int bounces;
    protected int life;
    protected boolean fire;
    protected boolean ice;

    @Override
    protected void onHit(HitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ) {
            hurtTarget(living);
        }

        if ( result.getType() == HitResult.Type.BLOCK ) {
            doBlockEffects(result);
        }

        if ( trigger && nextSpellSlot >= 0 ) {
            caster = this;
            SpellBuilder.cast((Player)owner, caster, itemHandler, slot + 1);
        }
    }

    protected void hurtTarget(LivingEntity target) {
    }

    protected void addEffects(LivingEntity target) {
    }

    protected void doBlockEffects(HitResult result) {
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }
        if ( this.tickCount > life ) {
            if ( deathTrigger && nextSpellSlot >= 0 ) {
                caster = this;
                SpellBuilder.cast((Player)owner, caster, itemHandler, slot + 1);
            }
            this.discard();
        }
    }

    protected void spawnParticles() {
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    protected float getGravity() {
        return 0.025F;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
