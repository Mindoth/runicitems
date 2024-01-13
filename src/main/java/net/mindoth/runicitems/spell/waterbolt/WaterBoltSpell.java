package net.mindoth.runicitems.spell.waterbolt;

import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WaterBoltSpell extends AbstractSpell {

    public static void shootMagic(PlayerEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;

        AbstractSpellEntity projectile = new WaterBoltEntity(level, owner, caster, spell, "water", 0.4F);
        projectile.setNoGravity(!spell.getGravity());
        playWaterShootSound(level, center);

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        projectile.setPos(center.x, center.y - 0.25F, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, spell.getSpeed(), 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public int getLife() {
        return 120;
    }

    @Override
    public float getPower() {
        return 4.0F;
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
        return false;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public boolean isChannel() {
        return false;
    }

    @Override
    public int getPierce() {
        return 8;
    }

    @Override
    public int getBounce() {
        return 3;
    }
}
