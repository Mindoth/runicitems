package net.mindoth.runicitems.client;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class RunicItemsLayers {
    public static final ModelLayerLocation MAGIC_SPARK_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "magic_spark_layer"), "magic_spark");

    public static final ModelLayerLocation HEALING_BOLT_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "healing_bolt_layer"), "healing_bolt");

    public static final ModelLayerLocation METEOR_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "meteor_layer"), "meteor");

    public static final ModelLayerLocation COMET_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "comet_layer"), "comet");

    public static final ModelLayerLocation WITHER_SKULL_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "ri_wither_skull_layer"), "ri_wither_skull");
}
