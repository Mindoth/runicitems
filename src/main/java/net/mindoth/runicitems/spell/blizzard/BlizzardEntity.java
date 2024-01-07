package net.mindoth.runicitems.spell.blizzard;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.fireball.FireballSpell;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.ArrayList;

public class BlizzardEntity extends AbstractSpellEntity {

    public BlizzardEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.BLIZZARD.get(), level);
    }

    public BlizzardEntity(EntityType<BlizzardEntity> entityType, World level) {
        super(entityType, level);
    }

    public BlizzardEntity(World level, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.TORNADO.get(), level, owner, caster, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        LivingEntity target = (LivingEntity)((EntityRayTraceResult)result).getEntity();
        if ( this.power > 0 ) {
            dealDamage(target);
        }
        this.remove();
    }

    @Override
    protected void doBlockEffects(RayTraceResult result) {
    }
}
