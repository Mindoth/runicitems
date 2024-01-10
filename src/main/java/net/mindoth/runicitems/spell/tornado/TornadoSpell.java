package net.mindoth.runicitems.spell.tornado;

import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class TornadoSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime) {
        World level = caster.level;

        AbstractSpellEntity projectile = new TornadoEntity(level, owner, caster, spell, "storm", 0.0F);
        projectile.setNoGravity(!spell.getGravity());

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        Vector3d pos = ShadowEvents.getPoint(caster, spell.getDistance(), 0.0F, true);
        projectile.setPos(pos.x, pos.y, pos.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, spell.getSpeed(), 0.0F);
        projectile.setDeltaMovement(projectile.getDeltaMovement().x, 0.0F, projectile.getDeltaMovement().z);
        level.addFreshEntity(projectile);
    }

    @Override
    public int getLife() {
        return 120;
    }

    @Override
    public float getPower() {
        return 1.0F;
    }

    @Override
    public float getSpeed() {
        return 0.25F;
    }

    @Override
    public int getDistance() {
        return 4;
    }

    @Override
    public boolean getGravity() {
        return false;
    }

    @Override
    public int getCooldown() {
        return 160;
    }

    @Override
    public boolean isChannel() {
        return false;
    }

    @Override
    public int getPierce() {
        return 0;
    }

    @Override
    public int getBounce() {
        return 0;
    }
}
