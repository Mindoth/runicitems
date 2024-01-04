package net.mindoth.runicitems.network.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

    void init();

    World getClientWorld();

    Minecraft getMinecraft();

    PlayerEntity getPlayer();
}
