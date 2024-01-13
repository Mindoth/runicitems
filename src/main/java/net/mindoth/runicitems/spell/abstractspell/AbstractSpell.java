package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.mindoth.runicitems.spell.ghostwalk.GhostWalkSpell;
import net.mindoth.runicitems.spell.raisedead.RaiseDeadSpell;
import net.mindoth.runicitems.spell.tornado.TornadoSpell;
import net.mindoth.runicitems.spell.waterbolt.WaterBoltSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class AbstractSpell {

    public static void routeSpell(PlayerEntity owner, Entity caster, AbstractSpell spell, Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        if ( spell instanceof BlizzardSpell ) BlizzardSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
        if ( spell instanceof TornadoSpell ) TornadoSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
        if ( spell instanceof FireballSpell ) FireballSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
        if ( spell instanceof GhostWalkSpell ) GhostWalkSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
        if ( spell instanceof RaiseDeadSpell ) RaiseDeadSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
        if ( spell instanceof WaterBoltSpell ) WaterBoltSpell.shootMagic(owner, caster, spell, center, xRot, yRot, useTime, rune);
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
        return 1;
    }

    public boolean getGravity() {
        return false;
    }

    public int getCooldown() {
        return 20;
    }

    public boolean isChannel() {
        return false;
    }

    public int getPierce() {
        return 0;
    }

    public int getBounce() {
        return 0;
    }

    public static boolean isPlayer(PlayerEntity owner, Entity caster) {
        return owner == caster;
    }

    public void chargeUpEffects(World level, Entity caster, int useTime) {
    }

    protected static void playMagicShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5F, 2);
    }

    protected static void playWaterShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.DOLPHIN_SPLASH, SoundCategory.PLAYERS, 0.25F, 0.85F);
    }

    protected static void playStormShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.35F, 2.0F);
    }

    protected static void playFireShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    protected static void playEvilShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    protected static void playWindSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2.0F, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2.0F, 0.03F);
    }

    protected static void playMagicSummonSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.25F, 2.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5F, 2.0F);
    }
}
