package net.mindoth.runicitems.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleEmber extends SpriteTexturedParticle {
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

    protected ParticleEmber(ClientWorld level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float scale, int lifetime, IAnimatedSprite sprite) {
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
        this.roll = 2.0f * (float)Math.PI;
        this.pickSprite(sprite);
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = (-(1 / (float)lifetime) * age + 1);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return ParticleRenderTypes.RUNIC_RENDER;
    }


    @Override
    public int getLightColor(float pTicks){
        return 255;
    }

    @Override
    public boolean isAlive() {
        return this.age < lifetime;
    }
}
