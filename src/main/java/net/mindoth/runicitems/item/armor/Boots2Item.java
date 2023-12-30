package net.mindoth.runicitems.item.armor;

import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

public class Boots2Item extends ArmorItem {

    private final LazyValue<BipedModel<?>> model;

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    public Boots2Item(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Item.Properties pProperties) {
        super(pMaterial, pSlot, pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
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
}
