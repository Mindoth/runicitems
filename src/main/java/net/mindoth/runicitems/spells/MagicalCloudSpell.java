package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.entity.spell.AbstractCloudEntity;
import net.mindoth.runicitems.entity.spell.MagicalCloudEntity;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class MagicalCloudSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, Vec3 center, float xRot, float yRot) {
        Level level = caster.level;

        AbstractCloudEntity cloud = getCloud(level, owner, caster, itemHandler, slot, effects);
        cloud.setNoGravity(SpellBuilder.getGravity(effects));
        float speed = getSpeed();
        if ( SpellBuilder.getSpeed(effects, speed) == 0.25F ) speed = Float.MIN_VALUE;
        playSound(level, center);

        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        cloud.setPos(new Vec3(center.x, center.y, center.z));
        cloud.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, SpellBuilder.getSpeed(effects, speed), 0);
        if ( SpellBuilder.getSpeed(effects, speed) == 0.25F ) cloud.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(cloud);
    }

    protected static AbstractCloudEntity getCloud(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects) {
        return new MagicalCloudEntity(level, owner, caster, itemHandler, slot, effects);
    }

    protected static float getSpeed() {
        return 0.25F;
    }

    protected static void playSound(Level level, Vec3 center) {
        playMagicSummonSound(level, center);
    }
}
