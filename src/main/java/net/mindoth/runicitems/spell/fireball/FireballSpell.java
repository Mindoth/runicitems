package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.client.particle.EmberParticleData;
import net.mindoth.runicitems.client.particle.ParticleColor;
import net.mindoth.runicitems.item.weapon.WandItem;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FireballSpell extends AbstractSpell {

    public static void shootMagic(PlayerEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;

        if ( useTime > 60 ) {
            if ( level.isClientSide ) return;
            AbstractSpellEntity projectile = new FireballEntity(level, owner, caster, spell, "fire", 1.2F);
            projectile.setNoGravity(!spell.getGravity());
            playFireShootSound(level, center);

            int adjuster;
            if ( caster != owner ) adjuster = -1;
            else adjuster = 1;
            projectile.setPos(center.x, center.y - 0.25F, center.z);
            projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, spell.getSpeed(), 1.0F);
            level.addFreshEntity(projectile);
            if ( AbstractSpell.isPlayer(owner, caster) ) {
                PlayerEntity player = (PlayerEntity)caster;
                player.stopUsingItem();
                //TODO find a better way to do this so client also gets cooldown
                WandItem.addCooldown(player, rune);
            }
        }
    }

    @Override
    public int getLife() {
        return 120;
    }

    @Override
    public float getPower() {
        return 16.0F;
    }

    @Override
    public float getSpeed() {
        return 1.4F;
    }

    @Override
    public int getDistance() {
        return 1;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public void chargeUpEffects(World level, PlayerEntity player, int useTime) {
        AbstractSpell spell = this;
        if ( !level.isClientSide ) return;
        for ( int i = 0; i < 12; i++ ) {
            ClientWorld world = (ClientWorld) level;
            float size = 0.02F * useTime / 8;
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            Vector3d pos = ShadowEvents.getPoint(player, spell.getDistance(), 0.0F, true);
            world.addParticle(EmberParticleData.createData(new ParticleColor(177, 63, 0), size,
                            5 + world.random.nextInt(10)), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
