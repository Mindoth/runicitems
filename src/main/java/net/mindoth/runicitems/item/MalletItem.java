package net.mindoth.runicitems.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MalletItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public MalletItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-3.0F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean canAttackBlock(BlockState p_43409_, World p_43410_, BlockPos p_43411_, PlayerEntity p_43412_) {
        return !p_43412_.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack p_43390_, LivingEntity p_43391_, LivingEntity p_43392_) {
        p_43390_.hurtAndBreak(1, p_43392_, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack p_43399_, World p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
        if ((double)p_43401_.getDestroySpeed(p_43400_, p_43402_) != 0.0D) {
            p_43399_.hurtAndBreak(2, p_43403_, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType p_43383_) {
        return p_43383_ == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43383_);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
