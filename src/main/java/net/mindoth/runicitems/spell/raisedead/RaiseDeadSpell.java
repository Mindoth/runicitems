package net.mindoth.runicitems.spell.raisedead;

import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RaiseDeadSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;
        playMagicSummonSound(level, center);

        MobEntity minion = new SkeletonMinionEntity(level, owner);

        minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(spell.getPower() * 1);
        minion.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(spell.getPower() * 5);
        minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(spell.getSpeed());
        minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(spell.getDistance() * 2);
        minion.addEffect(new EffectInstance(RunicItemsEffects.SKELETON_TIMER.get(), spell.getLife(), 0, false, false, true));
        minion.moveTo(ShadowEvents.getPointUntilSolid(caster, spell.getDistance(), 0.0F, true));

        minion.finalizeSpawn((ServerWorld)level, level.getCurrentDifficultyAt(minion.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }

    @Override
    public int getLife() {
        return 600;
    }

    @Override
    public float getPower() {
        return 4.0F;
    }

    @Override
    public float getSpeed() {
        return 0.25F;
    }

    @Override
    public int getDistance() {
        return 6;
    }

    @Override
    public boolean getGravity() {
        return false;
    }

    @Override
    public int getCooldown() {
        return 600;
    }

    @Override
    public boolean isChannel() {
        return false;
    }

    @Override
    public int getPierce() {
        return 0;
    }

    @Override
    public int getBounce() {
        return 0;
    }
}
