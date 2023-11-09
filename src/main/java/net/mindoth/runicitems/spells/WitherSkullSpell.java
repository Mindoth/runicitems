package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.entity.spell.AbstractProjectileEntity;
import net.mindoth.runicitems.entity.spell.WitherSkullEntity;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class WitherSkullSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Vec3 center, float xRot, float yRot) {
        Level level = caster.level;

        AbstractProjectileEntity projectile = getProjectile(level, owner, caster, itemHandler, slot, effects);
        projectile.setPos(center);
        projectile.setNoGravity(SpellBuilder.getGravity(effects));
        float speed = getSpeed();
        playSound(level, center);

        int adjuster = 1;
        if ( caster != owner ) adjuster = -1;
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, SpellBuilder.getSpeed(effects, speed), 1.0F);
        level.addFreshEntity(projectile);
    }

    protected static AbstractProjectileEntity getProjectile(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        return new WitherSkullEntity(level, owner, caster, itemHandler, slot, effects);
    }

    protected static float getSpeed() {
        return 1.0F;
    }

    protected static void playSound(Level level, Vec3 center) {
        playEvilSound(level, center);
    }
}
