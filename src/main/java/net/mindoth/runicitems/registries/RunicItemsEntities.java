package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.entity.spell.FamiliarBaseEntity;
import net.mindoth.runicitems.entity.spell.ProjectileBaseEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RunicItems.MOD_ID);

    public static final RegistryObject<EntityType<ProjectileBaseEntity>> PROJECTILE_BASE = ENTITIES.register("projectile_base",
            () -> getDefaultSizeEntityType(ProjectileBaseEntity::new));

    public static final RegistryObject<EntityType<FamiliarBaseEntity>> FAMILIAR_BASE = ENTITIES.register("familiar_base",
            () -> getDefaultSizeEntityType(FamiliarBaseEntity::new));



    private RunicItemsEntities() {}

    private static <T extends Entity> EntityType<T> getDefaultSizeEntityType(EntityType.EntityFactory<T> factory) {
        return getEntityType(factory, 0.25f, 0.25f);
    }

    private static <T extends Entity> EntityType<T> getEntityType(EntityType.EntityFactory<T> factory, float width, float height) {
        return getEntityType(factory, width, height, 128);
    }

    private static <T extends Entity> EntityType<T> getEntityType(EntityType.EntityFactory<T> factory, float width, float height, int trackingRange) {
        return EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, height).updateInterval(5).setTrackingRange(trackingRange).setShouldReceiveVelocityUpdates(true)
                .build("");
    }

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}
