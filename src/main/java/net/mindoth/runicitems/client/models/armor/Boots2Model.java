package net.mindoth.runicitems.client.models.armor;

import net.mindoth.shadowizardlib.client.models.ArmorModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;

public class Boots2Model<T extends Entity> extends ArmorModel {
    private ModelRenderer cube_r1;
    private ModelRenderer cube_r2;

    public Boots2Model(EquipmentSlotType slot) {
        super(slot);
        this.texWidth = 32;
        this.texHeight = 32;

        RightFoot = new ModelRenderer(this);
        RightFoot.setPos(0.0F, 24.0F, 0.0F);
        RightFoot.texOffs(0, 0).addBox(-3.0F, 13.0F, -3.0F, 6.0F, 0.0F, 6.0F, 0.0F, false);
        RightFoot.texOffs(0, 6).addBox(-3.0F, 7.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        RightFoot.texOffs(18, 0).addBox(-3.0F, 7.0F, 3.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);
        RightFoot.texOffs(0, 12).addBox(3.0F, 7.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        RightFoot.texOffs(18, 6).addBox(-3.0F, 7.0F, -3.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-2.0F, 11.0F, 0.0F);
        RightFoot.addChild(cube_r1);
        setRotation(cube_r1, 0.0F, -0.1309F, -0.1309F);
        cube_r1.texOffs(0, 0).addBox(-0.95F, -4.0F, 0.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(0, 2).addBox(-0.95F, -3.0F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(0, 4).addBox(-0.95F, -5.0F, 1.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        LeftFoot = new ModelRenderer(this);
        LeftFoot.setPos(0.0F, 24.0F, 0.0F);
        LeftFoot.texOffs(12, 24).addBox(-3.0F, 7.0F, -3.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);
        LeftFoot.texOffs(12, 12).addBox(3.0F, 7.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        LeftFoot.texOffs(0, 24).addBox(-3.0F, 7.0F, 3.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);
        LeftFoot.texOffs(12, 6).addBox(-3.0F, 7.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        LeftFoot.texOffs(0, 6).addBox(-3.0F, 13.0F, -3.0F, 6.0F, 0.0F, 6.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-2.0F, 11.0F, 0.0F);
        LeftFoot.addChild(cube_r2);
        setRotation(cube_r2, 0.0F, 0.1309F, 0.1309F);
        cube_r2.texOffs(24, 18).addBox(5.0F, -5.5F, 2.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r2.texOffs(24, 20).addBox(5.0F, -4.5F, 1.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r2.texOffs(24, 22).addBox(5.0F, -3.5F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
    }
}
