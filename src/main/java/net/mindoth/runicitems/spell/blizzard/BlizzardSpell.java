package net.mindoth.runicitems.spell.blizzard;

import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BlizzardSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime) {
        if ( useTime % 5 != 0 ) return;
        World level = caster.level;

        AbstractSpellEntity projectile = new BlizzardEntity(level, owner, caster, spell, "frost", 0.5F);
        projectile.setNoGravity(!spell.getGravity());

        setPos(level, caster, projectile, useTime);
        projectile.setDeltaMovement(0.0F, -spell.getSpeed(), 0.0F);
        level.addFreshEntity(projectile);
    }

    protected static void setPos(World level, Entity caster, AbstractSpellEntity projectile, int time) {
        float radius;
        if ( time % 20 == 0 ) radius = 0.0F;
        else radius = 2.0F;
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
            posX = CommonEvents.getPoint(caster, range, 0.0F, true).x + randX;
            posY = CommonEvents.blockHeight(level, caster, range, 0.0F, 7) + randY;
            posZ = CommonEvents.getPoint(caster, range, 0.0F, true).z + randZ;
        }
        Vector3d spawnPos = new Vector3d(posX, posY, posZ);
        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        if ( time % 5 == 0 ) playSound(level, new Vector3d(spawnPos.x, spawnPos.y, spawnPos.z));
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
        return 0.4F;
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
        return 10;
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    protected static void playSound(World level, Vector3d center) {
        playWindSound(level, center);
    }
}
