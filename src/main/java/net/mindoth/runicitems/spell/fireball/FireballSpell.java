package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.particle.ParticleColor;
import net.mindoth.runicitems.spell.abstractspell.AbstractProjectileEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class FireballSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Vector3d center, float xRot, float yRot) {
        World level = caster.level;

        AbstractProjectileEntity projectile = new AbstractProjectileEntity(level, owner, caster, itemHandler, slot, effects, new ParticleColor(177F, 63F, 0F,1.6F));
        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        float speed = getSpeed();
        playSound(level, center);

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        projectile.setPos(center.x, center.y - 0.5F, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
        level.addFreshEntity(projectile);
    }

    protected static float getSpeed() {
        return 0.6F;
    }

    protected static void playSound(World level, Vector3d center) {
        playFireSound(level, center);
    }
}
