package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.entity.spell.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RunicItems.MOD_ID);

    public static final RegistryObject<EntityType<MagicSparkEntity>> MAGIC_SPARK =
            ENTITIES.register("magic_spark", () -> registerEntity(EntityType.Builder.of(MagicSparkEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).setCustomClientFactory(MagicSparkEntity::new), "magic_spark"));

    public static final RegistryObject<EntityType<HealingBoltEntity>> HEALING_BOLT =
            ENTITIES.register("healing_bolt", () -> registerEntity(EntityType.Builder.of(HealingBoltEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).setCustomClientFactory(HealingBoltEntity::new), "healing_bolt"));

    public static final RegistryObject<EntityType<MeteorEntity>> METEOR =
            ENTITIES.register("meteor", () -> registerEntity(EntityType.Builder.of(MeteorEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f).setCustomClientFactory(MeteorEntity::new), "meteor"));

    public static final RegistryObject<EntityType<CometEntity>> COMET =
            ENTITIES.register("comet", () -> registerEntity(EntityType.Builder.of(CometEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f).setCustomClientFactory(CometEntity::new), "comet"));

    public static final RegistryObject<EntityType<WitherSkullEntity>> WITHER_SKULL =
            ENTITIES.register("ri_wither_skull", () -> registerEntity(EntityType.Builder.of(WitherSkullEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).setCustomClientFactory(WitherSkullEntity::new), "ri_wither_skull"));

    private static final EntityType registerEntity(EntityType.Builder builder, String entityName) {
        return (EntityType) builder.build(entityName);
    }



    public static final RegistryObject<EntityType<StormyCloudEntity>> STORMY_CLOUD = ENTITIES.register("stormy_cloud",
            () -> getDefaultSizeEntityType(StormyCloudEntity::new));

    public static final RegistryObject<EntityType<MagicalCloudEntity>> MAGICAL_CLOUD = ENTITIES.register("magical_cloud",
            () -> getDefaultSizeEntityType(MagicalCloudEntity::new));

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
}
