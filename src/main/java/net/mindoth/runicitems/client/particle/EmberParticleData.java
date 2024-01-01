package net.mindoth.runicitems.client.particle;

import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;

import java.util.Random;

public class EmberParticleData implements IParticleFactory<ColoredDynamicTypeData> {
    private final IAnimatedSprite spriteSet;
    public static final String NAME = "ember";

    public static Random random = new Random();
    public EmberParticleData(IAnimatedSprite sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColoredDynamicTypeData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ParticleEmber(worldIn, x,y,z,xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(),
                data.scale,
                data.age,  this.spriteSet);

    }

    public static IParticleData createData(ParticleColor color) {
        return new ColoredDynamicTypeData(RunicItemsParticles.EMBER_TYPE, color, 0.3F, 40);
    }

    public static IParticleData createData(ParticleColor color, float scale, int age) {
        return new ColoredDynamicTypeData(RunicItemsParticles.EMBER_TYPE, color, scale, age);
    }
}
