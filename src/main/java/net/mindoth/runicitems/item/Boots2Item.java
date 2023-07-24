package net.mindoth.runicitems.item;

import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.mindoth.runicitems.util.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

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
}
