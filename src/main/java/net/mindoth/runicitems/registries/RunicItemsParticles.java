package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RunicItemsParticles {
    @ObjectHolder(RunicItems.MOD_ID + ":" + GlowParticleData.NAME) public static ParticleType<ColorParticleTypeData> GLOW_TYPE;
    @ObjectHolder(RunicItems.MOD_ID + ":" + ParticleLineData.NAME) public static ParticleType<ColoredDynamicTypeData> LINE_TYPE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        IForgeRegistry<ParticleType<?>> r = event.getRegistry();
        r.register(new GlowParticleType().setRegistryName(GlowParticleData.NAME));
        r.register(new LineParticleType().setRegistryName(ParticleLineData.NAME));

    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(GLOW_TYPE, GlowParticleData::new);
        Minecraft.getInstance().particleEngine.register(LINE_TYPE, ParticleLineData::new);

    }
}
