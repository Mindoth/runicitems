package net.mindoth.runicitems.spell.blizzard;

import net.mindoth.runicitems.client.particle.ParticleColor;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.spell.abstractspell.AbstractProjectileEntity;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class BlizzardSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects,
                                  Vector3d center, float xRot, float yRot, ParticleColor.IntWrapper color) {
        World level = caster.level;

        AbstractProjectileEntity projectile = new AbstractProjectileEntity(level, owner, caster, itemHandler, slot, effects, color);
        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        float speed = getSpeed();

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        setPos(level, caster, projectile);
        projectile.shootFromRotation(caster, 0F * adjuster, 0F * adjuster, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
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
        projectile.setPos(posX, posY, posZ);
    }

    public boolean isChannel() {
        return true;
    }

    protected static float getSpeed() {
        return 0.0F;
    }
}
