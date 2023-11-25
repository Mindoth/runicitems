package net.mindoth.runicitems.spell.icicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.RunicItemsLayers;
import net.mindoth.runicitems.client.models.spells.IcicleSpellModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IcicleRenderer extends EntityRenderer<IcicleEntity> {
    public static final ResourceLocation ICICLE_LOCATION = new ResourceLocation(RunicItems.MOD_ID,"textures/spells/icicle.png");
    private final IcicleSpellModel model;

    public IcicleRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.model = new IcicleSpellModel(p_174420_.bakeLayer(RunicItemsLayers.ICICLE_LAYER));
    }

    public void render(IcicleEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.scale(0.68584F, 0.68584F, 0.68584F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(IcicleEntity p_116109_) {
        return ICICLE_LOCATION;
    }
}
