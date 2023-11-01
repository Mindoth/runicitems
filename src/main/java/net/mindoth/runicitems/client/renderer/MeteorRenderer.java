package net.mindoth.runicitems.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.RunicItemsLayers;
import net.mindoth.runicitems.client.models.spells.BlockSpellModel;
import net.mindoth.runicitems.entity.spell.MeteorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MeteorRenderer extends EntityRenderer<MeteorEntity> {
    public static final ResourceLocation METEOR_LOCATION = new ResourceLocation(RunicItems.MOD_ID,"textures/spells/meteor.png");
    private final BlockSpellModel model;

    public MeteorRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.model = new BlockSpellModel(p_174420_.bakeLayer(RunicItemsLayers.METEOR_LAYER));
    }

    public void render(MeteorEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        poseStack.mulPose(new Quaternion(Vector3f.YP, 0F, true));
        poseStack.mulPose(new Quaternion(Vector3f.ZN, (entity.tickCount + partialTicks) * 30F, true));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(MeteorEntity p_116109_) {
        return METEOR_LOCATION;
    }
}
