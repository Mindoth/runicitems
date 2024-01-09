package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.client.particle.EmberParticleData;
import net.mindoth.runicitems.client.particle.ParticleColor;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AbstractSpellEntity extends ThrowableEntity {

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, World level) {
        super(entityType, level);
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, World pLevel, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(entityType, owner, pLevel);

        this.owner = owner;
        this.caster = caster;
        this.spell = spell;

        this.element = element;
        this.scale = scale;
        this.power = this.spell.getPower();
        this.life = this.spell.getLife();
        setColor(getSpellColor(this.element), this.scale);
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected AbstractSpell spell;

    protected String element;
    protected float scale;
    protected float power;
    protected float life;

    protected boolean isAlly(LivingEntity target) {
        return !(target == this.owner || target.isAlliedTo(this.owner) || (target instanceof TameableEntity && ((TameableEntity)target).isOwnedBy(this.owner)));
    }

    protected void dealDamage(LivingEntity target) {
        if ( isAlly(target) ) {
            target.hurt(DamageSource.indirectMagic(this, this.owner), this.power);
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if ( level.isClientSide ) {
            doClientEffects();
        }
        if ( !level.isClientSide ) {
            if ( result.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult)result).getEntity() instanceof LivingEntity ) {
                doMobEffects(result);
            }
            if ( result.getType() == RayTraceResult.Type.BLOCK ) {
                doBlockEffects(result);
                BlockRayTraceResult traceResult = (BlockRayTraceResult)result;
                BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
                level.playSound(null, this.getX(), this.getY(), this.getZ(), blockstate.getSoundType().getBreakSound(), SoundCategory.PLAYERS, 0.2F, 2);
                this.remove();
            }
            playHitSound();
        }
    }

    protected void doClientEffects() {
    }

    protected void doMobEffects(RayTraceResult result) {
    }

    protected void doBlockEffects(RayTraceResult result) {
    }

    @Override
    public void tick() {
        super.tick();
        if ( level.isClientSide ) {
            doClientTickEffects();
            Vector3d vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            Vector3d center = CommonEvents.getEntityCenter(this);
            ClientWorld world = (ClientWorld)this.level;
            for ( int i = 0; i < 4; ++i ) {
                world.addParticle(EmberParticleData.createData(getParticleColor(), entityData.get(SIZE), 10), true,
                        center.x + d5 * (double)i / 4.0D, center.y + d6 * (double)i / 4.0D, center.z + d1 * (double)i / 4.0D, 0, 0, 0);
            }
        }
        if ( !level.isClientSide ) {
            doTickEffects();
            if ( this.tickCount > this.life ) {
                this.remove();
            }
        }
    }

    protected void doClientTickEffects() {
    }

    protected void doTickEffects() {
    }

    public static ParticleColor.IntWrapper getSpellColor(String element) {
        ParticleColor.IntWrapper returnColor = null;
        if ( element.equals("frost") ) returnColor = new ParticleColor.IntWrapper(49, 119, 249);
        if ( element.equals("storm") ) returnColor = new ParticleColor.IntWrapper(206, 0, 206);
        if ( element.equals("fire") ) returnColor = new ParticleColor.IntWrapper(177, 63, 0);
        return returnColor;
    }

    protected void playHitSound() {
        Vector3d center = CommonEvents.getEntityCenter(this);
        if ( this.element.equals("frost") ) {
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.GLASS_BREAK, SoundCategory.PLAYERS, 0.25F, 0.75F);
        }
        if ( this.element.equals("fire") ) {
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.25F, 0.75F);
        }
    }

    public static final DataParameter<Integer> RED = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> GREEN = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> BLUE = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Float> SIZE = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.FLOAT);

    public ParticleColor getParticleColor() {
        return new ParticleColor(entityData.get(RED), entityData.get(GREEN), entityData.get(BLUE));
    }

    public void setColor(ParticleColor.IntWrapper colors, float size) {
        entityData.set(RED, colors.r);
        entityData.set(GREEN, colors.g);
        entityData.set(BLUE, colors.b);
        entityData.set(SIZE, size);
    }

    @Override
    public void load(CompoundNBT compound) {
        super.load(compound);
        entityData.set(RED, compound.getInt("red"));
        entityData.set(GREEN, compound.getInt("green"));
        entityData.set(BLUE, compound.getInt("blue"));
        entityData.set(SIZE, compound.getFloat("size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("red", entityData.get(RED));
        compound.putInt("green", entityData.get(GREEN));
        compound.putInt("blue", entityData.get(BLUE));
        compound.putFloat("size", entityData.get(SIZE));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RED, 255);
        this.entityData.define(GREEN, 25);
        this.entityData.define(BLUE, 180);
        this.entityData.define(SIZE, 0.3F);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
