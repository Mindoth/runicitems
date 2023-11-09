package net.mindoth.runicitems.effect;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class GhostWalkEffect extends MobEffect {

    public GhostWalkEffect(MobEffectCategory pCategory, int pColor) {
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

    @SubscribeEvent
    public static void invisibilityPre(final RenderLivingEvent.Pre event) {
        LivingEntity livingEntity = event.getEntity();
        if ( livingEntity.hasEffect(RunicItemsEffects.GHOST_WALK.get()) && livingEntity.isInvisible() ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void visibilityModifier(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(RunicItemsEffects.GHOST_WALK.get()) ) {
            event.modifyVisibility(0);
        }
    }

    @SubscribeEvent
    public static void removeOnAttack(LivingHurtEvent event) {
        if ( !(event.getSource().getEntity() instanceof Mob || event.getSource().getEntity() instanceof Player) ) return;
        if ( !event.getEntity().hasEffect(RunicItemsEffects.GHOST_WALK.get()) ) return;
        event.getEntity().removeEffect(RunicItemsEffects.GHOST_WALK.get());
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
    public void addAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap map, int amplifier) {
        super.addAttributeModifiers(livingEntity, map, amplifier);
        AttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        double addition = (float)amplifier / 2;
        AttributeModifier MOVEMENT_SPEED = new AttributeModifier(UUID.fromString("d6262e54-0335-4551-b982-60fd36206b9d"), "Ghost Walk Modifier", addition, AttributeModifier.Operation.MULTIPLY_BASE);
        if ( movementSpeed != null && !movementSpeed.hasModifier(MOVEMENT_SPEED) ) movementSpeed.addPermanentModifier(MOVEMENT_SPEED);

        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
            return (((Mob) e).getTarget() == livingEntity);
        });

        livingEntity.level.getNearbyEntities(Mob.class, targetingCondition, livingEntity, livingEntity.getBoundingBox().inflate(40D))
                .forEach(entityTargetingCaster -> {
                    entityTargetingCaster.setTarget(null);
                    entityTargetingCaster.setLastHurtMob(null);
                    entityTargetingCaster.setLastHurtByMob(null);
                    entityTargetingCaster.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                });
        this.lastHurtTimestamp = livingEntity.getLastHurtMobTimestamp();
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap map, int amplifier) {
        super.addAttributeModifiers(livingEntity, map, amplifier);
        AttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        double addition = (float)amplifier / 2;
        AttributeModifier MOVEMENT_SPEED = new AttributeModifier(UUID.fromString("d6262e54-0335-4551-b982-60fd36206b9d"), "Ghost Walk Modifier", addition, AttributeModifier.Operation.MULTIPLY_BASE);
        if ( movementSpeed != null && movementSpeed.hasModifier(MOVEMENT_SPEED) ) movementSpeed.removeModifier(MOVEMENT_SPEED);
    }
}
