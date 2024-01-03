package net.mindoth.runicitems.item.spellbook;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.rune.RuneItem;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.gui.SpellbookContainer;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookManager;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class SpellbookItem extends Item {

    public static final String SLOT_TAG = "slot";

    public SpellbookItem(SpellbookType tier) {
        super(new Item.Properties().tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1));
        this.tier = tier;
    }

    final SpellbookType tier;

    @SubscribeEvent
    public static void whileCasting(LivingEntityUseItemEvent.Tick event) {
        if ( event.getEntity().level.isClientSide || event.getEntityLiving().isCrouching() ) return;
        if ( event.getItem().getItem() instanceof SpellbookItem && event.getEntityLiving() instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            ItemStack spellbook = event.getItem();
            final AbstractSpell spell = getSpell(spellbook);
            if ( player.tickCount % 5 == 0 ) cast(player, getSpellData(spellbook), spell);
        }
    }

    @SubscribeEvent
    public static void whileCasting(LivingEntityUseItemEvent.Stop event) {
        if ( event.getEntity().level.isClientSide || event.getEntityLiving().isCrouching() ) return;
        if ( event.getItem().getItem() instanceof SpellbookItem && event.getEntityLiving() instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            ItemStack spellbook = event.getItem();
            final AbstractSpell spell = getSpell(spellbook);
            addCooldown(player, spellbook.getItem(), spell);
        }
    }

    public static void setSlot(CompoundNBT nbt, int slot) {
        nbt.putInt(SpellbookItem.SLOT_TAG, slot);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        ItemStack spellbook = player.getItemInHand(handIn);
        if ( !spellbook.hasTag() ) setSlot(spellbook.getOrCreateTag(), 0);

        if ( !level.isClientSide && player instanceof ServerPlayerEntity && spellbook.getItem() instanceof SpellbookItem ) {
            SpellbookData data = SpellbookItem.getData(spellbook);
            if ( data.getUuid() != null ) {
                UUID uuid = data.getUuid();
                data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());
                if ( player.isCrouching() ) {
                    NetworkHooks.openGui(((ServerPlayerEntity) player), new SimpleNamedContainerProvider(
                            (windowId, playerInventory, playerEntity) -> new SpellbookContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                            spellbook.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
                }
                else {
                    if ( getRune(getSpellData(spellbook), spellbook.getTag().getInt(SLOT_TAG)).getItem() instanceof SpellRuneItem ) {
                        final AbstractSpell spell = getSpell(spellbook);
                        if ( spell.isChannel() ) {
                            player.startUsingItem(handIn);
                        }
                        else {
                            cast(player, getSpellData(spellbook), spell);
                            addCooldown(player, spellbook.getItem(), spell);
                        }
                    }
                }
            }
        }
        return ActionResult.pass(player.getItemInHand(handIn));
    }

    public static void cast(PlayerEntity player, IItemHandler itemHandler, AbstractSpell spell) {
        doSpell(player, player, itemHandler, spell, player.xRot, player.yRot);
    }

    public static void doSpell(PlayerEntity owner, Entity caster, IItemHandler itemHandler, final AbstractSpell spell, float xRot, float yRot) {
        Vector3d center;
        int distance = spell.getDistance();
        if ( distance > 0 ) {
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vector3d direction = CommonEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition(0).add(direction);
        }
        else center = new Vector3d(CommonEvents.getEntityCenter(caster).x, CommonEvents.getEntityCenter(caster).y + 0.5F, CommonEvents.getEntityCenter(caster).z);
        AbstractSpell.routeSpell(owner, caster, itemHandler, spell, center, xRot, yRot);
    }

    public static IItemHandler getSpellData(ItemStack ogStack) {
        SpellbookData ogData = SpellbookItem.getData(ogStack);
        final IItemHandler handler = ogData.getHandler();
        return handler;
    }

    public static Item getRune(IItemHandler itemHandler, int slot) {
        return itemHandler.getStackInSlot(slot).getItem();
    }

    public static AbstractSpell getSpell(ItemStack spellbook) {
        return ((SpellRuneItem)getRune(getSpellData(spellbook), spellbook.getTag().getInt(SLOT_TAG))).spell;
    }

    public static void addCooldown(PlayerEntity player,  Item spellbook, AbstractSpell spell) {
        player.getCooldowns().addCooldown(spellbook.getItem(), spell.getCooldown());
    }



    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
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
        return SpellbookManager.get().getOrCreateSpellbook(uuid, ((SpellbookItem) stack.getItem()).tier);
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
                if(!optional.isPresent())
                    optional = SpellbookManager.get().getCapability(stack);
                return optional.cast();
            }
            else
                return LazyOptional.empty();
        }
    }
}
