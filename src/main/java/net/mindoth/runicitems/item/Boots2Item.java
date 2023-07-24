package net.mindoth.runicitems.item;

import net.mindoth.runicitems.RunicItemsClient;
import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Consumer;

public class Boots2Item extends ArmorItem {

    public Boots2Item(ArmorMaterial pMaterial, EquipmentSlot pSlot, Item.Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(slot)),
                () -> () -> null);
    }

    private final LazyLoadedValue<HumanoidModel<?>> model;

    @OnlyIn(Dist.CLIENT)
    public HumanoidModel<?> provideArmorModelForSlot(EquipmentSlot slot) {
        return new Boots2Model<>(Minecraft.getInstance().getEntityModels().bakeLayer(RunicItemsClient.BOOTS2_LAYER), slot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return model.get();
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }
}
