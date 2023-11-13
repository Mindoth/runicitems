package net.mindoth.runicitems.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GlowProjectile extends TextureSheetParticle {
    public float colorR;
    public float colorG;
    public float colorB;
    public float initScale;
    public float initX;
    public float initY;
    public float initZ;
    public float destX;
    public float destY;
    public float destZ;
    protected GlowProjectile(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float scale, int lifetime, SpriteSet sprite) {
        super(level, x, y, z, 0, 0, 0);
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0) {
            this.colorR = this.colorR / 255.0F;
        }
        if (this.colorG > 1.0) {
            this.colorG = this.colorG / 255.0F;
        }
        if (this.colorB > 1.0) {
            this.colorB = this.colorB / 255.0F;
        }
        this.setColor(colorR, colorG, colorB);
        this.lifetime = lifetime;
        this.quadSize = scale;
        this.initScale = scale;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.initX = (float) x;
        this.initY = (float) y;
        this.initZ = (float) z;
        this.destX = (float) vx;
        this.destY = (float) vy;
        this.destZ = (float) vz;
        this.roll = 2.0f * (float) Math.PI;
        this.pickSprite(sprite);
    }
    @Override
    public void tick() {
        super.tick();
        this.alpha = (-(1/(float)lifetime) * age + 1);
        /*if (level.random.nextInt(6) == 0) {
            this.age++;
        }
        float lifeCoeff = (float) this.age / (float) this.lifetime;
        this.x = ((1.0f - lifeCoeff) * initX + (lifeCoeff) * destX);
        this.y = ((1.0f - lifeCoeff) * initY + (lifeCoeff) * destY);
        this.z = ((1.0f - lifeCoeff) * initZ + (lifeCoeff) * destZ);
        this.quadSize = initScale - initScale * lifeCoeff;
        this.alpha = 1.0f - lifeCoeff;
        this.oRoll = roll;*/
    }

    @Override
    public boolean isAlive() {
        return this.age < lifetime;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.EMBER_RENDER;
    }


    @Override
    public int getLightColor(float pTicks) {
        return 255;
    }

    @OnlyIn(Dist.CLIENT)
    public static class IceProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public IceProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new GlowProjectile(level, x, y, z, dx, dy, dz, 49F, 119F, 249F,0.4F, 10, this.sprites);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class StormProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public StormProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new GlowProjectile(level, x, y, z, dx, dy, dz, 206F, 0F, 206F,0.4F, 10, this.sprites);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FireProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public FireProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new GlowProjectile(level, x, y, z, dx, dy, dz, 177F, 63F, 0F,0.4F, 10, this.sprites);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class DeafeningBlastProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public DeafeningBlastProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new GlowProjectile(level, x, y, z, dx, dy, dz, 49F, 119F, 249F,0.4F, 5, this.sprites);
        }
    }
}
