package net.mindoth.runicitems.particle;

import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;

public class GlowParticleData implements IParticleFactory<ColorParticleTypeData> {
    private final IAnimatedSprite sprites;
    public static final String NAME = "glow";

    public GlowParticleData(IAnimatedSprite spriteSet) {
        this.sprites = spriteSet;
    }

    public Particle createParticle(ColorParticleTypeData data, ClientWorld level, double x, double y, double z, double dx, double dy, double dz) {
        return new ParticleGlow(level, x, y, z, dx, dy, dz, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), data.color.getScale(), 10, this.sprites);
    }

    public static IParticleData createData(ParticleColor color) {
        return new ColorParticleTypeData(RunicItemsParticles.GLOW_TYPE, color, false);
    }
}
