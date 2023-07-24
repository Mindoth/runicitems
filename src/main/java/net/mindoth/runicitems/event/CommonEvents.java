package net.mindoth.runicitems.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.mindoth.runicitems.registries.RunicItemsEnchantments;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void weaponsOnMobs(EntityJoinLevelEvent event) {
        if ( event.getEntity() instanceof WitherSkeleton ) {
            LivingEntity witherSkeleton = (LivingEntity)event.getEntity();
            double r = witherSkeleton.getRandom().nextFloat();
            if ( r <= RunicItemsCommonConfig.MALLET_CHANCE.get() && RunicItemsCommonConfig.MALLET_CHANCE.get() > 0 ) {
                witherSkeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RunicItemsItems.MALLET.get()));
            }
        }
    }

    @SubscribeEvent
    public static void malletTargetCracker(final LivingHurtEvent event) {
        if ( !event.getEntity().level.isClientSide ) {
            if ( event.getSource().getEntity() instanceof LivingEntity source ) {
                LivingEntity target =  event.getEntity();
                Item item = source.getMainHandItem().getItem();
                if ( item == RunicItemsItems.MALLET.get() && source.getMainHandItem().getEnchantmentLevel(RunicItemsEnchantments.TARGET_CRACKER.get()) > 0 ) {
                    int tier = source.getMainHandItem().getEnchantmentLevel(RunicItemsEnchantments.TARGET_CRACKER.get());
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
                        event.setAmount(event.getAmount() + ((1.5f * (tier - 1)) + 1.5f));
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
                        event.setAmount(event.getAmount() + 3);
                    }
                    else if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.EAGLE_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 6);
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
                        event.setAmount(event.getAmount() + 3);
                    }
                    else if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.WARRIOR_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 6);
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
                        event.setAmount(event.getAmount() + 3);
                    }
                    else if ( source.getItemBySlot(EquipmentSlot.FEET).getItem() == RunicItemsItems.SORCERER_BOOTS.get() ) {
                        event.setAmount(event.getAmount() + 6);
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

    private static final String TAG_UNDEAD = ("undead");

    @SubscribeEvent
    public static void stoneTabletEffect(LivingDamageEvent event) {
        if ( event.getEntity() instanceof Player && !event.getEntity().level.isClientSide ) {
            Player player = (Player)event.getEntity();
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            ServerLevel level = (ServerLevel)player.level;
            ItemStack itemStack;
            if ( player.getMainHandItem().getItem() == RunicItemsItems.STONE_TABLET.get() || player.getOffhandItem().getItem() == RunicItemsItems.STONE_TABLET.get() ) {
                if ( event.getAmount() >= player.getHealth() ) {
                    if ( player.getOffhandItem().getItem() == RunicItemsItems.STONE_TABLET.get() ) {
                        itemStack = player.getOffhandItem();
                    }
                    else {
                        itemStack = player.getMainHandItem();
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1, 0.5f);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1, 0.5f);
                    itemStack.shrink(1);
                    event.setAmount(0);
                    player.setHealth(1.0F);
                    player.removeAllEffects();
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                    if ( !data.getBoolean(TAG_UNDEAD) ) {
                        data.putBoolean(TAG_UNDEAD, true);
                        playerData.put(Player.PERSISTED_NBT_TAG, data);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void undeadFriendly(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity() instanceof Player player ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            if ( event.getLookingEntity() instanceof Mob ) {
                if ( data.getBoolean(TAG_UNDEAD) && ((Mob)event.getLookingEntity()).getMobType() == MobType.UNDEAD ) {
                    event.modifyVisibility(0);
                }
            }
        }
    }

    //Undead attributes
    @SubscribeEvent
    public static void onPlayerUpdate(final LivingEvent.LivingTickEvent event) {
        LivingEntity player = event.getEntity();
        if ( !player.level.isClientSide ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            if ( data.getBoolean(TAG_UNDEAD) ) {
                //Burn in the sun
                if ( !player.isInWaterOrRain() && !player.isOnFire() && player.level.canSeeSky(player.blockPosition()) && player.level.isDay() && !player.hasItemInSlot(EquipmentSlot.HEAD) ) {
                    player.setSecondsOnFire(8);
                }
                //Breathe in water
                if ( player.isEyeInFluid(FluidTags.WATER) && player.getAirSupply() < player.getMaxAirSupply() ) {
                    player.setAirSupply(player.getMaxAirSupply());
                }
                //Immunity to poison and regeneration
                if ( player.hasEffect(MobEffects.POISON) ) {
                    player.removeEffect(MobEffects.POISON);
                }
                if ( player.hasEffect(MobEffects.REGENERATION) ) {
                    player.removeEffect(MobEffects.REGENERATION);
                }
            }
        }
    }

    //Curing Undead Curse
    @SubscribeEvent
    public static void onPlayerAte(final LivingEntityUseItemEvent.Finish event) {
        LivingEntity player = event.getEntity();
        if ( !player.level.isClientSide ) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            if ( event.getItem().getItem().equals(Items.GOLDEN_APPLE) && player.hasEffect(MobEffects.WEAKNESS) ) {
                data.remove(TAG_UNDEAD);
                player.level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1, 1);
            }
        }
    }
}
