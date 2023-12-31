package net.mindoth.runicitems.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleGlow extends SpriteTexturedParticle {
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;

    public boolean disableDepthTest;
    public ParticleGlow(ClientWorld worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, IAnimatedSprite sprite, boolean disableDepthTest) {
        super(worldIn, x,y,z,0,0,0);
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0){
            this.colorR = this.colorR/255.0f;
        }
        if (this.colorG > 1.0){
            this.colorG = this.colorG/255.0f;
        }
        if (this.colorB > 1.0){
            this.colorB = this.colorB/255.0f;
        }
        this.setColor(colorR, colorG, colorB);
        this.lifetime = (int)((float)lifetime*0.5f);
        this.quadSize = 0;
        this.initScale = scale;
        this.xd = vx*2.0f;
        this.yd = vy*2.0f;
        this.zd = vz*2.0f;
        this.initAlpha = a;
        this.pickSprite(sprite);
        this.disableDepthTest = disableDepthTest;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return this.disableDepthTest ? ParticleRenderTypes.EMBER_RENDER_NO_MASK : ParticleRenderTypes.EMBER_RENDER;
    }

    @Override
    public int getLightColor(float pTicks){
        return 255;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }
}
