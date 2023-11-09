package net.mindoth.runicitems.spells;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class GhostWalkSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, HashMap<Item, Integer> effects) {
        Level level = caster.getLevel();
        Vec3 center = CommonEvents.getEntityCenter(caster);
        int duration = 1200;
        int life = SpellBuilder.getLife(effects, duration);
        int amplifier = Math.round(SpellBuilder.getPower(effects, 0)) - 1;
        playMagicSound(level, center);
        owner.addEffect(new MobEffectInstance(RunicItemsEffects.GHOST_WALK.get(), life, amplifier, false, false, true));
    }
}
