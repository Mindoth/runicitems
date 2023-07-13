package net.mindoth.runicitems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class EagleBoots extends Boots2Item {
    public EagleBoots(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.runicitems.eagle_boots"));

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "runicitems:textures/models/armor/eagle_boots_model.png";
    }
}
