package net.mindoth.runicitems.client;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class RunicItemsLayers {

    public static final ModelLayerLocation BOOTS2_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "boots2_armor_layer"), "boots2_armor");

    public static final ModelLayerLocation METEOR_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "meteor_layer"), "meteor");

    public static final ModelLayerLocation WITHER_SKULL_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "ri_wither_skull_layer"), "ri_wither_skull");

    public static final ModelLayerLocation ICICLE_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "icicle_layer"), "icicle");

    public static final ModelLayerLocation DEAFENING_BLAST_LAYER = new ModelLayerLocation(
            new ResourceLocation(RunicItems.MOD_ID, "deafening_blast_layer"), "deafening_blast");
}
