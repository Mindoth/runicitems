package net.mindoth.runicitems.item.spellbook;

import net.mindoth.runicitems.item.spellbook.gui.SpellbookContainer;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookManager;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
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

public class SpellbookItem extends Item {
    public SpellbookItem(SpellbookType tier, int durability) {
        super(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1).durability(durability));
        this.tier = tier;
    }

    final SpellbookType tier;

    //TODO doesn't work in multiplayer for some reason
    /*@OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        ChatFormatting color;
        if ( stack.getDamageValue() <= 0 ) {
            color = ChatFormatting.GREEN;
        }
        else color = ChatFormatting.GRAY;

        tooltip.add(Component.translatable("tooltip.runicitems.wand_charge")
                .append(Component.literal(": "))
                .append(Component.literal("" + (stack.getMaxDamage() - stack.getDamageValue() - 1)).withStyle(color)));

        super.appendHoverText(stack, world, tooltip, flagIn);
    }*/

    public static SpellbookData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof SpellbookItem) ) return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return SpellbookManager.get().getOrCreateWand(uuid, ((SpellbookItem) stack.getItem()).tier);
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
                if ( !this.optional.isPresent() )
                    this.optional = SpellbookManager.get().getCapability(this.stack);
                return this.optional.cast();
            }
            else
                return LazyOptional.empty();
        }
    }

    /*@Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if ( !oldStack.isEmpty() || !newStack.isEmpty() ) {
            if ( oldStack.getItem() == newStack.getItem() && !slotChanged && oldStack.getItem() instanceof WandItem && newStack.getItem() instanceof WandItem )
                return false;
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }*/

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        ItemStack spellbook = player.getItemInHand(handIn);
        if ( !level.isClientSide && player instanceof ServerPlayer && spellbook.getItem() instanceof SpellbookItem ) {
            SpellbookData data = SpellbookItem.getData(spellbook);
            if ( data.getUuid() != null ) {
                UUID uuid = data.getUuid();
                data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());
                NetworkHooks.openScreen(((ServerPlayer) player), new SimpleMenuProvider( (windowId, playerInventory, playerEntity) -> new SpellbookContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), spellbook.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(handIn));
    }

    /*private void castFail(Player player) {
        player.displayClientMessage(Component.translatable("message.runicitems.failedcast"), true);
        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if ( pLevel.isClientSide ) return;
        if ( pStack.getDamageValue() > 0 ) {
            pStack.setDamageValue(pStack.getDamageValue() - 1);
        }
    }

    private void addCooldown(Player player, ItemStack wand) {
        player.getCooldowns().addCooldown(wand.getItem(), getCooldown(wand.getItem()));
    }

    private int getCooldown(Item wand) {
        if ( wand instanceof WandItem ) {
            return 20;
        }
        else return 0;
    }*/
}
