package net.mindoth.runicitems.entity.summon;

import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class BlazeMinionEntity extends Blaze implements SummonedMinion {

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public BlazeMinionEntity(EntityType<? extends Blaze> pEntityType, Level pLevel) {
        super(RunicItemsEntities.BLAZE_MINION.get(), pLevel);
        xpReward = 0;
    }

    public BlazeMinionEntity(Level pLevel, LivingEntity owner) {
        this(RunicItemsEntities.BLAZE_MINION.get(), pLevel);
        setSummoner(owner);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new BlazeAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.FOLLOW_RANGE, 48.0D).build();
    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level, cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if ( owner != null ) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, RunicItemsEffects.BLAZE_TIMER.get());
        super.onRemovedFromWorld();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedSummon(pEntity);
    }

    @Override
    public void onUnSummon() {
        if ( !level.isClientSide ) discard();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BlazeMinionEntity.class, EntityDataSerializers.BYTE);

    void setCharged(boolean pCharged) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pCharged) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    class BlazeAttackGoal extends Goal {
        private final BlazeMinionEntity blaze;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        public BlazeAttackGoal(BlazeMinionEntity pBlaze) {
            this.blaze = pBlaze;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.blaze.getTarget();
            return livingentity != null && livingentity.isAlive() && this.blaze.canAttack(livingentity);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.attackStep = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            BlazeMinionEntity.this.setCharged(false);
            this.lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.blaze.getTarget();
            if (livingentity != null) {
                boolean flag = this.blaze.getSensing().hasLineOfSight(livingentity);
                if (flag) {
                    this.lastSeen = 0;
                } else {
                    ++this.lastSeen;
                }

                double d0 = this.blaze.distanceToSqr(livingentity);
                if (d0 < 4.0D) {
                    if (!flag) {
                        return;
                    }

                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.blaze.doHurtTarget(livingentity);
                    }

                    this.blaze.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    double d1 = livingentity.getX() - this.blaze.getX();
                    double d2 = livingentity.getY(0.5D) - this.blaze.getY(0.5D);
                    double d3 = livingentity.getZ() - this.blaze.getZ();
                    if (this.attackTime <= 0) {
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                            this.blaze.setCharged(true);
                        } else if (this.attackStep <= 4) {
                            this.attackTime = 6;
                        } else {
                            this.attackTime = 100;
                            this.attackStep = 0;
                            this.blaze.setCharged(false);
                        }

                        if (this.attackStep > 1) {
                            double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5D;
                            if (!this.blaze.isSilent()) {
                                this.blaze.level.levelEvent((Player)null, 1018, this.blaze.blockPosition(), 0);
                            }

                            for(int i = 0; i < 1; ++i) {
                                SmallFireball smallfireball = new SmallFireball(this.blaze.level, this.blaze, this.blaze.getRandom().triangle(d1, 2.297D * d4), d2, this.blaze.getRandom().triangle(d3, 2.297D * d4));
                                smallfireball.setPos(smallfireball.getX(), this.blaze.getY(0.5D) + 0.5D, smallfireball.getZ());
                                this.blaze.level.addFreshEntity(smallfireball);
                            }
                        }
                    }

                    this.blaze.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                } else if (this.lastSeen < 5) {
                    this.blaze.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
