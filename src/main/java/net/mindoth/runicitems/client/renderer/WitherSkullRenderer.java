package net.mindoth.runicitems.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.RunicItemsLayers;
import net.mindoth.runicitems.client.models.spells.CubeSpellModel;
import net.mindoth.runicitems.entity.spell.WitherSkullEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WitherSkullRenderer extends EntityRenderer<WitherSkullEntity> {
    public static final ResourceLocation WITHER_SKULL_LOCATION = new ResourceLocation(RunicItems.MOD_ID,"textures/spells/wither_skull.png");
    private final CubeSpellModel model;

    public WitherSkullRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.model = new CubeSpellModel(p_174420_.bakeLayer(RunicItemsLayers.WITHER_SKULL_LAYER));
    }

    public void render(WitherSkullEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.scale(0.68584F, 0.68584F, 0.68584F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(WitherSkullEntity p_116109_) {
        return WITHER_SKULL_LOCATION;
    }
}
