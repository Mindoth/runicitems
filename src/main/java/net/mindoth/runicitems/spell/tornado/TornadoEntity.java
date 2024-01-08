package net.mindoth.runicitems.spell.tornado;

import net.mindoth.runicitems.event.MiscEvents;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.List;

public class TornadoEntity extends AbstractSpellEntity {

    public TornadoEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.TORNADO.get(), level);
    }

    public TornadoEntity(EntityType<TornadoEntity> entityType, World level) {
        super(entityType, level);
    }

    public TornadoEntity(World level, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, spell, element, scale);
    }

    final int range = 3;

    @Override
    protected void doTickEffects() {
        Vector3d center = CommonEvents.getEntityCenter(this);
        int tickCount = this.tickCount;
        if ( tickCount % 5 == 0 ) {
            this.level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2, 0.02F);
            this.level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2, 0.03F);
        }
        List<LivingEntity> targets = MiscEvents.getEnemiesAround(this, this.level, this.range);
        for ( LivingEntity target : targets ) {
            double velY = target.getDeltaMovement().y;
            double dx = (center.x - target.position().x > 0 ? 0.5 : -0.5) - (center.x - target.position().x) * 0.125;
            double dz = (center.z - target.position().z > 0 ? 0.5 : -0.5) - (center.z - target.position().z) * 0.125;
            dealDamage(target);

            double lift = 0.01D;
            if ( !(target instanceof ServerPlayerEntity ) && velY < 0.5D ) target.push(dx, velY + lift, dz);
        }
    }

    @Override
    protected void spawnParticles() {
        Vector3d center = CommonEvents.getEntityCenter(this);
        double posX = center.x;
        double posY = center.y - 2;
        double posZ = center.z;
        int lines = 6;
        int angle = this.tickCount * lines;
        int maxHeight = 4 + this.range;
        double maxRadius = this.range;
        double heightIncreasement = 0.5D;
        double radiusIncreasement = maxRadius / maxHeight;
        for ( int l = 0; l < lines; l++ ) {
            for ( double y = 0; y < maxHeight; y += heightIncreasement ) {
                double radius = y * radiusIncreasement + heightIncreasement;
                double x = (Math.cos(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                double z = (Math.sin(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                ServerWorld level = (ServerWorld)this.level;
                level.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, blockUnder(this)), posX + x, posY + y, posZ + z,
                        0, 0, 1, 0, 1.0D);
            }
        }
    }

    private BlockState blockUnder(Entity tornado) {
        BlockPos tornadoPos = tornado.blockPosition();
        World level = tornado.level;
        BlockState returnState = Blocks.WHITE_WOOL.defaultBlockState();
        for ( int i = tornadoPos.getY(); i > 0; i-- ) {
            BlockPos particleBlockPost = new BlockPos(tornadoPos.getX(), i, tornadoPos.getZ());
            if ( level.getBlockState(particleBlockPost).getMaterial().isSolid() || level.getBlockState(particleBlockPost).getMaterial().isLiquid() ) {
                returnState = level.getBlockState(particleBlockPost).getBlock().defaultBlockState();
                break;
            }
        }
        return returnState;
    }
}
