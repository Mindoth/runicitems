package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.inventory.WandManager;
import net.mindoth.runicitems.item.WandItem;
import net.mindoth.runicitems.item.rune.EffectRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

import static java.lang.Math.max;
import static net.mindoth.runicitems.item.WandItem.getdelay;
import static net.mindoth.runicitems.spell.ShootProjectile.shootSparkBolt;
import static net.mindoth.runicitems.spell.SpawnEffect.causeExplosion;

public class SpellBuilder {
    public static void cast(Player owner, ItemStack wand) {
        HashMap<Item, Integer> effectsNew = new HashMap<>();
        resetEffects(effectsNew);
        if ( WandManager.get().getCapability(wand).isPresent() ) {
            IItemHandler itemHandler = WandManager.get().getCapability(wand).resolve().get();
            CompoundTag tag = wand.getOrCreateTag();
            if ( !tag.contains("SLOT") ) {
                tag.putInt("SLOT", 0);
            }
            for ( int i = getSlot(wand); i < itemHandler.getSlots(); i++ ) {
                Item rune = getRune(itemHandler, wand);
                if ( !itemHandler.getStackInSlot(getSlot(wand)).isEmpty() ) {
                    if ( rune instanceof SpellRuneItem ) {
                        doSpell(owner, owner, wand, itemHandler);
                        break;
                    }
                    if ( rune instanceof EffectRuneItem ) {
                        effects.merge(rune, 1, Integer::sum);
                    }
                }
                advance(tag, wand, owner, itemHandler);
            }
            advance(tag, wand, owner, itemHandler);
        }
    }

    private static void doSpell(Player owner, Entity caster, ItemStack wand, IItemHandler itemHandler) {
        Item rune = getRune(itemHandler, wand);

        if ( rune == RunicItemsItems.SPARK_BOLT_RUNE.get() ) {
            shootSparkBolt(owner, caster, effects);
        }
        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            causeExplosion(owner, caster, effects);
        }

        if ( caster instanceof Player player ) {
            player.getCooldowns().addCooldown(wand.getItem(), getdelay(wand.getItem()));
        }
    }

    private static Item getRune(IItemHandler itemHandler, ItemStack wand) {
        return itemHandler.getStackInSlot(getSlot(wand)).getItem();
    }

    private static int getSlot(ItemStack wand) {
        CompoundTag tag = wand.getTag();
        return tag.getInt("SLOT");
    }

    private static void resetEffects(HashMap<Item, Integer> effectsNew) {
        effects = effectsNew;
    }

    private static HashMap<Item, Integer> effects;

    private static Item getNextSpell(int i, IItemHandler itemHandler) {
        Item nextRune = null;
        for ( int j = i + 1; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = itemHandler.getStackInSlot(j).getItem();
            }
        }
        return nextRune;
    }

    private static void advance(CompoundTag tag, ItemStack wand, Player player, IItemHandler itemHandler) {
        if ( getNextSpell(getSlot(wand), itemHandler) == null ) {
            WandItem.reload(wand, player);
        }
        else tag.putInt("SLOT", getSlot(wand) + 1);
    }
}
