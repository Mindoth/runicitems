package net.mindoth.runicitems.spell.fireball;

import net.mindoth.runicitems.item.weapon.WandItem;
import net.mindoth.runicitems.network.PacketClientChargeUpEffects;
import net.mindoth.runicitems.network.RunicItemsNetwork;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FireballSpell extends AbstractSpell {

    public static void shootMagic(PlayerEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;

        if ( useTime > spell.getCooldown() * 3 ) {
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
        return 2.0F;
    }

    @Override
    public float getDistance() {
        return 0.5F;
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
    public void chargeUpEffects(World level, Entity caster, int useTime) {
        if ( level.isClientSide ) return;
        for ( int i = 0; i < 12; i++ ) {
            float size = 0.02F * useTime / 4;
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            Vector3d pos = ShadowEvents.getPoint(caster, this.getDistance(), 0.0F, true);

            ServerWorld world = (ServerWorld)level;
            RunicItemsNetwork.sendToNearby(level, caster, new PacketClientChargeUpEffects(177, 63, 0, size, 5 + world.random.nextInt(10),
                    pos.x + randX, pos.y - 0.25F + randY, pos.z + randZ));
        }
    }
}
