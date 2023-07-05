package net.mindoth.runicitems.item;

import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.mindoth.runicitems.util.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Boots2Item extends ArmorItem {

    public Boots2Item(ArmorMaterial pMaterial, EquipmentSlot pSlot, Item.Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {
            static Boots2Model model;

            @Override
            public Boots2Model getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default) {
                if (model == null) model = new Boots2Model(Minecraft.getInstance().getEntityModels().bakeLayer(ClientProxy.BOOTS2_ARMOR_LAYER));
                float pticks = Minecraft.getInstance().getFrameTime();
                float f = Mth.rotLerp(pticks, entity.yBodyRotO, entity.yBodyRot);
                float f1 = Mth.rotLerp(pticks, entity.yHeadRotO, entity.yHeadRot);
                float netHeadYaw = f1 - f;
                float netHeadPitch = Mth.lerp(pticks, entity.xRotO, entity.getXRot());
                model.slot = slot;
                model.copyFromDefault(_default);
                model.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
                return model;
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    public enum MaterialBoots implements ArmorMaterial {

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
        private final LazyLoadedValue<Ingredient> repairMaterial;

        MaterialBoots(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
        }

        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
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
