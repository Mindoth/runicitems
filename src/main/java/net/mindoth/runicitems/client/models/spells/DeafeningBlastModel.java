package net.mindoth.runicitems.client.models.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class DeafeningBlastModel extends Model {
    private final ModelPart modelPart;

    public DeafeningBlastModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.modelPart = root.getChild("modelPart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition modelPart = partdefinition.addOrReplaceChild("modelPart", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = modelPart.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 3).addBox(-17.25F, -21.25F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 4).addBox(-21.25F, -17.25F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-20.25F, -20.25F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-22.25F, -12.25F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(6, 8).addBox(-21.25F, -15.25F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-15.25F, -21.25F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-12.25F, -22.25F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
