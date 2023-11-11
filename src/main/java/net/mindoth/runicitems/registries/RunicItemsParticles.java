package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RunicItems.MOD_ID);

    public static final RegistryObject<SimpleParticleType> GLOW_FIRE_PARTICLE =
            PARTICLE_TYPES.register("glow_fire_particle", () -> new SimpleParticleType(true));
}
