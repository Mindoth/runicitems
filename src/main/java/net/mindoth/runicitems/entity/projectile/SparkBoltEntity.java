package net.mindoth.runicitems.entity.projectile;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class SparkBoltEntity extends ProjectileBaseEntity {

    public SparkBoltEntity(EntityType<SparkBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SparkBoltEntity(Level level, Player player, float power, boolean hasFire, boolean hasExplosion) {
        super(RunicItemsEntities.SPARK_BOLT.get(), level, player, power, hasFire, hasExplosion);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected float getGravity() {
        return 0.03f;
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.END_ROD;
    }
}
