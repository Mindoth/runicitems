package net.mindoth.runicitems.item;

import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Boots2Item extends ArmorItem {

    private final LazyValue<BipedModel<?>> model;

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    public Boots2Item(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Item.Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyValue<>(() -> this.provideArmorModelForSlot(slot)),
                () -> () -> null);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) model.get();
    }

    @OnlyIn(Dist.CLIENT)
    public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
        return new Boots2Model<>(slot);
    }

    public enum MaterialBoots implements IArmorMaterial {

        BOOTS2("boots2_armor", 0, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_CHAIN,
                0.0F, 0.0F, () -> {
            return Ingredient.of(Items.STRING);
        });

        private static final int[] MAX_DAMAGE_ARRAY = new int[] { 0, 0, 0, 0 };
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;
        private final float knockbackResistance;
        private final LazyValue<Ingredient> repairMaterial;

        MaterialBoots(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairMaterial = new LazyValue<>(repairMaterial);
        }

        @Override
        public int getDurabilityForSlot(EquipmentSlotType slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlotType slotIn) {
            return this.damageReductionAmountArray[slotIn.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return this.soundEvent;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairMaterial.get();
        }

        @OnlyIn(Dist.CLIENT)
        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        /**
         * Gets the percentage of knockback resistance provided by armor of the material.
         */
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }
}
