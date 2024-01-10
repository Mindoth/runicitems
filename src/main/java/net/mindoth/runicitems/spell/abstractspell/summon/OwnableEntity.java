package net.mindoth.runicitems.spell.abstractspell.summon;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface OwnableEntity {
    @Nullable
    UUID getOwnerUUID();

    @Nullable
    Entity getOwner();
}
