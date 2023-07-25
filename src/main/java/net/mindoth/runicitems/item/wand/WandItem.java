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

    private static boolean isSpell(Item rune) {
        return rune instanceof SpellRuneItem;
    }

    private static Item getRune(int i, IItemHandler itemHandler) {
        return itemHandler.getStackInSlot(i).getItem();
    }

    private static Item getNextSpell(int i, IItemHandler itemHandler) {
        Item nextRune = null;
        for ( int j = i; j < itemHandler.getSlots(); j++ ) {
            if ( itemHandler.getStackInSlot(j).getItem() instanceof SpellRuneItem ) {
                nextRune = itemHandler.getStackInSlot(j).getItem();
            }
        }
        return nextRune;
    }

    private static Item getLastUtility(int i, IItemHandler itemHandler) {
        Item lastRune = null;
        for ( int j = i - 1; j > 0; j-- ) {
            if ( isSpell(itemHandler.getStackInSlot(j).getItem()) ) {
                break;
            }
            if ( itemHandler.getStackInSlot(j).getItem() instanceof RuneItem ) {
                lastRune = itemHandler.getStackInSlot(j).getItem();
            }
        }
        return lastRune;
    }

    private static int getCooldown() {
        return 20;
    }

    private static float getPower() {
        return 1.0f * amplifier;
    }

    private static boolean hasFire() {
        return effect.contains("fire");
    }

    private static boolean hasExplosion() {
        return effect.contains("explosion");
    }

    private void resetModifiers() {
        amplifier = 1;
        speed = 1;
        effect = "";
        sb = new StringBuilder();
    }

    private static int amplifier;
    private static int speed;
    private static String effect;
    private static StringBuilder sb;

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack wand = playerIn.getItemInHand(handIn);
        if ( !worldIn.isClientSide && playerIn instanceof ServerPlayer && wand.getItem() instanceof ModWand) {
            WandData data = ModWand.getData(wand);
            UUID uuid = data.getUuid();

            data.updateAccessRecords(playerIn.getName().getString(), System.currentTimeMillis());
            if ( playerIn.isShiftKeyDown() ) {
                NetworkHooks.openScreen(((ServerPlayer) playerIn), new SimpleMenuProvider( (windowId, playerInventory, playerEntity) -> new WandContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), wand.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            }
            else {
                if ( !playerIn.getCooldowns().isOnCooldown(wand.getItem()) ) {
                    cast(worldIn, playerIn, wand);
                }
                return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
            }
        }
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    private void cast(Level level, Player player, ItemStack wand) {
        if ( WandManager.get().getCapability(wand).isPresent() ) {
            IItemHandler itemHandler = WandManager.get().getCapability(wand).resolve().get();
            resetModifiers();
            for ( int i = 0; i < itemHandler.getSlots(); i++ ) {
                Item rune = itemHandler.getStackInSlot(i).getItem();
                if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                    if ( rune instanceof EffectRuneItem ) {
                        doEffect(rune);
                    }
                    if ( rune instanceof SpellRuneItem ) {
                        doSpell(player, level, i, itemHandler);
                    }
                }
            }
            player.getCooldowns().addCooldown(wand.getItem(), getCooldown());
        }
    }

    public void doSpell(Player player, Level level, int i, IItemHandler itemHandler) {
        Vec3 look = player.getLookAngle();

        System.out.println("last utility spell: " + getLastUtility(i, itemHandler));
        if ( getLastUtility(i, itemHandler) == RunicItemsItems.TRIGGER_RUNE.get() ) {
            if ( getNextSpell(i, itemHandler) == RunicItemsItems.FIRE_RUNE.get() ) {
                System.out.println("SET FIRE TAG");
                effect = sb.append("fire;").toString();
            }
            if ( getNextSpell(i, itemHandler) == RunicItemsItems.EXPLOSION_RUNE.get() ) {
                System.out.println("SET EXPLOSION TAG");
                effect = sb.append("explosion;").toString();
            }
        }

        if ( getRune(i, itemHandler) == RunicItemsItems.FIRE_RUNE.get() ) {
            System.out.println("SET PLAYER ON FIRE");
            player.setSecondsOnFire((int)getPower());
        }
        if ( getRune(i, itemHandler) == RunicItemsItems.EXPLOSION_RUNE.get() ) {
            System.out.println("EXPLODED PLAYER");
            causeExplosion(level, player, CommonEvents.getEntityCenter(player));
        }
        if ( getRune(i, itemHandler) == RunicItemsItems.SPARK_BOLT_RUNE.get() ) {
            System.out.println("SHOT SPARK");
            System.out.println("has fire: " + hasFire() + " has explosion: " + hasExplosion());
            shootSparkBolt(player, look, getPower(), hasFire(), hasExplosion());
        }

        System.out.println("RESETTING MODIFIERS");
        resetModifiers();
    }

    public void doEffect(Item rune) {
        if ( rune == RunicItemsItems.AMPLIFICATION_RUNE.get() ) {
            amplifier = 2;
        }
        if ( rune == RunicItemsItems.NULLIFICATION_RUNE.get() ) {
            amplifier = 0;
        }
    }

    public static void causeExplosion(Level level, Player player, Vec3 pos) {
        level.explode(null, DamageSource.playerAttack(player), null, pos.x, pos.y, pos.z, getPower(), hasFire(), Explosion.BlockInteraction.NONE);
    }

    public void shootSparkBolt(Player player, Vec3 look, float power, boolean hasFire, boolean hasExplosion) {
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
