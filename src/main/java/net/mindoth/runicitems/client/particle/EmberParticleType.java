package net.mindoth.runicitems.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.particles.ParticleType;

public class EmberParticleType extends ParticleType<ColoredDynamicTypeData> {
    public EmberParticleType() {
        super(false, ColoredDynamicTypeData.DESERIALIZER);
    }

    @Override
    public Codec<ColoredDynamicTypeData> codec() {
        return ColoredDynamicTypeData.CODEC;
    }
}
