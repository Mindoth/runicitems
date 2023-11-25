package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.spell.icicle.IcicleSpell;
import net.mindoth.runicitems.spell.tornado.TornadoSpell;
import net.mindoth.runicitems.spell.witherskull.WitherSkullSpell;
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
        if ( spell instanceof IcicleSpell ) IcicleSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof WitherSkullSpell ) WitherSkullSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof TornadoSpell ) TornadoSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
    }

    protected static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 2);
    }

    protected static void playStormSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.35F, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WARDEN_STEP, SoundSource.PLAYERS, 2, 1);
    }

    protected static void playFireSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.4F, 1);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 1);
    }

    protected static void playEvilSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5F, 1);
    }

    protected static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25F, 2);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 2);
    }

    protected static void playFireSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 1);
    }
}
