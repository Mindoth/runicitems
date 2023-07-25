package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.entity.projectile.SparkBoltEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RunicItems.MOD_ID);
    public static final RegistryObject<EntityType<SparkBoltEntity>> SPARK_BOLT = ENTITIES.register("spark_bolt",
            () -> getDefaultSizeEntityType(SparkBoltEntity::new));



    private RunicItemsEntities() {}

    private static <T extends Entity> EntityType<T> getDefaultSizeEntityType(EntityType.EntityFactory<T> factory) {
        return getEntityType(factory, 0, 0);
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
