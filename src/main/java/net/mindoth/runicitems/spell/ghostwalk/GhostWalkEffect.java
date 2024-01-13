package net.mindoth.runicitems.spell.ghostwalk;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class GhostWalkEffect extends Effect {

    public GhostWalkEffect(EffectType pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    /*@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void invisibilityPre(final RenderLivingEvent.Pre event) {
        LivingEntity livingEntity = event.getEntity();
        if ( livingEntity.hasEffect(RunicItemsEffects.GHOST_WALK.get()) && livingEntity.isInvisible() ) {
            event.setCanceled(true);
        }
    }*/

    /*@SubscribeEvent
    public static void noHat(final RenderPlayerEvent event) {
        PlayerEntity player = event.getPlayer();
        if ( player.hasEffect(RunicItemsEffects.GHOST_WALK.get()) && player.isInvisible() ) {
            event.getRenderer().getModel().hat.visible = false;
        }
    }*/

    @SubscribeEvent
    public static void visibilityModifier(LivingEvent.LivingVisibilityEvent event) {
        if ( ((LivingEntity)event.getEntity()).hasEffect(RunicItemsEffects.GHOST_WALK.get()) ) {
            event.modifyVisibility(0);
        }
    }

    @SubscribeEvent
    public static void removeOnAttack(LivingAttackEvent event) {
        if ( !(event.getSource().getEntity() instanceof LivingEntity) ) return;
        LivingEntity source = (LivingEntity)event.getSource().getEntity();
        if ( !source.hasEffect(RunicItemsEffects.GHOST_WALK.get()) ) return;
        source.removeEffect(RunicItemsEffects.GHOST_WALK.get());
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if ( !livingEntity.level.isClientSide ) {
            if ( lastHurtTimestamp != livingEntity.getLastHurtMobTimestamp() ) livingEntity.removeEffect(this);
            if ( !livingEntity.isInvisible() ) {
                livingEntity.setInvisible(true);
            }
            if ( livingEntity.getHealth() < livingEntity.getMaxHealth() && livingEntity.tickCount % 60 == 0 ) {
                livingEntity.heal((float)amplifier / 2);
            }
        }
        super.applyEffectTick(livingEntity, amplifier);
    }

    int lastHurtTimestamp;

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeModifierManager map, int amplifier) {
        super.addAttributeModifiers(livingEntity, map, amplifier);
        ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        double addition = (float)amplifier / 2;
        AttributeModifier MOVEMENT_SPEED = new AttributeModifier(UUID.fromString("d6262e54-0335-4551-b982-60fd36206b9d"), "Ghost Walk Modifier", addition, AttributeModifier.Operation.MULTIPLY_BASE);
        if ( movementSpeed != null && !movementSpeed.hasModifier(MOVEMENT_SPEED) ) movementSpeed.addPermanentModifier(MOVEMENT_SPEED);

        //EntityPredicate targetingCondition = EntityPredicate.forCombat().ignoreLineOfSight().selector(e -> (((MobEntity) e).getTarget() == livingEntity));
        EntityPredicate targetingCondition = new EntityPredicate();

        livingEntity.level.getNearbyEntities(MobEntity.class, targetingCondition, livingEntity, livingEntity.getBoundingBox().inflate(40D))
                .forEach(entityTargetingCaster -> {
                    entityTargetingCaster.setTarget(null);
                    entityTargetingCaster.setLastHurtMob(null);
                    entityTargetingCaster.setLastHurtByMob(null);
                    entityTargetingCaster.targetSelector.getRunningGoals().forEach(PrioritizedGoal::stop);
                });
        this.lastHurtTimestamp = livingEntity.getLastHurtMobTimestamp();
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeModifierManager map, int amplifier) {
        super.addAttributeModifiers(livingEntity, map, amplifier);
        ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        double addition = (float)amplifier / 2;
        AttributeModifier MOVEMENT_SPEED = new AttributeModifier(UUID.fromString("d6262e54-0335-4551-b982-60fd36206b9d"), "Ghost Walk Modifier", addition, AttributeModifier.Operation.MULTIPLY_BASE);
        if ( movementSpeed != null && movementSpeed.hasModifier(MOVEMENT_SPEED) ) movementSpeed.removeModifier(MOVEMENT_SPEED);
    }
}
