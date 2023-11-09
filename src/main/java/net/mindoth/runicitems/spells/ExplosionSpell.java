package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.Position;
import java.util.HashMap;

public class ExplosionSpell extends AbstractSpell {

    public static void shootMagic(Entity caster, HashMap<Item, Integer> effects, Vec3 center) {
        Level level = caster.getLevel();

        Explosion.BlockInteraction destroyBlocks;
        if ( SpellBuilder.getBlockPiercing(effects) ) destroyBlocks = Explosion.BlockInteraction.NONE;
        else destroyBlocks = Explosion.BlockInteraction.DESTROY;
        int amplifier = Math.round(SpellBuilder.getPower(effects, 0));
        level.explode(null, DamageSource.MAGIC, null, center.x, center.y, center.z, amplifier, false, destroyBlocks);
    }
}
