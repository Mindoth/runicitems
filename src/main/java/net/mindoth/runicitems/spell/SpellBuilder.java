package net.mindoth.runicitems.spell;

import net.mindoth.runicitems.inventory.WandManager;
import net.mindoth.runicitems.item.WandItem;
import net.mindoth.runicitems.item.rune.EffectRuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.rune.UtilityRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

import static java.lang.Math.max;
import static net.mindoth.runicitems.item.WandItem.getCooldown;
import static net.mindoth.runicitems.item.WandItem.getdelay;
import static net.mindoth.runicitems.spell.ShootProjectile.shootSparkBolt;
import static net.mindoth.runicitems.spell.SpawnEffect.causeExplosion;

public class SpellBuilder {

    public static void cast(Level level, Player owner, ItemStack wand) {
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
                        doSpell(tag, owner, owner, wand, itemHandler);
                        break;
                    }
                }
                advance(tag, wand, owner, itemHandler);
            }
            advance(tag, wand, owner, itemHandler);
        }
    }

    //TODO After trigger has triggered on spell skip the next spell that was used in the trigger
    private static void doSpell(CompoundTag tag, Player owner, Entity caster, ItemStack wand, IItemHandler itemHandler) {
        Item rune = getRune(itemHandler, wand);
        HashMap<String, Integer> effects = getEffects(getSlot(wand), itemHandler);

        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            causeExplosion(owner, caster, effects);
        }
        if ( rune == RunicItemsItems.SPARK_BOLT_RUNE.get() ) {
            shootSparkBolt(owner, caster, effects);
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

    private static HashMap<String, Integer> getEffects(int i, IItemHandler itemHandler) {
        HashMap<String, Integer> effects = new HashMap<>();
        effects.put("power", 0);
        effects.put("trigger", 0);
        effects.put("explosion", 0);
        effects.put("sparkbolt", 0);
        for ( int j = max(getLastSpell(i, itemHandler) + 1, 0); j < i; j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof EffectRuneItem || itemHandler.getStackInSlot(j).getItem() instanceof UtilityRuneItem ) {
                Item rune = itemHandler.getStackInSlot(j).getItem();
                if ( rune == RunicItemsItems.AMPLIFICATION_RUNE.get() ) {
                    effects.put("power", effects.get("power") + 1);
                }
                if ( rune == RunicItemsItems.NULLIFICATION_RUNE.get() ) {
                    effects.put("power", effects.get("power") - 1);
                }
                if ( rune == RunicItemsItems.TRIGGER_RUNE.get() ) {
                    effects.put("trigger", 1);
                    Item nextRune = getNextSpell(i, itemHandler);
                    if ( nextRune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
                        effects.put("explosion", 1);
                    }
                    if ( nextRune == RunicItemsItems.SPARK_BOLT_RUNE.get() ) {
                        effects.put("sparkbolt", 1);
                    }
                }
            }
        }
        return effects;
    }

    private static Item getNextSpell(int i, IItemHandler itemHandler) {
        Item nextRune = null;
        for ( int j = i + 1; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = itemHandler.getStackInSlot(j).getItem();
            }
        }
        return nextRune;
    }

    private static int getNextSpellSlot(int i, IItemHandler itemHandler) {
        int nextRune = -1;
        for ( int j = i + 1; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = j;
            }
        }
        return nextRune;
    }

    private static int getLastSpell(int i, IItemHandler itemHandler) {
        int nextRune = -1;
        for ( int j = i - 1; j >= 0; j-- ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = j;
            }
        }
        return nextRune;
    }

    private static void advance(CompoundTag tag, ItemStack wand, Player player, IItemHandler itemHandler) {
        if ( getNextSpell(getSlot(wand), itemHandler) == null ) {
            tag.putInt("SLOT", 0);
            player.getCooldowns().addCooldown(wand.getItem(), getCooldown(wand.getItem()));
        }
        else tag.putInt("SLOT", getSlot(wand) + 1);
    }
}
