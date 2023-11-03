package net.mindoth.runicitems.item;

import net.mindoth.runicitems.client.gui.WandContainer;
import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.inventory.WandData;
import net.mindoth.runicitems.inventory.WandManager;
import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.itemgroup.RunicItemsItemGroup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class WandItem extends Item {
    public WandItem(WandType tier, int durability) {
        super(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1).durability(durability));
        this.tier = tier;
    }
    final WandType tier;

    public static WandData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof WandItem))
            return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return WandManager.get().getOrCreateWand(uuid, ((WandItem) stack.getItem()).tier);
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return this.tier.rarity;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new WandCaps(stack);
    }

    static class WandCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public WandCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if(!this.optional.isPresent())
                    this.optional = WandManager.get().getCapability(this.stack);
                return this.optional.cast();
            }
            else
                return LazyOptional.empty();
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack wand = playerIn.getItemInHand(handIn);
        if ( !worldIn.isClientSide && playerIn instanceof ServerPlayer && wand.getItem() instanceof WandItem) {
            WandData data = WandItem.getData(wand);
            UUID uuid = data.getUuid();

            data.updateAccessRecords(playerIn.getName().getString(), System.currentTimeMillis());
            if ( playerIn.isShiftKeyDown() ) {
                NetworkHooks.openScreen(((ServerPlayer) playerIn), new SimpleMenuProvider( (windowId, playerInventory, playerEntity) -> new WandContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), wand.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            }
            else {
                IItemHandler itemHandler = WandManager.get().getCapability(wand).resolve().get();
                if ( !playerIn.getCooldowns().isOnCooldown(wand.getItem()) ) {
                    if ( checkDurability(itemHandler, wand) ) {
                        SpellBuilder.cast(playerIn, playerIn, itemHandler, 0, playerIn.getXRot(), playerIn.getYRot());
                        drainDurability(getDrainAmount(itemHandler), wand, playerIn, handIn);
                        addCooldown(playerIn, wand);
                        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
                    }
                    else {
                        castFail(playerIn);
                        addCooldown(playerIn, wand);
                        return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
                    }
                }
            }
        }
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if ( pLevel.isClientSide ) return;
        if ( pStack.getDamageValue() > 0 ) {
            pStack.setDamageValue(pStack.getDamageValue() - 1);
        }
    }

    public void castFail(Player player) {
        player.displayClientMessage(Component.translatable("message.runicitems.failedcast"), true);
        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1, 1);
    }

    private static void addCooldown(Player player, ItemStack wand) {
        player.getCooldowns().addCooldown(wand.getItem(), getCooldown(wand.getItem()));
    }

    private static int getCooldown(Item wand) {
        if ( wand instanceof WandItem ) {
            return 20;
        }
        else return 0;
    }

    private static boolean checkDurability(IItemHandler itemHandler, ItemStack wand) {
        return getDrainAmount(itemHandler) < (wand.getMaxDamage() - wand.getDamageValue());
    }

    private static int getDrainAmount(IItemHandler itemHandler) {
        int drain = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++ ) {
            if ( !itemHandler.getStackInSlot(i).isEmpty() ) {
                Item item = SpellBuilder.getRune(itemHandler, i);
                if ( item instanceof RuneItem rune ) {
                    drain += rune.getRuneDrain();
                }
            }
        }
        return drain;
    }

    private static void drainDurability(int amount, ItemStack wand, Player player, InteractionHand hand) {
        for ( int i = 0; i < amount; i++ ) {
            wand.hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(hand));
        }
    }
}
