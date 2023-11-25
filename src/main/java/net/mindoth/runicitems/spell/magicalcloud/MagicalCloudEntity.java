package net.mindoth.runicitems.spell.magicalcloud;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.spell.abstractspell.AbstractCloudEntity;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class MagicalCloudEntity extends AbstractCloudEntity {

    public MagicalCloudEntity(EntityType<MagicalCloudEntity> entityType, Level level) {
        super(entityType, level);
        this.lastXRot = 9999;
        this.lastYRot = 9999;
    }

    private float lastXRot;
    private float lastYRot;

    public MagicalCloudEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                              HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.MAGICAL_CLOUD.get(), level, owner, caster, itemHandler, slot, effects);
    }

    @Override
    public void doTickEffects() {
        if ( this.tickCount % 40 == 0 ) {
            Entity nearest = CommonEvents.getNearestEntity(this, level, this.range);
            if ( nearest != null ) {
                Vec3 vec3 = CommonEvents.getEntityCenter(this);
                Vec3 pTarget = CommonEvents.getEntityCenter(nearest);
                double d0 = pTarget.x - vec3.x;
                double d1 = pTarget.y - vec3.y;
                double d2 = pTarget.z - vec3.z;
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                this.lastXRot = Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI))));
                this.lastYRot = Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
            }
            if ( lastXRot != 0 && lastYRot != 0 ) {
                SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.lastXRot * -1, this.lastYRot * -1);
            }
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.WITCH;
    }
}
