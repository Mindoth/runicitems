package net.mindoth.runicitems.item;

import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AncientStaffItem extends StaffItem {
    public AncientStaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack) > 0
        || EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack) > 0 ) {
            int useTime = getUseDuration(pStack) - pRemainingUseDuration;
            if ( useTime > 60 && useTime % 10 == 0 ) {
                if ( !pLevel.isClientSide ) {
                    ServerLevel level = (ServerLevel) pLevel;
                    if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack) > 0 ) {
                        for (int i = 0; i < 8; ++i) {
                            level.sendParticles(ParticleTypes.SMOKE,
                                    pLivingEntity.getBoundingBox().getCenter().x,
                                    pLivingEntity.getBoundingBox().getCenter().y,
                                    pLivingEntity.getBoundingBox().getCenter().z, 1,
                                    pLivingEntity.getBoundingBox().getXsize() / 2,
                                    pLivingEntity.getBoundingBox().getYsize() / 4,
                                    pLivingEntity.getBoundingBox().getZsize() / 2, 0);
                        }
                    }
                    if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack) > 0 ) {
                        for (int i = 0; i < 8; ++i) {
                            level.sendParticles(ParticleTypes.SNOWFLAKE,
                                    pLivingEntity.getBoundingBox().getCenter().x,
                                    pLivingEntity.getBoundingBox().getCenter().y,
                                    pLivingEntity.getBoundingBox().getCenter().z, 1,
                                    pLivingEntity.getBoundingBox().getXsize() / 2,
                                    pLivingEntity.getBoundingBox().getYsize() / 4,
                                    pLivingEntity.getBoundingBox().getZsize() / 2, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if ( pEntityLiving instanceof Player player ) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPower(i);
            if ( pStack.getEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get()) > 0
            || pStack.getEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get()) > 0 ) {
                if ( f >= 3.0D ) {
                    List<Entity> aroundCaster;
                    aroundCaster = pLevel.getEntities(player, player.getBoundingBox().inflate(64.0D, 64.0D, 64.0D), Entity::isAlive);
                    aroundCaster.removeIf(closeEntities -> closeEntities.isAlliedTo(player));
                    aroundCaster.removeIf(closeEntities -> !(closeEntities instanceof LivingEntity));
                    aroundCaster.removeIf(closeEntities -> !(isLookingAtMe(player, closeEntities)));
                    if ( aroundCaster.size() > 0 ) {
                        Entity listedEntity = getClosestEntity(aroundCaster);
                        if ( listedEntity != null ) {
                            if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack) > 0 ) {
                                int tier = EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.BLOOD_BARRAGE.get(), pStack);
                                List<Entity> entitiesAround;
                                entitiesAround = pLevel.getEntities(listedEntity, listedEntity.getBoundingBox().inflate(tier, 0.0D, tier), Entity::isAlive);
                                entitiesAround.add(listedEntity);
                                entitiesAround.removeIf(closeEntities -> closeEntities.isAlliedTo(player));
                                entitiesAround.removeIf(closeEntities -> !(closeEntities instanceof LivingEntity));
                                entitiesAround.remove(player);
                                if ( entitiesAround.size() > 0 ) {
                                    if ( !pLevel.isClientSide ) {
                                        pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                                                SoundEvents.SILVERFISH_HURT, SoundSource.PLAYERS, 0.25f, 0.25f);
                                        pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                                                SoundEvents.HOE_TILL, SoundSource.PLAYERS, 2, 0.5f);
                                        pLevel.playSound(null, player.getBoundingBox().getCenter().x, player.getBoundingBox().getCenter().y, player.getBoundingBox().getCenter().z,
                                                SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
                                    }
                                    for ( Entity damageableEntities : entitiesAround ) {
                                        int height = (int) damageableEntities.getBoundingBox().getYsize();
                                        if ( !pLevel.isClientSide ) {
                                            damageableEntities.hurt(DamageSource.MAGIC, 4 + (tier * 2));
                                            player.heal(tier);
                                            ServerLevel level = (ServerLevel) pLevel;
                                            for ( int j = 0; j < 4 + (4 * height); ++j ) {
                                                level.sendParticles(DustParticleOptions.REDSTONE,
                                                        damageableEntities.getBoundingBox().getCenter().x,
                                                        damageableEntities.getBoundingBox().getCenter().y,
                                                        damageableEntities.getBoundingBox().getCenter().z, 1,
                                                        damageableEntities.getBoundingBox().getXsize() / 2,
                                                        damageableEntities.getBoundingBox().getYsize() / 4,
                                                        damageableEntities.getBoundingBox().getZsize() / 2, 0);
                                            }
                                            for ( int j = 0; j < 4 + (4 * height); ++j ) {
                                                level.sendParticles(ParticleTypes.SMOKE,
                                                        damageableEntities.getBoundingBox().getCenter().x,
                                                        damageableEntities.getBoundingBox().getCenter().y,
                                                        damageableEntities.getBoundingBox().getCenter().z, 1,
                                                        damageableEntities.getBoundingBox().getXsize() / 2,
                                                        damageableEntities.getBoundingBox().getYsize() / 4,
                                                        damageableEntities.getBoundingBox().getZsize() / 2, 0);
                                            }
                                            double playerX = player.getBoundingBox().getCenter().x;
                                            double playerY = player.getBoundingBox().getCenter().y;
                                            double playerZ = player.getBoundingBox().getCenter().z;
                                            double listedEntityX = damageableEntities.getBoundingBox().getCenter().x();
                                            double listedEntityY = damageableEntities.getBoundingBox().getCenter().y();
                                            double listedEntityZ = damageableEntities.getBoundingBox().getCenter().z();
                                            int particleInterval = (int) Math.round(player.distanceTo(damageableEntities)) * 5;
                                            for ( int k = 1; k < (1 + particleInterval); k++ ) {
                                                double x = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
                                                double y = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
                                                double z = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
                                                level.sendParticles(DustParticleOptions.REDSTONE, x, y, z, 1, 0, 0, 0, 0);
                                            }
                                        }
                                    }
                                }
                            }
                            if ( EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack) > 0 ) {
                                int tier = EnchantmentHelper.getItemEnchantmentLevel(RunicItemsEnchantments.ICE_BARRAGE.get(), pStack);
                                List<Entity> entitiesAround;
                                entitiesAround = pLevel.getEntities(listedEntity, listedEntity.getBoundingBox().inflate(tier, 0.0D, tier), Entity::isAlive);
                                entitiesAround.add(listedEntity);
                                entitiesAround.removeIf(closeEntities -> closeEntities.isAlliedTo(player));
                                entitiesAround.removeIf(closeEntities -> !(closeEntities instanceof LivingEntity));
                                entitiesAround.remove(player);
                                if ( entitiesAround.size() > 0 ) {
                                    for ( Entity damageableEntities : entitiesAround ) {
                                        int height = (int) damageableEntities.getBoundingBox().getYsize();
                                        if ( !pLevel.isClientSide ) {
                                            damageableEntities.hurt(DamageSource.MAGIC, 2 + (tier * 2));
                                            damageableEntities.setTicksFrozen(140 * tier);
                                            if ( damageableEntities instanceof Mob mob ) {
                                                if ( !mob.isNoAi() ) {
                                                    mob.setNoAi(true);
                                                }
                                            }
                                            ServerLevel level = (ServerLevel)pLevel;
                                            for ( int j = 0; j < 16 + (16 * height); ++j ) {
                                                level.sendParticles(ParticleTypes.SNOWFLAKE,
                                                        damageableEntities.getBoundingBox().getCenter().x,
                                                        damageableEntities.getBoundingBox().getCenter().y,
                                                        damageableEntities.getBoundingBox().getCenter().z, 1,
                                                        damageableEntities.getBoundingBox().getXsize() / 2,
                                                        damageableEntities.getBoundingBox().getYsize() / 4,
                                                        damageableEntities.getBoundingBox().getZsize() / 2, 0);
                                            }
                                            pLevel.playSound(null, damageableEntities.getBoundingBox().getCenter().x, damageableEntities.getBoundingBox().getCenter().y, damageableEntities.getBoundingBox().getCenter().z,
                                                    SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Entity getClosestEntity(List<Entity> list) {
        double lowestSoFar = Double.MAX_VALUE;
        Entity listedEntity = null;
        for ( Entity target : list ) {
            for ( Entity closestSoFar : list ) {
                double testDistance = target.distanceTo(closestSoFar);
                if ( testDistance < lowestSoFar ) {
                    lowestSoFar = testDistance;
                    listedEntity = closestSoFar;
                }
            }
        }
        return listedEntity;
    }

    public static boolean isLookingAtMe(Player pPlayer, Entity entity) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(entity.getX() - pPlayer.getX(), entity.getEyeY() - pPlayer.getEyeY(), entity.getZ() - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 0.99D - 0.025D / d0 ? pPlayer.hasLineOfSight(entity) : false;
    }

    public static float getPower(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f >= 3.0F) {
            f = 3.0F;
        }
        return f;
    }
}
