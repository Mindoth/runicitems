package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.event.SpellBuilder;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public class MagicalCloudEntity extends FamiliarBaseEntity {

    public MagicalCloudEntity(EntityType<MagicalCloudEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MagicalCloudEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                             HashMap<Item, Integer> effects, Item rune, float xRot, float yRot) {
        super(RunicItemsEntities.MAGICAL_CLOUD.get(), level, owner, caster, itemHandler, slot, effects, rune, xRot, yRot);
    }

    @Override
    public void doTickEffects() {
        if ( nextSpellSlot >= 0 && this.tickCount % 40 == 0 && this.tickCount >= 40 ) {
            if ( itemHandler.getStackInSlot(nextSpellSlot).getItem() != RunicItemsItems.MAGICAL_CLOUD_RUNE.get()  ) {
                Entity nearest = SpellBuilder.getNearestEntity(this, level, this.range);
                if ( nearest != null ) {
                    SpellBuilder.lookAt(this, nearest);
                    SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.getXRot(), this.getYRot());
                }
                else {
                    SpellBuilder.cast((Player)owner, this, itemHandler, slot + 1, this.xRot, this.yRot);
                }
            }
        }
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.WITCH;
    }
}
