package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.spell.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;

public class ProjectileBaseEntity extends ThrowableItemProjectile {

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected ProjectileBaseEntity(EntityType<? extends ProjectileBaseEntity> pEntityType, Level pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        super(pEntityType, owner, pLevel);
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

    protected void hurtTarget(LivingEntity target) {
        this.discard();
    }

    protected void doBlockEffects(HitResult result) {
        if ( SpellBuilder.getBounce(effects) ) {
            BlockHitResult traceResult = (BlockHitResult) result;
            BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
            if (!blockstate.getCollisionShape(this.level, traceResult.getBlockPos()).isEmpty()) {
                Direction face = traceResult.getDirection();
                blockstate.onProjectileHit(this.level, blockstate, traceResult, this);

                Vec3 motion = this.getDeltaMovement();

                double motionX = motion.x();
                double motionY = motion.y();
                double motionZ = motion.z();


                if (face == Direction.EAST)
                    motionX = -motionX;
                else if (face == Direction.SOUTH)
                    motionZ = -motionZ;
                else if (face == Direction.WEST)
                    motionX = -motionX;
                else if (face == Direction.NORTH)
                    motionZ = -motionZ;
                else if (face == Direction.UP)
                    motionY = -motionY;
                else if (face == Direction.DOWN)
                    motionY = -motionY;

                this.setDeltaMovement(motionX, motionY, motionZ);
            }
        }
        else this.discard();
    }

    @Override
    protected void onHit(HitResult result) {
        if ( level.isClientSide ) return;

        if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ) {
            hurtTarget(living);
            splashParticles(living);
        }

        if ( result.getType() == HitResult.Type.BLOCK ) {
            doBlockEffects(result);
        }

        if ( SpellBuilder.getTrigger(effects) && SpellBuilder.getNextSpellSlot(slot, itemHandler) >= 0 ) {
            caster = this;
            SpellBuilder.cast((Player)owner, caster, itemHandler, slot + 1);
        }
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

    private void splashParticles(LivingEntity target) {
        if ( !this.level.isClientSide ) {
            ServerLevel level = (ServerLevel)this.level;
            for (int i = 0; i < 1 + 2 * SpellBuilder.getPower(effects); ++i) {
                level.sendParticles(this.getParticle(), target.getBoundingBox().getCenter().x, target.getBoundingBox().getCenter().y, target.getBoundingBox().getCenter().z, 1, target.getBoundingBox().getXsize() / 2 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getYsize() / 4 + this.random.nextDouble() * 0.15F, target.getBoundingBox().getZsize() / 2 + this.random.nextDouble() * 0.15F, 0);
            }
        }
    }

    @Override
    protected float getGravity() {
        return 0.1F;
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
