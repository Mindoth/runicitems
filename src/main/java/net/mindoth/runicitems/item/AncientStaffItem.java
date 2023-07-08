package net.mindoth.runicitems.item;

import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AncientStaffItem extends StaffItem {
    public AncientStaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if ( pLivingEntity instanceof Player player ) {
            int useTime = getUseDuration(pStack) - pRemainingUseDuration;
            if ( useTime > 60 && useTime % 5 == 0 ) {
                if ( !pLevel.isClientSide ) {
                    ServerLevel level = (ServerLevel) pLevel;
                    Vec3 direction = player.getLookAngle().normalize();
                    direction = direction.multiply(1, 1, 1);
                    Position pos = player.getEyePosition().add(direction);
                    float offset = 0.2f;
                    if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack) > 0 ) {
                        level.sendParticles(DustParticleOptions.REDSTONE, pos.x(), pos.y(), pos.z(), 1, offset, offset, offset, 0);
                    }
                    if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack) > 0 ) {
                        level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x(), pos.y(), pos.z(), 1, offset, offset, offset, 0);
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if ( pEntityLiving instanceof Player player && !pLevel.isClientSide ) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPower(i);
            int powerScale = RunicItemsCommonConfig.BARRAGE_POWER_SCALE.get();
            double radiusScale = 2;
            if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.RADIUS.get(), pStack) > 0 ) {
                radiusScale = radiusScale + ((double)EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.RADIUS.get(), pStack) / 2);
            }
            if ( f >= 3.0D ) {
                if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack) > 0 ) {
                    int power = powerScale * EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack);
                    bloodBarrage(player, pLevel, pStack,
                            player.getX() + 3.0 * player.getLookAngle().x,
                            player.getY() + 3.0 * player.getLookAngle().y,
                            player.getZ() + 3.0 * player.getLookAngle().z,
                            radiusScale, power);
                }
                else if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack) > 0 ) {
                    int power = powerScale * EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack);
                    iceBarrage(player, pLevel, pStack,
                            player.getX() + 3.0 * player.getLookAngle().x,
                            player.getY() + 3.0 * player.getLookAngle().y,
                            player.getZ() + 3.0 * player.getLookAngle().z,
                            radiusScale, power);
                }
            }
        }
    }

    public void bloodBarrage( Player player, Level pLevel, ItemStack pStack, double x, double y, double z, double size, int power ) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(x - size, y - size, z - size, x + size, y + size, z + size));
        if ( targets.size() > 0 ) {
            pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                SoundEvents.SILVERFISH_HURT, SoundSource.PLAYERS, 0.25f, 0.25f);
            pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                    SoundEvents.HOE_TILL, SoundSource.PLAYERS, 2, 0.5f);
            pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                    SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
        }
        for ( LivingEntity target : targets ) {
            if ( target != player && !target.isAlliedTo(player) && player.hasLineOfSight(target) ) {
                target.hurt(DamageSource.MAGIC, 4 + (power * 2));
                player.heal(power);
                ServerLevel level = (ServerLevel) pLevel;
                double playerX = player.getBoundingBox().getCenter().x;
                double playerY = player.getBoundingBox().getCenter().y;
                double playerZ = player.getBoundingBox().getCenter().z;
                double listedEntityX = target.getBoundingBox().getCenter().x();
                double listedEntityY = target.getBoundingBox().getCenter().y();
                double listedEntityZ = target.getBoundingBox().getCenter().z();
                int particleInterval = (int)Math.round(player.distanceTo(target)) * 5;
                for ( int k = 1; k < (1 + particleInterval); k++ ) {
                    double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
                    double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
                    double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
                    level.sendParticles(DustParticleOptions.REDSTONE, lineX, lineY, lineZ, 1, 0, 0, 0, 0);
                }
            }
        }
        if ( !player.getAbilities().instabuild ) {
            if ( pStack.getEquipmentSlot() == EquipmentSlot.MAINHAND ) {
                pStack.hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            else if ( pStack.getEquipmentSlot() == EquipmentSlot.OFFHAND ) {
                pStack.hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.OFFHAND));
            }
        }
    }

    public void iceBarrage( Player player, Level pLevel, ItemStack pStack, double x, double y, double z, double size, int power ) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(x - size, y - size, z - size, x + size, y + size, z + size));
        for ( LivingEntity target : targets ) {
            if ( target != player && !target.isAlliedTo(player) && player.hasLineOfSight(target) ) {
                int height = (int) target.getBoundingBox().getYsize();
                target.hurt(DamageSource.MAGIC, 2 + (power * 2));
                target.setTicksFrozen(280 * power);
                if ( RunicItemsCommonConfig.FREEZE_AI.get() ) {
                    if ( target instanceof Mob mob && !(mob instanceof EnderDragon || mob instanceof WitherBoss || mob instanceof ElderGuardian) ) {
                        if (!mob.isNoAi()) {
                            mob.setNoAi(true);
                        }
                    }
                }
                ServerLevel level = (ServerLevel)pLevel;
                for (int j = 0; j < 16 + (16 * height); ++j) {
                    level.sendParticles(ParticleTypes.SNOWFLAKE,
                            target.getBoundingBox().getCenter().x,
                            target.getBoundingBox().getCenter().y,
                            target.getBoundingBox().getCenter().z, 1,
                            target.getBoundingBox().getXsize() / 2,
                            target.getBoundingBox().getYsize() / 4,
                            target.getBoundingBox().getZsize() / 2, 0);
                }
                pLevel.playSound(null, target.getBoundingBox().getCenter().x, target.getBoundingBox().getCenter().y, target.getBoundingBox().getCenter().z,
                        SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1, 1);
            }
        }
        if ( !player.getAbilities().instabuild ) {
            if ( pStack.getEquipmentSlot() == EquipmentSlot.MAINHAND ) {
                pStack.hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            else if ( pStack.getEquipmentSlot() == EquipmentSlot.OFFHAND ) {
                pStack.hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.OFFHAND));
            }
        }
    }
}
