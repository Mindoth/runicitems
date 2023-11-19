package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.List;

public class TornadoEntity extends AbstractCloudEntity {

    public TornadoEntity(EntityType<TornadoEntity> entityType, Level level) {
        super(entityType, level);
    }

    public TornadoEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                         HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, itemHandler, slot, effects);
    }

    @Override
    protected void doTickEffects() {
        Vec3 center = CommonEvents.getEntityCenter(this);
        int tickCount = this.tickCount;
        if ( tickCount % 5 == 0 ) {
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2, 0.02F);
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2, 0.03F);
        }
        List<LivingEntity> targets = CommonEvents.getEntitiesAround(this, this.level, this.range);
        for ( LivingEntity target : targets ) {
            double velY = target.getDeltaMovement().y;
            double dx = (center.x - target.position().x > 0 ? 0.5 : -0.5) - (center.x - target.position().x) * 0.125;
            double dz = (center.z - target.position().z > 0 ? 0.5 : -0.5) - (center.z - target.position().z) * 0.125;
            float damage = this.power;
            if ( this.owner != null ) target.hurt(DamageSource.indirectMagic(this, this.owner), damage);

            double lift = 0.01D;
            if ( !(target instanceof ServerPlayer) && velY < 0.5D ) target.push(dx, velY + lift, dz);
        }
    }

    @Override
    protected void spawnParticles() {
        Vec3 center = CommonEvents.getEntityCenter(this);
        double posX = center.x;
        double posY = center.y - 1;
        double posZ = center.z;
        int lines = 6;
        int angle = this.tickCount * lines;
        int maxHeight = 4 + this.range;
        double maxRadius = this.range - 1;
        double heightIncreasement = 0.5D;
        double radiusIncreasement = maxRadius / maxHeight;
        for ( int l = 0; l < lines; l++ ) {
            for ( double y = 0; y < maxHeight; y += heightIncreasement ) {
                double radius = y * radiusIncreasement + heightIncreasement;
                double x = (Math.cos(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                double z = (Math.sin(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                ServerLevel level = (ServerLevel)this.level;
                level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockUnder(this)), posX + x, posY + y, posZ + z,
                        0, 0, 1, 0, 1.0D);
            }
        }
    }

    private BlockState blockUnder(Entity tornado) {
        BlockPos tornadoPos = tornado.getOnPos();
        Level level = tornado.level;
        BlockState returnState = Blocks.WHITE_WOOL.defaultBlockState();
        for ( int i = tornadoPos.getY(); i > level.getMinBuildHeight(); i-- ) {
            BlockPos particleBlockPost = new BlockPos(tornadoPos.getX(), i, tornadoPos.getZ());
            if ( level.getBlockState(particleBlockPost).getMaterial().isSolid() || level.getBlockState(particleBlockPost).getMaterial().isLiquid() ) {
                returnState = level.getBlockState(particleBlockPost).getBlock().defaultBlockState();
                break;
            }
        }
        return returnState;
    }
}
