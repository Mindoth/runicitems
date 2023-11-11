package net.mindoth.runicitems.spells;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public abstract class AbstractSpell {

    public static void routeSpell(Player owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, AbstractSpell spell, Vec3 center, float xRot, float yRot) {
        if ( spell instanceof MagicSparkSpell ) MagicSparkSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof MeteorSpell ) MeteorSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof HealingBoltSpell ) HealingBoltSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof WitherSkullSpell ) WitherSkullSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof StormyCloudSpell ) StormyCloudSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof MagicalCloudSpell ) MagicalCloudSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof TornadoSpell ) TornadoSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof UnstableCloudSpell ) UnstableCloudSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof ExplosionSpell ) ExplosionSpell.shootMagic(owner, effects, center);
        if ( spell instanceof GhostWalkSpell ) GhostWalkSpell.shootMagic(owner, caster, effects);
        if ( spell instanceof AlacritySpell ) AlacritySpell.shootMagic(owner, caster, effects);
        if ( spell instanceof ForgeSpiritSpell ) ForgeSpiritSpell.shootMagic(owner, caster, effects, center);
        if ( spell instanceof RaiseDeadSpell ) RaiseDeadSpell.shootMagic(owner, caster, effects, center);
        if ( spell instanceof IcicleSpell ) IcicleSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof DeafeningBlastSpell ) DeafeningBlastSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        //if ( spell instanceof SunStrikeSpell ) SunStrikeSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
    }

    protected static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }

    protected static void playFireSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5f, 1);
    }

    protected static void playEvilSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, 1);
    }

    protected static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25f, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
    }
}
