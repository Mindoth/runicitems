package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.blizzard.BlizzardSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        if ( this.power > 0 ) {
            doSplashDamage();
        }
        this.remove();
    }

    @Override
    protected void doBlockEffects(RayTraceResult result) {
        if ( this.power > 0 ) {
            doSplashDamage();
        }
    }

    protected void doSplashDamage() {
        ArrayList<LivingEntity> list = CommonEvents.getEntitiesAround(this, this.level, this.scale);
        for ( LivingEntity target : list ) {
            dealDamage(target);
            target.setSecondsOnFire(8);
        }
    }
}
