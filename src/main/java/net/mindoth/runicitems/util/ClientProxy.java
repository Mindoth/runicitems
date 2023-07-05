package net.mindoth.runicitems.util;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.client.models.armor.Boots2Model;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy implements SidedProxy {

    public static final ModelLayerLocation BOOTS2_ARMOR_LAYER = new ModelLayerLocation(new ResourceLocation(RunicItems.MOD_ID, "boots2_armor"), "main");
    public static Boots2Model BOOTS2_ARMOR_MODEL = null;

    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public void init() {
    }

    @Override
    public void openCodexGui() {
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ClientProxy.BOOTS2_ARMOR_LAYER, Boots2Model::createBodyLayer);
    }
}
