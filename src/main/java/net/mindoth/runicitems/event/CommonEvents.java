package net.mindoth.runicitems.event;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class CommonEvents {

    //Enchantment Static
    @SubscribeEvent
    public static void hammerZapEvent(final AttackEntityEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            ServerLevel level = (ServerLevel)event.getEntity().level;
            if ( event.getEntity() != null ) {
                LivingEntity source;
                Entity target;
                List<Entity> bounced = new ArrayList<>();
                List<Entity> entitiesAround = new ArrayList<>();
                target = event.getTarget();
                source = event.getEntity();
                bounced.add(source);
                Item item = source.getMainHandItem().getItem();
                int power = source.getMainHandItem().getEnchantmentLevel(RunicItemsEnchantments.STATIC.get());
                if ( item == RunicItemsItems.HAMMER.get() && power > 0 ) {
                    //TODO for some reason it skips the 4th attack
                    for ( int j = 0; j < power; j++ ) {
                        entitiesAround = level.getEntities(target, target.getBoundingBox().inflate(3.0D + power * 2, 3.0D + power * 2, 3.0D + power * 2), Entity::isAttackable);

                        double lowestSoFar = Double.MAX_VALUE;
                        Entity listedEntity = null;

                        for ( Entity closestSoFar : entitiesAround ) {
                            double testDistance = target.distanceTo(closestSoFar);
                            if (testDistance < lowestSoFar && closestSoFar instanceof LivingEntity && !bounced.contains(closestSoFar)) {
                                lowestSoFar = testDistance;
                                listedEntity = closestSoFar;
                            }
                        }
                        if ( listedEntity != null ) {
                            bounced.add(listedEntity);
                            double playerX = target.getX();
                            double playerY = target.getEyeY();
                            double playerZ = target.getZ();
                            double listedEntityX = listedEntity.getX();
                            double listedEntityY = listedEntity.getEyeY();
                            double listedEntityZ = listedEntity.getZ();
                            int particleInterval = (int)Math.round(target.distanceTo(listedEntity)) * 5;
                            //Particles
                            for ( int i = 1; i < (1 + particleInterval); i++ ) {
                                double x = playerX * (1 - ((double) i / particleInterval)) + listedEntityX * ((double) i / particleInterval);
                                double y = playerY * (1 - ((double) i / particleInterval)) + listedEntityY * ((double) i / particleInterval);
                                double z = playerZ * (1 - ((double) i / particleInterval)) + listedEntityZ * ((double) i / particleInterval);
                                level.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0, 0, 0);
                            }
                            //Apply damage
                            listedEntity.hurt(DamageSource.mobAttack(source).setMagic().bypassMagic().bypassArmor(), 1 + power);
                            target = listedEntity;
                            entitiesAround.clear();
                        }
                    }
                }
            }
        }
    }
}
