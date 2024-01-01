package net.mindoth.runicitems.spell.blizzard;

import net.mindoth.runicitems.spell.abstractspell.AbstractProjectileEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;

public class BlizzardSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot) {
        World level = caster.level;

        AbstractProjectileEntity projectile = new AbstractProjectileEntity(level, owner, caster, itemHandler, spell, "frost", 0.3F);
        projectile.setNoGravity(!spell.getGravity());

        setPos(level, caster, projectile);
        projectile.setDeltaMovement(0F, -0.6F, 0F);
        level.addFreshEntity(projectile);
    }

    protected static void setPos(World level, Entity caster, AbstractProjectileEntity projectile) {
        float radius = 2.0F;
        float range = 8.0F;
        float error = 0.5F;
        float randX = (float)((Math.random() * (radius - (-radius))) + (-radius));
        float randY = (float)((Math.random() * (radius - (-radius))) + (-radius));
        float randZ = (float)((Math.random() * (radius - (-radius))) + (-radius));
        double posX;
        double posY;
        double posZ;
        if ( CommonEvents.getPointedEntity(level, caster, range, error, true) != caster ) {
            posX = CommonEvents.getPointedEntity(level, caster, range, error, true).getX() + randX;
            posY = CommonEvents.blockHeight(level, caster, range, error, 7) + randY;
            posZ = CommonEvents.getPointedEntity(level, caster, range, error, true).getZ() + randZ;
        }
        else {
            posX = CommonEvents.getPoint(caster, range, error, true).x + randX;
            posY = CommonEvents.blockHeight(level, caster, range, error, 7) + randY;
            posZ = CommonEvents.getPoint(caster, range, error, true).z + randZ;
        }
        Vector3d spawnPos = new Vector3d(posX, posY, posZ);
        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        if ( caster.tickCount % 5 == 0 ) playSound(level, new Vector3d(spawnPos.x, spawnPos.y, spawnPos.z));
    }

    @Override
    public int getLife() {
        return 120;
    }

    @Override
    public float getPower() {
        return 8.0F;
    }

    @Override
    public float getSpeed() {
        return 0.0F;
    }

    @Override
    public int getDistance() {
        return 0;
    }

    @Override
    public boolean getGravity() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    protected static void playSound(World level, Vector3d center) {
        playWindSound(level, center);
    }
}
