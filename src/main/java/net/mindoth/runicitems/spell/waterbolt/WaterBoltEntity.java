package net.mindoth.runicitems.spell.waterbolt;

import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class WaterBoltEntity extends AbstractSpellEntity {

    public WaterBoltEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(RunicItemsEntities.WATERBOLT.get(), level);
    }

    public WaterBoltEntity(EntityType<WaterBoltEntity> entityType, World level) {
        super(entityType, level);
    }

    public WaterBoltEntity(World level, LivingEntity owner, Entity caster, AbstractSpell spell, String element, float scale) {
        super(RunicItemsEntities.WATERBOLT.get(), level, owner, caster, spell, element, scale);
    }

    @Override
    protected void doMobEffects(RayTraceResult result) {
        LivingEntity target = (LivingEntity)((EntityRayTraceResult)result).getEntity();
        if ( this.power > 0 ) {
            dealDamage(target);
        }
    }
}
