package net.mindoth.runicitems.client.models.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class StarSpellModel extends Model {
    private final ModelPart modelPart;

    public StarSpellModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.modelPart = root.getChild("modelPart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition modelPart = partdefinition.addOrReplaceChild("modelPart", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -24.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(6, 9).addBox(-4.0F, -24.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-1.0F, -24.5F, 2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 6).addBox(2.0F, -24.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-1.0F, -24.5F, -4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
