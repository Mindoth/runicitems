package net.mindoth.runicitems.client.models.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Boots2Model<T extends Entity> extends ArmorModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "boots2_armor"), "main");

    public Boots2Model(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition root = createHumanoidAlias(mesh);
        PartDefinition right_foot = root.getChild("RightBoot");
        PartDefinition left_foot = root.getChild("LeftBoot");

        PartDefinition right = right_foot.addOrReplaceChild("RightBoot", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-3.0F, -17.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-3.0F, -17.0F, 3.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(3.0F, -17.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(18, 6).addBox(-3.0F, -17.0F, -3.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = right_foot.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4F, 7.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-0.4F, 8.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-0.4F, 6.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));

        PartDefinition left = left_foot.addOrReplaceChild("LeftBoot", CubeListBuilder.create().texOffs(12, 24).addBox(-3.0F, -17.0F, -3.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 12).addBox(3.0F, -17.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.0F, -17.0F, 3.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 6).addBox(-3.0F, -17.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r4 = left_foot.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 18).addBox(4.4F, 6.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 20).addBox(4.4F, 7.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 22).addBox(4.4F, 8.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));

        return LayerDefinition.create(mesh, 32, 32);
    }


    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
