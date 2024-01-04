package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.SpellProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class FireballSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot) {
        World level = caster.level;

        AbstractSpellEntity projectile = new SpellProjectileEntity(level, owner, caster, spell, "fire", 1.2F);
        projectile.setNoGravity(!spell.getGravity());
        playSound(level, center);

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        projectile.setPos(center.x, center.y - 0.5F, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, spell.getSpeed(), 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public int getLife() {
        return 120;
    }

    @Override
    public float getPower() {
        return 12.0F;
    }

    @Override
    public float getSpeed() {
        return 0.8F;
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
        return 100;
    }

    @Override
    public boolean isChannel() {
        return false;
    }

    protected static void playSound(World level, Vector3d center) {
        playFireSound(level, center);
    }
}
