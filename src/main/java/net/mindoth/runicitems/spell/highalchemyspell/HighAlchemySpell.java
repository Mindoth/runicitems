package net.mindoth.runicitems.spell.highalchemyspell;

import net.mindoth.runicitems.item.weapon.WandItem;
import net.mindoth.runicitems.network.PacketClientChargeUpEffects;
import net.mindoth.runicitems.network.RunicItemsNetwork;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class HighAlchemySpell extends AbstractSpell {

    public static void shootMagic(PlayerEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;

        if ( useTime > spell.getCooldown() * 3 ) {
            if ( caster == owner ) {
                Vector3d ownerPos = ShadowEvents.getEntityCenter(owner);
                ItemStack mainHandStack = owner.getMainHandItem();
                ItemStack offHandStack = owner.getOffhandItem();
                if ( mainHandStack.getItem() == Items.BONE || offHandStack.getItem() == Items.BONE ) {
                    ItemStack handItem;
                    if ( mainHandStack.getItem() == Items.BONE ) handItem = mainHandStack;
                    else handItem = offHandStack;
                    int amount = handItem.getCount();
                    ItemEntity drop = new ItemEntity(level, ownerPos.x, ownerPos.y, ownerPos.z, new ItemStack(Items.APPLE, amount));
                    drop.setDeltaMovement(0, 0, 0);
                    drop.setNoPickUpDelay();
                    level.addFreshEntity(drop);
                    handItem.shrink(amount);
                }
                else if ( (mainHandStack.getItem().canBeDepleted() && mainHandStack.isRepairable() && mainHandStack.getMaxDamage() > Items.GOLDEN_CHESTPLATE.getMaxDamage())
                        || (offHandStack.getItem().canBeDepleted() && offHandStack.isRepairable() && mainHandStack.getMaxDamage() > Items.GOLDEN_CHESTPLATE.getMaxDamage()) ) {
                    ItemStack handItem;
                    if ( (mainHandStack.getItem().canBeDepleted() && mainHandStack.isRepairable()) ) handItem = mainHandStack;
                    else handItem = offHandStack;
                    double quality = (double)(handItem.getMaxDamage() - handItem.getDamageValue()) / handItem.getMaxDamage();
                    System.out.println("RANDOM: " + level.random.nextDouble());
                    System.out.println("QUALITY: " + quality);
                    if ( level.random.nextDouble() <= quality ) {
                        ItemEntity drop = new ItemEntity(level, ownerPos.x, ownerPos.y, ownerPos.z, new ItemStack(Items.GOLD_INGOT, (int)spell.getPower()));
                        drop.setDeltaMovement(0, 0, 0);
                        drop.setNoPickUpDelay();
                        level.addFreshEntity(drop);
                    }
                    handItem.shrink(1);
                    playMagicShootSound(level, center);
                }

                if ( AbstractSpell.isPlayer(owner, caster) ) {
                    PlayerEntity player = (PlayerEntity)caster;
                    player.stopUsingItem();
                    WandItem.addCooldown(player, rune);
                }
            }
        }
    }

    @Override
    public float getPower() {
        return 1.0F;
    }

    @Override
    public float getDistance() {
        return 0.75F;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public void chargeUpEffects(World level, Entity caster, int useTime) {
        if ( level.isClientSide ) return;
        for ( int i = 0; i < 12; i++ ) {
            float size = 0.01F * useTime / 4;
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            Vector3d pos = ShadowEvents.getPoint(caster, this.getDistance(), 0.0F, true);

            ServerWorld world = (ServerWorld)level;
            RunicItemsNetwork.sendToNearby(level, caster, new PacketClientChargeUpEffects(255, 216, 0, size, 5 + world.random.nextInt(10),
                    pos.x + randX, pos.y - 0.25F + randY, pos.z + randZ));
        }
    }
}
