package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.mindoth.runicitems.spell.tornado.TornadoSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractSpell {

    public static void routeSpell(PlayerEntity owner, Entity caster, AbstractSpell spell, Vector3d center, float xRot, float yRot, int useTime) {
        if ( spell instanceof BlizzardSpell ) BlizzardSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime);
        if ( spell instanceof TornadoSpell ) TornadoSpell.shootMagic(owner, caster, spell, center, xRot, yRot);
        if ( spell instanceof FireballSpell ) FireballSpell.shootMagic(owner, caster, spell, center, xRot, yRot);
    }

    public int getLife() {
        return 120;
    }

    public float getPower() {
        return 1.0F;
    }

    public float getSpeed() {
        return 0.0F;
    }

    public int getDistance() {
        return 0;
    }

    public boolean getGravity() {
        return true;
    }

    public int getCooldown() {
        return 20;
    }

    public boolean isChannel() {
        return false;
    }

    protected static void playMagicSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5F, 2);
    }

    protected static void playWindSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2, 0.03F);
    }

    protected static void playStormSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.35F, 2);
    }

    protected static void playFireSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.4F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, 1);
    }

    protected static void playEvilSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundCategory.PLAYERS, 0.5F, 1);
    }

    protected static void playMagicSummonSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.25F, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5F, 2);
    }

    protected static void playFireSummonSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, 1);
    }
}
