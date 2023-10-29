package net.mindoth.runicitems.item;

import net.mindoth.runicitems.client.gui.WandContainer;
import net.mindoth.runicitems.inventory.WandData;
import net.mindoth.runicitems.inventory.WandManager;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.runicitems.event.SpellBuilder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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
    public WandItem(WandType tier) {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).fireResistant());
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
                if ( !playerIn.getCooldowns().isOnCooldown(wand.getItem()) ) {
                    IItemHandler itemHandler = WandManager.get().getCapability(wand).resolve().get();
                    SpellBuilder.cast(playerIn, playerIn, itemHandler, 0, playerIn.getXRot(), playerIn.getYRot());

                    playerIn.getCooldowns().addCooldown(wand.getItem(), getdelay(wand.getItem()));
                    return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
                }
            }
        }
        playerIn.getCooldowns().addCooldown(wand.getItem(), getCooldown(wand.getItem()));
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    public static int getdelay(Item wand) {
        if ( wand == RunicItemsItems.BASIC_WAND.get() ) {
            return 20;
        }
        else return 0;
    }

    public static int getCooldown(Item wand) {
        if ( wand == RunicItemsItems.BASIC_WAND.get() ) {
            return 10;
        }
        else return 0;
    }
}
