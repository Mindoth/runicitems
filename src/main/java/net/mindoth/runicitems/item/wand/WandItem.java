package net.mindoth.runicitems.item.wand;

import net.mindoth.runicitems.client.gui.WandContainer;
import net.mindoth.runicitems.entity.projectile.SparkBoltEntity;
import net.mindoth.runicitems.inventory.WandData;
import net.mindoth.runicitems.inventory.WandManager;
import net.mindoth.runicitems.item.ModWand;
import net.mindoth.runicitems.item.WandType;
import net.mindoth.runicitems.item.rune.EffectRuneItem;
import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.rune.UtilityRuneItem;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.UUID;

public class WandItem extends ModWand {
    public WandItem(WandType tier) {
        super(tier);
    }

    private static Item getRune(IItemHandler itemHandler, ItemStack wand) {
        CompoundTag tag = wand.getTag();
        return itemHandler.getStackInSlot(tag.getInt("SLOT")).getItem();
    }

    private static int getSlot(ItemStack wand) {
        CompoundTag tag = wand.getTag();
        return tag.getInt("SLOT");
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

    private static int skipNextSpell(int i, IItemHandler itemHandler) {
        int nextRune = -1;
        for ( int j = i + 1; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = j;
            }
        }
        return nextRune;
    }

    private static float getPower() {
        return 1.0f + amplifier;
    }

    private static boolean hasFire() {
        return effect.contains("fire");
    }

    private static boolean hasExplosion() {
        return effect.contains("explosion");
    }

    private static boolean hasTrigger() {
        return effect.contains("trigger");
    }

    private static void resetModifiers() {
        amplifier = 0;
        effect = "";
        sb.delete(0, sb.length());
    }

    private static int amplifier = 0;
    private static String effect = "";
    private static StringBuilder sb = new StringBuilder();

    public static void cast(Level level, Player player, ItemStack wand) {
        if ( WandManager.get().getCapability(wand).isPresent() ) {
            IItemHandler itemHandler = WandManager.get().getCapability(wand).resolve().get();
            CompoundTag tag = wand.getOrCreateTag();
            if ( !tag.contains("SLOT") ) {
                tag.putInt("SLOT", 0);
            }
            for ( int i = getSlot(wand); i < itemHandler.getSlots(); i++ ) {
                Item rune = getRune(itemHandler, wand);
                if ( !itemHandler.getStackInSlot(getSlot(wand)).isEmpty() ) {
                    if ( rune instanceof EffectRuneItem ) {
                        doEffect(rune);
                    }
                    if ( rune instanceof UtilityRuneItem ) {
                        doUtility(rune);
                    }
                    if ( rune instanceof SpellRuneItem ) {
                        doSpell(tag, player, level, wand, itemHandler);
                        player.getCooldowns().addCooldown(wand.getItem(), 20);
                        break;
                    }
                }
                advance(tag, wand, player, itemHandler);
            }
            advance(tag, wand, player, itemHandler);
        }
    }

    private static void advance(CompoundTag tag, ItemStack wand, Player player, IItemHandler itemHandler) {
        if ( getNextSpell(getSlot(wand), itemHandler) == null ) {
            tag.putInt("SLOT", 0);
            player.getCooldowns().addCooldown(wand.getItem(), 60);
            resetModifiers();
        }
        else tag.putInt("SLOT", getSlot(wand) + 1);
    }

    private static void doSpell(CompoundTag tag, Player player, Level level, ItemStack wand, IItemHandler itemHandler) {
        Vec3 look = player.getLookAngle();
        Item rune = getRune(itemHandler, wand);
        if ( hasTrigger() ) {
            if ( getNextSpell(getSlot(wand), itemHandler) == RunicItemsItems.EXPLOSION_RUNE.get() ) {
                effect = sb.append("explosion;").toString();
            }
            tag.putInt("SLOT", skipNextSpell(getSlot(wand), itemHandler));
        }

        if ( rune == RunicItemsItems.FIRE_RUNE.get() ) {
            player.setSecondsOnFire((int)getPower());
        }
        if ( rune == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            causeExplosion(level, player, CommonEvents.getEntityCenter(player));
        }
        if ( rune == RunicItemsItems.SPARK_BOLT_RUNE.get() ) {
            shootSparkBolt(player, look, getPower(), hasFire(), hasExplosion());
        }


        resetModifiers();
    }

    private static void doEffect(Item rune) {
        if ( rune == RunicItemsItems.AMPLIFICATION_RUNE.get() ) {
            amplifier += 1;
        }
        if ( rune == RunicItemsItems.NULLIFICATION_RUNE.get() ) {
            amplifier -= 1;
        }
        if ( rune == RunicItemsItems.FIRE_RUNE.get() ) {
            effect = sb.append("fire;").toString();
        }
    }

    private static void doUtility(Item rune) {
        if ( rune == RunicItemsItems.TRIGGER_RUNE.get() ) {
            effect = sb.append("trigger;").toString();
        }
    }

    public static void causeExplosion(Level level, Player player, Vec3 pos) {
        level.explode(null, DamageSource.playerAttack(player), null, pos.x, pos.y, pos.z, getPower(), hasFire(), Explosion.BlockInteraction.NONE);
    }

    public static void shootSparkBolt(Player player, Vec3 look, float power, boolean hasFire, boolean hasExplosion) {
        Vec3 center = CommonEvents.getEntityCenter(player);
        player.level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 1);
        player.level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5f, 2);
        SparkBoltEntity sparkBolt = new SparkBoltEntity(player.level, player, power, hasFire, hasExplosion);
        sparkBolt.setPos(sparkBolt.getX() + look.x, player.getY() + player.getEyeHeight(), sparkBolt.getZ() + look.z);
        sparkBolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 1.0F, 0.0F);
        player.level.addFreshEntity(sparkBolt);
    }
}
