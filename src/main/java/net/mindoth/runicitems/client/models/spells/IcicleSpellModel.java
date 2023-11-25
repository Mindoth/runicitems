package net.mindoth.runicitems.client.models.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class IcicleSpellModel extends Model {
    private final ModelPart modelPart;

    public IcicleSpellModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.modelPart = root.getChild("modelPart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition modelPart = partdefinition.addOrReplaceChild("modelPart", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = modelPart.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, -20.5F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, -2).addBox(0.0F, -11.5F, -4.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, -2).addBox(0.0F, -18.5F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(0.0F, -14.5F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r2 = modelPart.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, -20.5F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, -2).addBox(0.0F, -11.5F, -4.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, -2).addBox(0.0F, -18.5F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(0.0F, -14.5F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
