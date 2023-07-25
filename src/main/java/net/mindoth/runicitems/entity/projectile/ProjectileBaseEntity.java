package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.item.wand.WandItem;
import net.mindoth.shadowizardlib.event.CommonEvents;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class ProjectileBaseEntity extends ThrowableItemProjectile {
    private float baseDamage = 0.0f;
    private boolean hasFire = false;
    private boolean hasExplosion = false;

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> pEntityType, Level pLevel, LivingEntity pShooter, float power, boolean hasFire, boolean hasExplosion) {
        super(pEntityType, pShooter, pLevel);
        this.setDamage(power);
        this.setFire(hasFire);
        this.setExplosion(hasExplosion);
    }

    public void setDamage(float damage) {
        this.baseDamage = damage;
    }

    public float getDamage() {
        return this.baseDamage;
    }

    public void setFire(boolean hasFire) {
        this.hasFire = hasFire;
    }

    public boolean getFire() {
        return this.hasFire;
    }

    public void setExplosion(boolean hasExplosion) {
        this.hasExplosion = hasExplosion;
    }

    public boolean getExplosion() {
        return this.hasExplosion;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected float getGravity() {
        return 0.03f;
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }
        spawnParticles();
    }

    private void spawnParticles() {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            level.sendParticles(this.getParticle(), getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity caster = getOwner();

        if ( level.isClientSide ) return;

        if ( result.getEntity() instanceof Mob living ) {
            hurtTarget(living, caster);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Entity caster = getOwner();
        BlockPos blockPos = result.getBlockPos().relative(result.getDirection());

        if ( level.isClientSide ) return;

        doBlockEffects(blockPos, caster);

        this.discard();
    }



    protected void hurtTarget(Mob target, Entity caster) {
        if ( getDamage() > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, caster), getDamage());
        }
        if ( getFire() ) {
            target.setSecondsOnFire((int)getDamage());
        }
        if ( getExplosion() ) {
            WandItem.causeExplosion(level, (Player)caster, target.getBoundingBox().getCenter());
        }
    }
    protected void doBlockEffects(BlockPos blockPos, Entity caster) {
        if ( getFire() ) {
            if ( level.isEmptyBlock(blockPos) ) {
                level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(level, blockPos));
            }
        }
        if ( getExplosion() ) {
            Vec3 onBlock = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            WandItem.causeExplosion(level, (Player)caster, onBlock);
        }
    }



    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
