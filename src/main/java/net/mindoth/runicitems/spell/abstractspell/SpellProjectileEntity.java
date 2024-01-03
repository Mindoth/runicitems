package net.mindoth.runicitems.spell.abstractspell;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

public class SpellProjectileEntity extends AbstractSpellEntity {

    public SpellProjectileEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.SPELL_PROJECTILE.get(), level);
    }

    public SpellProjectileEntity(EntityType<SpellProjectileEntity> entityType, World level) {
        super(entityType, level);
    }

    public SpellProjectileEntity(World level, LivingEntity owner, Entity caster, IItemHandler itemHandler, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, itemHandler, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        //LivingEntity target = (LivingEntity)((EntityRayTraceResult)result).getEntity();
        if ( power > 0 ) {
            doSplashDamage();
        }
        this.remove();
    }

    @Override
    protected void doBlockEffects(RayTraceResult result) {
        if ( power > 0 ) {
            if ( this.spell instanceof FireballSpell ) {
                doSplashDamage();
            }
        }
    }

    protected void doSplashDamage() {
        ArrayList<LivingEntity> list = CommonEvents.getEntitiesAround(this, this.level, this.scale);
        for ( LivingEntity target : list ) {
            damageTarget(target);
        }
    }

    protected void damageTarget(LivingEntity target) {
        target.hurt(DamageSource.indirectMagic(this, this.owner), this.power);
        target.setSecondsOnFire(8);
    }
}
