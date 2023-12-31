package net.mindoth.runicitems.particle;

import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;

public class GlowParticleData implements IParticleFactory<ColorParticleTypeData> {
    private final IAnimatedSprite spriteSet;
    public static final String NAME = "glow";

    public GlowParticleData(IAnimatedSprite sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColorParticleTypeData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ParticleGlow(worldIn, x,y,z,xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), 1.0F, 0.4F, 36, this.spriteSet, data.disableDepthTest);
    }

    public static IParticleData createData(ParticleColor color) {
        return new ColorParticleTypeData(RunicItemsParticles.GLOW_TYPE, color, false);
    }

    public static IParticleData createData(ParticleColor color, boolean disableDepthTest) {
        return new ColorParticleTypeData(RunicItemsParticles.GLOW_TYPE, color, disableDepthTest);
    }
}
