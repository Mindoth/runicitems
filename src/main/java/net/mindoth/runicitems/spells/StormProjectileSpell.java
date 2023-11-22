package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.entity.spell.AbstractProjectileEntity;
import net.mindoth.runicitems.entity.spell.MagicSparkEntity;
import net.mindoth.runicitems.entity.spell.StormProjectileEntity;
import net.mindoth.runicitems.event.SpellBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class StormProjectileSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Vec3 center, float xRot, float yRot) {
        Level level = caster.level;

        AbstractProjectileEntity projectile = getProjectile(level, owner, caster, itemHandler, slot, effects);
        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        float speed = getSpeed();
        playSound(level, center);

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        projectile.setPos(new Vec3(center.x, center.y, center.z));
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
        level.addFreshEntity(projectile);
    }

    protected static AbstractProjectileEntity getProjectile(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        return new StormProjectileEntity(level, owner, caster, itemHandler, slot, effects);
    }

    protected static float getSpeed() {
        return 1.0F;
    }

    protected static void playSound(Level level, Vec3 center) {
        playStormSound(level, center);
    }
}
