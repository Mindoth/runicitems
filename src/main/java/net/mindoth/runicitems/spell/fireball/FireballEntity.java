package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.client.particle.EmberParticleData;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.ArrayList;

public class FireballEntity extends AbstractSpellEntity {

    public FireballEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.FIREBALL.get(), level);
    }

    public FireballEntity(EntityType<FireballEntity> entityType, World level) {
        super(entityType, level);
    }

    public FireballEntity(World level, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.FIREBALL.get(), level, owner, caster, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        if ( this.power > 0 ) {
            doSplashDamage();
        }
    }

    @Override
    protected void doBlockEffects(RayTraceResult result) {
        if ( this.power > 0 ) {
            doSplashDamage();
        }
    }

    protected void doSplashDamage() {
        ArrayList<LivingEntity> list = getEnemiesAround(this, this.level, this.scale * 2);
        for ( LivingEntity target : list ) {
            dealDamage(target);
            if ( isAlly(target) ) target.setSecondsOnFire(8);
        }
    }

    @Override
    protected void doClientHitEffects() {
        ClientWorld world = (ClientWorld)this.level;
        Vector3d pos = ShadowEvents.getEntityCenter(this);
        for ( int i = 0; i < 360; i++ ) {
            if ( i % 5 == 0 ) {
                world.addParticle(EmberParticleData.createData(getParticleColor(), entityData.get(SIZE) / 2, (int)(10 * entityData.get(SIZE))), true,
                        pos.x, this.getY(), pos.z, Math.cos(i) * 0.5F, 0, Math.sin(i) * 0.5F);
            }
        }
    }

    @Override
    protected void playHitSound() {
        Vector3d center = ShadowEvents.getEntityCenter(this);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.75F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 0.75F);
    }
}