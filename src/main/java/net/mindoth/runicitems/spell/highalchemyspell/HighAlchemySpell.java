package net.mindoth.runicitems.spell.highalchemyspell;

import net.mindoth.runicitems.RunicItems;
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
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicItems.MOD_ID)
public class HighAlchemySpell extends AbstractSpell {

    public static void shootMagic(PlayerEntity owner, Entity caster, AbstractSpell spell,
                                  Vector3d center, float xRot, float yRot, int useTime, Item rune) {
        World level = caster.level;

        if ( useTime > spell.getCooldown() * 3 ) {
            if ( caster == owner ) {
                if ( owner.getMainHandItem().getItem() == Items.BONE || owner.getOffhandItem().getItem() == Items.BONE ) {
                    ItemStack handItem;
                    if ( owner.getMainHandItem().getItem() == Items.BONE ) handItem = owner.getMainHandItem();
                    else handItem = owner.getOffhandItem();
                    int amount = handItem.getCount();
                    ItemEntity drop = new ItemEntity(level,
                            owner.getBoundingBox().getCenter().x, owner.getBoundingBox().getCenter().y, owner.getBoundingBox().getCenter().z,
                            new ItemStack(Items.APPLE, amount));
                    drop.setDeltaMovement(0, 0, 0);
                    drop.setNoPickUpDelay();
                    level.addFreshEntity(drop);
                    handItem.shrink(amount);
                }
                else if ( (owner.getMainHandItem().getItem().canBeDepleted() && owner.getMainHandItem().isRepairable())
                        || (owner.getOffhandItem().getItem().canBeDepleted() && owner.getOffhandItem().isRepairable()) ) {
                    ItemStack handItem;
                    //TODO Find a way to exclude cheap items like wooden and stone tools
                    if ( (owner.getMainHandItem().getItem().canBeDepleted() && owner.getMainHandItem().isRepairable()) ) handItem = owner.getMainHandItem();
                    else handItem = owner.getOffhandItem();
                    double quality = (1.0D - (handItem.getDamageValue() - handItem.getMaxDamage())) / 2;
                    if ( level.random.nextDouble() < quality ) {
                        ItemEntity drop = new ItemEntity(level,
                                owner.getBoundingBox().getCenter().x, owner.getBoundingBox().getCenter().y, owner.getBoundingBox().getCenter().z,
                                new ItemStack(Items.GOLD_INGOT, 1));
                        drop.setDeltaMovement(0, 0, 0);
                        drop.setNoPickUpDelay();
                        level.addFreshEntity(drop);
                    }
                    handItem.shrink(1);
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
    public float getDistance() {
        return 0.75F;
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
