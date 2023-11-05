package net.mindoth.runicitems.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.mindoth.runicitems.entity.spell.StormyCloudEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class StormyCloudRenderer<T extends StormyCloudEntity> extends EntityRenderer<T> {

    public StormyCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private final Map<EntityType<?>, RenderType> entityCutOuts = new HashMap<>();

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        VertexConsumer vertexconsumer = bufferIn.getBuffer(getEntityCutOut(entityIn));
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
        p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private RenderType getEntityCutOut(T entity) {
        if (!entityCutOuts.containsKey(entity.getType())) entityCutOuts.put(entity.getType(), RenderType.entityCutout(getTextureLocation(entity)));
        return entityCutOuts.get(entity.getType());
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return pEntity.getSpellTexture();
    }
}
