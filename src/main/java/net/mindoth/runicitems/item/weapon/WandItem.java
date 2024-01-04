package net.mindoth.runicitems.item.weapon;

import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class WandItem extends Item {

    public WandItem(Properties pProperties) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB).stacksTo(1));
    }

    @Override
    public void onUseTick(World level, LivingEntity living, ItemStack wand, int pCount) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            if ( bookSlot(player.inventory) >= 0 ) {
                ItemStack spellbook = player.inventory.getItem(bookSlot(player.inventory));
                final AbstractSpell spell = SpellbookItem.getSpell(spellbook);
                if ( player.tickCount % 5 == 0 ) cast(player, spell);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack wand, World level, LivingEntity living, int pTimeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            if ( bookSlot(player.inventory) >= 0 ) {
                ItemStack spellbook = player.inventory.getItem(bookSlot(player.inventory));
                Item slotItem = SpellbookItem.getRune(SpellbookItem.getSpellData(spellbook), SpellbookItem.getSlot(spellbook)).getItem();
                final AbstractSpell spell = SpellbookItem.getSpell(spellbook);
                if ( spell.isChannel() ) addCooldown(player, slotItem, spell);
            }
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        if ( bookSlot(player.inventory) >= 0
                && !(player.getMainHandItem().getItem() instanceof SpellbookItem)
                && !(player.getOffhandItem().getItem() instanceof SpellbookItem) ) {
            ItemStack spellbook = player.inventory.getItem(bookSlot(player.inventory));
            ItemStack wand = player.getItemInHand(handIn);
            if ( !spellbook.hasTag() ) SpellbookItem.setSlot(spellbook.getOrCreateTag(), 0);
            if ( !level.isClientSide && player instanceof ServerPlayerEntity && wand.getItem() instanceof WandItem ) {
                SpellbookData data = SpellbookItem.getData(spellbook);
                if ( data.getUuid() != null ) {
                    Item slotItem = SpellbookItem.getRune(SpellbookItem.getSpellData(spellbook), SpellbookItem.getSlot(spellbook)).getItem();
                    if ( slotItem instanceof SpellRuneItem && !player.getCooldowns().isOnCooldown(slotItem) ) {
                        final AbstractSpell spell = SpellbookItem.getSpell(spellbook);
                        if ( spell.isChannel() ) {
                            player.startUsingItem(handIn);
                        }
                        else {
                            cast(player, spell);
                            addCooldown(player, slotItem, spell);
                        }
                    }
                }
            }
        }
        return ActionResult.pass(player.getItemInHand(handIn));
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

    public static ItemStack getSpellBook(PlayerEntity player) {
        return player.inventory.getItem(bookSlot(player.inventory));
    }

    public static void cast(PlayerEntity player, AbstractSpell spell) {
        doSpell(player, player, spell, player.xRot, player.yRot);
    }

    public static void doSpell(PlayerEntity owner, Entity caster, final AbstractSpell spell, float xRot, float yRot) {
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
        AbstractSpell.routeSpell(owner, caster, spell, center, xRot, yRot);
    }

    public static void addCooldown(PlayerEntity player, Item item, AbstractSpell spell) {
        player.getCooldowns().addCooldown(item, spell.getCooldown());
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
    }
}
