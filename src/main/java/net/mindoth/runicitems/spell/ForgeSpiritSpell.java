package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.entity.minion.BlazeMinionEntity;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class ForgeSpiritSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, HashMap<Item, Integer> effects, Vec3 center) {
        Level level = caster.getLevel();
        if ( level.isClientSide ) return;
        int duration = 600;
        int life = SpellBuilder.getLife(effects, duration);
        Mob minion = getMinion(level, owner);

        minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(SpellBuilder.getPower(effects, 6));
        minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(SpellBuilder.getPower(effects, 20));
        minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(SpellBuilder.getSpeed(effects, 0.23F));
        minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(SpellBuilder.getRange(effects) * 12.0D);
        minion.addEffect(new MobEffectInstance(RunicItemsEffects.BLAZE_TIMER.get(), life, 0, false, false, true));
        minion.moveTo(center);

        playFireSound(level, center);
        minion.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(minion.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }

    protected static Mob getMinion(Level level, LivingEntity owner) {
        return new BlazeMinionEntity(level, owner);
    }

    protected static void playSound(Level level, Vec3 center) {
        playFireSound(level, center);
    }
}
