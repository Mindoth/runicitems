package net.mindoth.runicitems.item.weapon;

import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.item.spellbook.inventory.SpellbookData;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
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
    public void onUseTick(World level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            if ( SpellbookItem.bookSlot(player.inventory) >= 0 ) {
                ItemStack spellbook = SpellbookItem.getSpellBook(player);
                doSpell(player, player, SpellbookItem.getSpell(spellbook), getUseDuration(wand) - timeLeft);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack wand, World level, LivingEntity living, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            if ( SpellbookItem.bookSlot(player.inventory) >= 0 ) {
                ItemStack spellbook = player.inventory.getItem(SpellbookItem.bookSlot(player.inventory));
                Item slotItem = SpellbookItem.getRune(SpellbookItem.getHandler(spellbook), SpellbookItem.getSlot(spellbook)).getItem();
                if ( SpellbookItem.getSpell(spellbook).isChannel() ) addCooldown(player, slotItem);
            }
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        if ( SpellbookItem.bookSlot(player.inventory) >= 0
                && !(player.getMainHandItem().getItem() instanceof SpellbookItem)
                && !(player.getOffhandItem().getItem() instanceof SpellbookItem) ) {
            ItemStack spellbook = player.inventory.getItem(SpellbookItem.bookSlot(player.inventory));
            ItemStack wand = player.getItemInHand(handIn);
            if ( !spellbook.hasTag() ) SpellbookItem.setSlot(spellbook.getOrCreateTag(), 0);
            if ( !level.isClientSide && player instanceof ServerPlayerEntity && wand.getItem() instanceof WandItem ) {
                SpellbookData data = SpellbookItem.getData(spellbook);
                if ( data.getUuid() != null ) {
                    Item slotItem = SpellbookItem.getRune(SpellbookItem.getHandler(spellbook), SpellbookItem.getSlot(spellbook)).getItem();
                    if ( slotItem instanceof SpellRuneItem && !player.getCooldowns().isOnCooldown(slotItem) ) {
                        final AbstractSpell spell = SpellbookItem.getSpell(spellbook);
                        if ( spell.isChannel() ) {
                            player.startUsingItem(handIn);
                        }
                        else {
                            doSpell(player, player, spell, 0);
                            addCooldown(player, slotItem);
                            return ActionResult.success(player.getItemInHand(handIn));
                        }
                    }
                }
            }
        }
        return ActionResult.pass(player.getItemInHand(handIn));
    }

    public static void doSpell(PlayerEntity owner, Entity caster, final AbstractSpell spell, int useTime) {
        float xRot = caster.xRot;
        float yRot = caster.yRot;
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
        AbstractSpell.routeSpell(owner, caster, spell, center, xRot, yRot, useTime);
    }

    public static void addCooldown(PlayerEntity player, Item item) {
        player.getCooldowns().addCooldown(item, ((SpellRuneItem)item).spell.getCooldown());
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
    }

    public static @Nonnull ItemStack getHeldWand(PlayerEntity playerEntity) {
        ItemStack wand = playerEntity.getMainHandItem().getItem() instanceof WandItem ? playerEntity.getMainHandItem() : null;
        return wand == null ? (playerEntity.getOffhandItem().getItem() instanceof WandItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : wand;
    }
}
