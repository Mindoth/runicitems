package net.mindoth.runicitems.event;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientReference {
    @OnlyIn(Dist.CLIENT)
    public static final ResourceLocation CLEAR = new ResourceLocation(RunicItems.MOD_ID, "textures/spells/clear.png");
    @OnlyIn(Dist.CLIENT)
    public static final ResourceLocation MAGIC_SPARK = new ResourceLocation(RunicItems.MOD_ID, "textures/spells/magic_spark.png");
    @OnlyIn(Dist.CLIENT)
    public static final ResourceLocation HEALING_BOLT = new ResourceLocation("textures/item/snowball.png");
}
