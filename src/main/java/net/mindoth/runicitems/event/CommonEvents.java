package net.mindoth.runicitems.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class CommonEvents {

    //Enchantment Freezing
    @SubscribeEvent
    public static void malletFreezingEvent(final AttackEntityEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            Player source = event.getEntity();
            if ( event.getTarget() instanceof LivingEntity ) {
                LivingEntity target = (LivingEntity)event.getTarget();
                Item item = source.getMainHandItem().getItem();
                if ( item == RunicItemsItems.MALLET.get() && source.getMainHandItem().getEnchantmentLevel(RunicItemsEnchantments.FREEZING.get()) > 0 ) {
                    if ( target.canFreeze() && !source.isAlliedTo(target) && (!(target instanceof ArmorStand) || !((ArmorStand)target).isMarker()) ) {
                        target.setTicksFrozen(target.getTicksFrozen() + (Math.round(source.getAttackStrengthScale(0.5f) * 280)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void rangedBonus(final LivingHurtEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            if ( event.getSource().getEntity() instanceof LivingEntity  ) {
                LivingEntity source = (LivingEntity)event.getSource().getEntity();
                if ( !event.getSource().isMagic() && event.getSource().isProjectile() ) {
                    if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.ARCHER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 4);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void meleeBonus(final LivingHurtEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            if ( event.getSource().getEntity() instanceof LivingEntity  ) {
                LivingEntity source = (LivingEntity)event.getSource().getEntity();
                if ( !event.getSource().isMagic() && event.getSource().getDirectEntity() == source ) {
                    if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.FIGHTER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 4);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void magicBonus(final LivingHurtEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            if ( event.getSource().getEntity() instanceof LivingEntity  ) {
                LivingEntity source = (LivingEntity)event.getSource().getEntity();
                if ( event.getSource().isMagic() ) {
                    if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.WIZARD_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 4);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if ( event.getType() == VillagerProfession.CLERIC ) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack stack = new ItemStack(RunicItemsItems.WIZARD_BOOTS.get(), 1);
            int villagerLevel = 5;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 64),
                    stack, 12, 30, 0.05F));
        }
    }
}
