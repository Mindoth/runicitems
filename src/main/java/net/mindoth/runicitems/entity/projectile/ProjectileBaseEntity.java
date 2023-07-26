package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.spell.SpawnEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

import static java.lang.Math.abs;

public class ProjectileBaseEntity extends ThrowableItemProjectile {

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> pEntityType, Level pLevel, LivingEntity caster, HashMap<String, Integer> effects) {
        super(pEntityType, caster, pLevel);
        this.owner = caster;
        this.effects = effects;
        this.baseDamage = effects.get("power");
    }

    private LivingEntity owner;
    private HashMap<String, Integer> effects;
    private float baseDamage = 0.0f;

    public float getDamage() {
        return this.baseDamage;
    }

    protected void hurtTarget(Mob target) {
        if ( getDamage() > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), getDamage());
        }
        if ( effects.get("explosion") == 1 ) {
            SpawnEffect.causeExplosion(owner, this, effects);
        }
    }
    protected void doBlockEffects() {
        if ( effects.get("explosion") == 1 ) {
            SpawnEffect.causeExplosion(owner, this, effects);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getEntity() instanceof Mob living ) {
            hurtTarget(living);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if ( level.isClientSide ) return;

        doBlockEffects();

        this.discard();
    }



    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }
        if ( this.tickCount > 160 ) {
            this.discard();
        }
        spawnParticles();
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    private void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected float getGravity() {
        return 0.001F;
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
