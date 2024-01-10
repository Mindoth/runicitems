package net.mindoth.runicitems.spell.ghostwalk;

import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GhostWalkSpell extends AbstractSpell {

    public static void shootMagic(LivingEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime) {
        World level = caster.level;
        playMagicShootSound(level, center);

        if ( caster == owner ) {
            owner.addEffect(new EffectInstance(RunicItemsEffects.GHOST_WALK.get(), spell.getLife(), (int)spell.getPower(), false, false));
        }
    }

    @Override
    public int getLife() {
        return 400;
    }

    @Override
    public float getPower() {
        return 0.0F;
    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public int getDistance() {
        return 0;
    }

    @Override
    public boolean getGravity() {
        return false;
    }

    @Override
    public int getCooldown() {
        return 400;
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
