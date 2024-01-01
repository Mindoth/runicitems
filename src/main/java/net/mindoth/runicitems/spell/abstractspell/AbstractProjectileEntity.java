package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.client.particle.GlowParticleData;
import net.mindoth.runicitems.client.particle.ParticleColor;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

public class AbstractProjectileEntity extends ThrowableEntity {

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    public AbstractProjectileEntity(EntityType<? extends AbstractProjectileEntity> entityType, World level) {
        super(entityType, level);
    }

    public AbstractProjectileEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.PROJECTILE.get(), level);
    }

    public AbstractProjectileEntity(World pLevel, LivingEntity owner, Entity caster, IItemHandler itemHandler, AbstractSpell spell, String color) {
        super(RunicItemsEntities.PROJECTILE.get(), owner, pLevel);

        setColor(getSpellColor(color));
        this.owner = owner;
        this.caster = caster;
        this.itemHandler = itemHandler;
        this.spell = spell;

        this.element = color;
        this.power = this.spell.getPower();
        this.life = this.spell.getLife();
    }

    protected LivingEntity owner;
    protected Entity caster;
    protected IItemHandler itemHandler;
    protected AbstractSpell spell;

    protected String element;
    protected float power;
    protected float life;

    protected void doMobEffects(RayTraceResult result) {
        LivingEntity target = (LivingEntity)((EntityRayTraceResult)result).getEntity();
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
    }

    protected void doBlockEffects(RayTraceResult result) {
        BlockRayTraceResult traceResult = (BlockRayTraceResult)result;
        BlockState blockstate = this.level.getBlockState(traceResult.getBlockPos());
        level.playSound(null, this.getX(), this.getY(), this.getZ(), blockstate.getSoundType().getBreakSound(), SoundCategory.PLAYERS, 0.2f, 2);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if ( level.isClientSide ) return;
        if ( result.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult)result).getEntity() instanceof LivingEntity ) {
            doMobEffects(result);
            this.remove();
        }
        if ( result.getType() == RayTraceResult.Type.BLOCK ) {
            doBlockEffects(result);
            this.remove();
        }
        playHitSound();
    }

    @Override
    public void tick() {
        super.tick();
        if ( level.isClientSide ) {
            ClientWorld world = (ClientWorld)this.level;
            Vector3d vec3 = this.getDeltaMovement();
            Vector3d center = CommonEvents.getEntityCenter(this);
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            for ( int i = 0; i < 4; ++i ) {
                world.addParticle(GlowParticleData.createData(getParticleColor()), center.x + d5 * (double)i / 4.0D, center.y + d6 * (double)i / 4.0D, center.z + d1 * (double)i / 4.0D, 0, 0, 0);
            }
        }
        if ( !level.isClientSide ) {
            if ( this.tickCount > this.life ) {
                this.remove();
            }
        }
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



    protected static ParticleColor.IntWrapper getSpellColor(String element) {
        ParticleColor.IntWrapper returnColor = null;
        if ( element.equals("frost") ) returnColor = new ParticleColor.IntWrapper(49, 119, 249);
        if ( element.equals("storm") ) returnColor = new ParticleColor.IntWrapper(206, 0, 206);
        if ( element.equals("fire") ) returnColor = new ParticleColor.IntWrapper(177, 63, 0);
        return returnColor;
    }

    public static final DataParameter<Integer> RED = EntityDataManager.defineId(AbstractProjectileEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> GREEN = EntityDataManager.defineId(AbstractProjectileEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> BLUE = EntityDataManager.defineId(AbstractProjectileEntity.class, DataSerializers.INT);

    public ParticleColor getParticleColor() {
        return new ParticleColor(entityData.get(RED), entityData.get(GREEN), entityData.get(BLUE));
    }

    public ParticleColor.IntWrapper getParticleColorWrapper() {
        return new ParticleColor.IntWrapper(entityData.get(RED), entityData.get(GREEN), entityData.get(BLUE));
    }

    public void setColor(ParticleColor.IntWrapper colors) {
        entityData.set(RED, colors.r);
        entityData.set(GREEN, colors.g);
        entityData.set(BLUE, colors.b);
    }

    @Override
    public void load(CompoundNBT compound) {
        super.load(compound);
        entityData.set(RED, compound.getInt("red"));
        entityData.set(GREEN, compound.getInt("green"));
        entityData.set(BLUE, compound.getInt("blue"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("red", entityData.get(RED));
        compound.putInt("green", entityData.get(GREEN));
        compound.putInt("blue", entityData.get(BLUE));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RED, 255);
        this.entityData.define(GREEN, 25);
        this.entityData.define(BLUE, 180);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
