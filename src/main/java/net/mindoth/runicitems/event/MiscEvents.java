package net.mindoth.runicitems.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class MiscEvents {

    @SubscribeEvent
    public static void cancelFastEquip(final PlayerInteractEvent.RightClickItem event) {
        //if ( event.getWorld().isClientSide ) return;
        PlayerEntity player = event.getPlayer();
        if ( event.getItemStack().getItem() instanceof ArmorItem
                && (player.getMainHandItem().getItem() instanceof WandItem || player.getOffhandItem().getItem() instanceof WandItem) ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void weaponsOnMobs(EntityJoinWorldEvent event) {
        if ( event.getEntity() instanceof WitherSkeletonEntity ) {
            LivingEntity witherSkeleton = (LivingEntity)event.getEntity();
            double r = witherSkeleton.getRandom().nextFloat();
            if ( r <= RunicItemsCommonConfig.MALLET_CHANCE.get() && RunicItemsCommonConfig.MALLET_CHANCE.get() > 0 ) {
                witherSkeleton.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(RunicItemsItems.MALLET.get()));
            }
        }
    }

    @SubscribeEvent
    public static void malletTargetCracker(final LivingHurtEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            if ( event.getSource().getEntity() instanceof LivingEntity ) {
                LivingEntity source = (LivingEntity)event.getSource().getEntity();
                LivingEntity target =  event.getEntityLiving();
                Item item = source.getMainHandItem().getItem();

                if ( item == RunicItemsItems.MALLET.get() && EnchantmentHelper.getEnchantmentLevel(RunicItemsEnchantments.TARGET_CRACKER.get(), source) > 0 ) {
                    int tier = EnchantmentHelper.getEnchantmentLevel(RunicItemsEnchantments.TARGET_CRACKER.get(), source);
                    float coverage = target.getArmorCoverPercentage();
                    boolean flag = false;
                    if ( coverage <= 0.25f && tier >= 1 ) {
                        flag = true;
                    }
                    else if ( coverage <= 0.50f && tier >= 2 ) {
                        flag = true;
                    }
                    else if ( coverage <= 0.75f && tier >= 3 ) {
                        flag = true;
                    }
                    else if ( coverage <= 1.00f && tier >= 4 ) {
                        flag = true;
                    }
                    if ( flag ) {
                        event.setAmount(event.getAmount() + (((1 + coverage) * (tier - 1)) + (1 + coverage)));
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
                    if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.ARCHER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.ARCHERBOOTS_BONUS.get());
                    }
                    else if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.EAGLE_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.EAGLEBOOTS_BONUS.get());
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
                    if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.FIGHTER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.FIGHTERBOOTS_BONUS.get());
                    }
                    else if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.WARRIOR_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.WARRIORBOOTS_BONUS.get());
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
                    if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.WIZARD_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.WIZARDBOOTS_BONUS.get());
                    }
                    else if ( source.getItemBySlot(EquipmentSlotType.FEET).getItem() == RunicItemsItems.SORCERER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + RunicItemsCommonConfig.SORCERERBOOTS_BONUS.get());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if ( RunicItemsCommonConfig.WIZARDBOOTS_TRADE.get() ) {
            if ( event.getType() == VillagerProfession.CLERIC ) {
                Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
                ItemStack stack = new ItemStack(RunicItemsItems.WIZARD_BOOTS.get(), 1);
                int villagerLevel = 5;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 64),
                        stack, 12, 30, 0.05F));
            }
        }
    }

    private static final String TAG_UNDEAD = ("undead");

    @SubscribeEvent
    public static void stoneTabletEffect(LivingDamageEvent event) {
        if ( event.getEntity() instanceof PlayerEntity && !event.getEntity().level.isClientSide ) {
            PlayerEntity player = (PlayerEntity)event.getEntity();
            CompoundNBT playerData = player.getPersistentData();
            CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            ServerWorld level = (ServerWorld)player.level;
            ItemStack itemStack;
            if ( player.getMainHandItem().getItem() == RunicItemsItems.STONE_TABLET.get() || player.getOffhandItem().getItem() == RunicItemsItems.STONE_TABLET.get() ) {
                if ( event.getAmount() >= player.getHealth() ) {
                    if ( player.getOffhandItem().getItem() == RunicItemsItems.STONE_TABLET.get() ) {
                        itemStack = player.getOffhandItem();
                    }
                    else {
                        itemStack = player.getMainHandItem();
                    }
                    Vector3d center = ShadowEvents.getEntityCenter(player);
                    level.playSound(null, center.x, center.y, center.z,
                            SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1, 0.5f);
                    level.playSound(null, center.x, center.y, center.z,
                            SoundEvents.WITHER_SPAWN, SoundCategory.PLAYERS, 1, 0.5f);
                    itemStack.shrink(1);
                    event.setAmount(0);
                    player.setHealth(1.0F);
                    player.removeAllEffects();
                    player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                    player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                    if ( !data.getBoolean(TAG_UNDEAD) ) {
                        data.putBoolean(TAG_UNDEAD, true);
                        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void undeadFriendly(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity() instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)event.getEntity();
            CompoundNBT playerData = player.getPersistentData();
            CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if ( event.getLookingEntity() instanceof MobEntity) {
                if ( data.getBoolean(TAG_UNDEAD) && ((MobEntity)event.getLookingEntity()).getMobType() == CreatureAttribute.UNDEAD ) {
                    event.modifyVisibility(0);
                }
            }
        }
    }

    //Undead attributes
    @SubscribeEvent
    public static void onPlayerUpdate(final TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if ( !player.level.isClientSide ) {
            CompoundNBT playerData = player.getPersistentData();
            CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if ( data.getBoolean(TAG_UNDEAD) ) {
                //Burn in the sun
                if ( !player.isInWaterOrRain() && !player.isOnFire() && player.level.canSeeSky(player.blockPosition()) && player.level.isDay() && !player.hasItemInSlot(EquipmentSlotType.HEAD) ) {
                    player.setSecondsOnFire(8);
                }
                //Breathe in water
                if ( player.isEyeInFluid(FluidTags.WATER) && player.getAirSupply() < player.getMaxAirSupply() ) {
                    player.setAirSupply(player.getMaxAirSupply());
                }
                //Immunity to poison and regeneration
                if ( player.hasEffect(Effects.POISON) ) {
                    player.removeEffect(Effects.POISON);
                }
                if ( player.hasEffect(Effects.REGENERATION) ) {
                    player.removeEffect(Effects.REGENERATION);
                }
            }
        }
    }

    //Curing Undead Curse
    @SubscribeEvent
    public static void onPlayerAte(final LivingEntityUseItemEvent.Finish event) {
        LivingEntity player = event.getEntityLiving();
        if ( !player.level.isClientSide ) {
            CompoundNBT playerData = player.getPersistentData();
            CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if ( event.getItem().getItem().equals(Items.GOLDEN_APPLE) && player.hasEffect(Effects.WEAKNESS) ) {
                data.remove(TAG_UNDEAD);
                Vector3d center = ShadowEvents.getEntityCenter(player);
                player.level.playSound(null, center.x, center.y, center.z,
                        SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1, 1);
            }
        }
    }
}
