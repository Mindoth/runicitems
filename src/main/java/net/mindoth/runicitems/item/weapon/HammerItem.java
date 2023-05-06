package net.mindoth.runicitems.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.math.Vector3d;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HammerItem extends Item implements Vanishable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public HammerItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 9.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-3.2F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }


    public boolean canAttackBlock(BlockState p_43409_, Level p_43410_, BlockPos p_43411_, Player p_43412_) {
        return !p_43412_.isCreative();
    }

    public boolean hurtEnemy(ItemStack p_43390_, LivingEntity p_43391_, LivingEntity p_43392_) {
        p_43390_.hurtAndBreak(1, p_43392_, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack p_43399_, Level p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
        if ((double)p_43401_.getDestroySpeed(p_43400_, p_43402_) != 0.0D) {
            p_43399_.hurtAndBreak(2, p_43403_, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43383_) {
        return p_43383_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43383_);
    }

    public int getEnchantmentValue() {
        return 1;
    }



    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1 || player.getCooldowns().isOnCooldown(RunicItemsItems.HAMMER.get()) ) {
            return InteractionResultHolder.fail(itemstack);
        }
        else {
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 200);
            player.getItemInHand(hand).hurtAndBreak(1, player, (holder) -> holder.broadcastBreakEvent(hand));
            if (player.isOnGround()) {
                //TODO maybe useless?
                Block onBlock = level.getBlockState(player.getOnPos().below()).getBlock();
                List<Entity> entitiesAround = level.getEntities(player, player.getBoundingBox().inflate(5.0D, 5.0D, 5.0D));
                //Sound
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1, 0.5f);
                //Particles
                for (int i = 0; i < 360; i++) {
                    if (i % 20 == 0) {
                        //TODO block particle, might need to be on an interaction event for other players to see particles
                        level.addParticle(ParticleTypes.BLOCK, true, player.getX(), player.getY(), player.getZ(),
                                Math.cos(i) * 0.5D, 0.05D, Math.sin(i) * 0.5D);
                    }
                }
                for (Entity listedEntity : entitiesAround) {
                    if (listedEntity instanceof LivingEntity) {
                        if (player.hasLineOfSight(listedEntity) && listedEntity.isOnGround()) {
                            listedEntity.hurt(DamageSource.playerAttack(player), (float)Math.round(player.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2));
                        }
                    }
                }
            }
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
}
