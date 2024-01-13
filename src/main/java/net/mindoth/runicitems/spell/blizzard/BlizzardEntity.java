package net.mindoth.runicitems.spell.blizzard;

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
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class BlizzardEntity extends AbstractSpellEntity {

    public BlizzardEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.BLIZZARD.get(), level);
    }

    public BlizzardEntity(EntityType<BlizzardEntity> entityType, World level) {
        super(entityType, level);
    }

    public BlizzardEntity(World level, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.BLIZZARD.get(), level, owner, caster, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        LivingEntity target = (LivingEntity)((EntityRayTraceResult)result).getEntity();
        if ( this.power > 0 ) {
            dealDamage(target);
        }
    }

    @Override
    protected void doClientHitEffects() {
        ClientWorld world = (ClientWorld)this.level;
        Vector3d pos = ShadowEvents.getEntityCenter(this);
        for ( int i = 0; i < 360; i++ ) {
            if ( i % 5 == 0 ) {
                world.addParticle(EmberParticleData.createData(getParticleColor(), entityData.get(SIZE) / 2, (int)(20 * entityData.get(SIZE))), true,
                        pos.x, this.getY(), pos.z, Math.cos(i) * 0.25F, 0, Math.sin(i) * 0.25F);
            }
        }
    }

    @Override
    protected void playHitSound() {
        Vector3d center = ShadowEvents.getEntityCenter(this);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.GLASS_BREAK, SoundCategory.PLAYERS, 0.25F, 0.75F);
    }
}