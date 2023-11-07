package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class TornadoEntity extends CloudBaseEntity {

    public TornadoEntity(EntityType<TornadoEntity> entityType, Level level) {
        super(entityType, level);
    }

    public TornadoEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                         HashMap<Item, Integer> effects, Item rune) {
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, itemHandler, slot, effects, rune);
    }

    //TODO refine tornado particles and add lifting entities
    @Override
    protected void spawnParticles() {
        Vec3 center = CommonEvents.getEntityCenter(this);
        double posX = center.x;
        double posY = center.y - 2;
        double posZ = center.z;
        int tickCount = this.tickCount;
        int angle = tickCount * 16;
        int maxHeight = 8;
        double maxRadius = 3.0D;
        int lines = 3;
        double heightIncreasement = 0.5D;
        double radiusIncreasement = maxRadius / maxHeight;
        for ( int l = 0; l < lines; l++ ) {
            for ( double y = 0; y < maxHeight; y += heightIncreasement ) {
                double radius = y * radiusIncreasement + 0.5D;
                double x = Math.cos(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius;
                double z = Math.sin(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius;
                ServerLevel level = (ServerLevel)this.level;
                level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockUnder(this)), posX + x, posY + y, posZ + z,
                        0, x, 64, z, 16.0D);
            }
        }
        if ( tickCount % 5 != 0 ) return;
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2, 0.03F);
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
