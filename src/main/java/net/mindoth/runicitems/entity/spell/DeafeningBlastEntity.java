package net.mindoth.runicitems.entity.spell;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.registries.RunicItemsEntities;
import net.mindoth.shadowizardlib.event.CommonEvents;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;

public class DeafeningBlastEntity extends AbstractProjectileEntity {

    @Override
    protected int getBasePower() {
        return 5;
    }

    @Override
    protected int getBaseEnemyPiercing() {
        return 99999;
    }

    public DeafeningBlastEntity(Level level, LivingEntity owner, Entity caster, IItemHandler itemHandler, int slot,
                            HashMap<Item, Integer> effects) {
        super(RunicItemsEntities.DEAFENING_BLAST.get(), level, owner, caster, itemHandler, slot, effects);
    }

    public DeafeningBlastEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public DeafeningBlastEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(RunicItemsEntities.DEAFENING_BLAST.get(), level);
    }

    @Override
    protected void hurtTarget(LivingEntity target) {
        addEffects(target);
        if ( power > 0 ) {
            target.hurt(DamageSource.indirectMagic(this, owner), power);
        }
    }

    @Override
    protected void addEffects(LivingEntity target) {
        if ( !target.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() ) {
            dropItem(target.getItemBySlot(EquipmentSlot.MAINHAND), target instanceof Player);
            target.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        if ( !target.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() ) {
            dropItem(target.getItemBySlot(EquipmentSlot.OFFHAND), target instanceof Player);
            target.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
        target.knockback(((double)this.power / 3), this.getDeltaMovement().x * -1, this.getDeltaMovement().z * -1);
    }

    protected void dropItem(ItemStack pDroppedItem, boolean pIncludeThrowerName) {
        Vec3 pos = CommonEvents.getEntityCenter(this);
        ItemEntity itementity = new ItemEntity(this.level, pos.x, this.getY(), pos.z, pDroppedItem);
        itementity.setPickUpDelay(40);
        if ( pIncludeThrowerName ) itementity.setThrower(this.getUUID());
        itementity.setDeltaMovement(this.getDeltaMovement());
        this.level.addFreshEntity(itementity);
    }

    @Override
    protected void spawnParticles() {
        float width = 1.0F;
        float step = 0.125F;
        float halver = width / step;
        float radians = Mth.DEG_TO_RAD * getYRot();
        int var = 64;
        for ( float i = 0; i < halver + 1; i++ ) {
            float curve = /*-0.75F +*/ -(i * i) / var;
            Vec3 direction = this.getDeltaMovement().normalize();
            direction = direction.multiply(curve, curve, curve);
            Vec3 center = CommonEvents.getEntityCenter(this).add(direction);
            double offset = step * ((i + 4.0F) - width / step / 2);
            double rotX = offset * width * Math.cos(radians);
            double rotZ = -offset * width * Math.sin(radians);

            ServerLevel level = (ServerLevel)this.level;
            SimpleParticleType particle;
            if ( i == halver ) particle = ParticleTypes.END_ROD;
            else particle = this.getParticle();
            level.sendParticles(particle, center.x + rotX, center.y, center.z + rotZ, 1, 0, 0, 0, 0);
        }
        for ( float i = halver; i > 0; i-- ) {
            float curve = /*-0.75F +*/ -(i * i) / var;
            Vec3 direction = this.getDeltaMovement().normalize();
            direction = direction.multiply(curve, curve, curve);
            Vec3 center = CommonEvents.getEntityCenter(this).add(direction);
            double offset = step * ((i + 4.0F) - width / step / 2);
            double rotX = offset * width * Math.cos(radians);
            double rotZ = -offset * width * Math.sin(radians);

            ServerLevel level = (ServerLevel)this.level;
            SimpleParticleType particle;
            if ( i == halver ) particle = ParticleTypes.END_ROD;
            else particle = this.getParticle();
            level.sendParticles(particle, center.x - rotX, center.y, center.z - rotZ, 1, 0, 0, 0, 0);
        }
    }

    public ResourceLocation getSpellTexture() {
        return new ResourceLocation(RunicItems.MOD_ID, "textures/spells/clear.png");
    }

    @Override
    protected SimpleParticleType getParticle() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }
}
