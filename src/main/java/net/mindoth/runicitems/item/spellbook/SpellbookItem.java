package net.mindoth.runicitems.item.spellbook;

import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.gui.SpellbookContainer;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookManager;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SpellbookItem extends Item {

    public SpellbookType tier;
    public static final String SLOT_TAG = "slot";

    public SpellbookItem(SpellbookType tier) {
        super(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1));
        this.tier = tier;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        ItemStack spellbook = player.getItemInHand(handIn);
        if ( !spellbook.getOrCreateTag().contains(SLOT_TAG) ) setSlot(spellbook.getOrCreateTag(), 0);
        if ( !level.isClientSide && player instanceof ServerPlayerEntity && spellbook.getItem() instanceof SpellbookItem ) {
            SpellbookData data = getData(spellbook);
            if ( data.getUuid() != null ) {
                UUID uuid = data.getUuid();
                data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());
                NetworkHooks.openGui(((ServerPlayerEntity)player), new SimpleNamedContainerProvider(
                        (windowId, playerInventory, playerEntity) -> new SpellbookContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                        spellbook.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            }
        }
        return ActionResult.pass(player.getItemInHand(handIn));
    }

    public static void setSlot(CompoundNBT nbt, int slot) {
        nbt.putInt(SLOT_TAG, slot);
    }

    public static int getSlot(ItemStack spellbook) {
        return spellbook.getTag().getInt(SpellbookItem.SLOT_TAG);
    }

    public static Item getRune(IItemHandler itemHandler, int slot) {
        return itemHandler.getStackInSlot(slot).getItem();
    }

    public static AbstractSpell getSpell(ItemStack spellbook) {
        return ((SpellRuneItem)getRune(getHandler(spellbook), spellbook.getTag().getInt(SLOT_TAG))).spell;
    }

    public static IItemHandler getHandler(ItemStack ogStack) {
        SpellbookData ogData = getData(ogStack);
        final IItemHandler handler = ogData.getHandler();
        return handler;
    }

    public static ItemStack getSpellBook(PlayerEntity player) {
        return player.inventory.getItem(SpellbookItem.bookSlot(player.inventory));
    }

    public static int bookSlot(PlayerInventory playerInventory) {
        int bookSlot = -1;
        for ( int i = 0; i < playerInventory.getContainerSize(); i++ ) {
            if ( playerInventory.getItem(i).getItem() instanceof SpellbookItem ) {
                bookSlot = i;
                break;
            }
        }
        return bookSlot;
    }

    public static SpellbookData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof SpellbookItem) ) return null;
        UUID uuid;
        CompoundNBT tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return SpellbookManager.get().getOrCreateSpellbook(uuid, ((SpellbookItem)stack.getItem()).tier);
    }

    public static SpellbookData getDataFromNBT(CompoundNBT tag, SpellbookType tier) {
        UUID uuid;
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return SpellbookManager.get().getOrCreateSpellbook(uuid, tier);
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return tier.rarity;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new SpellbookCaps(stack);
    }

    static class SpellbookCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public SpellbookCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if ( cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
                if ( !optional.isPresent() )
                    optional = SpellbookManager.get().getCapability(stack);
                return optional.cast();
            }
            else
                return LazyOptional.empty();
        }
    }
}
