package net.mindoth.runicitems.client.models.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class BoltSpellModel extends Model {
    private final ModelPart modelPart;

    public BoltSpellModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.modelPart = root.getChild("modelPart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition modelPart = partdefinition.addOrReplaceChild("modelPart", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -26.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 8).addBox(-1.5F, -25.0F, -2.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = modelPart.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -25.0F, -2.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = modelPart.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(6, 7).addBox(-1.5F, -25.0F, -2.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r3 = modelPart.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, -25.0F, -2.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
