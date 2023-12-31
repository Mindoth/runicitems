package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public abstract class AbstractSpell {

    public static void routeSpell(PlayerEntity owner, Entity caster, IItemHandler itemHandler, int slot, HashMap<Item, Integer> effects, AbstractSpell spell, Vector3d center, float xRot, float yRot) {
        if ( spell instanceof FireballSpell ) FireballSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
        if ( spell instanceof BlizzardSpell ) {
            for ( int i = 0; i < 3; i++ ) {
                BlizzardSpell.shootMagic(owner, caster, itemHandler, slot, effects, center, xRot, yRot);
            }
        }
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
