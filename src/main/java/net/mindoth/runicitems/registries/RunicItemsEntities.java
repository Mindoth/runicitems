package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.entity.minion.SkeletonMinionEntity;
import net.mindoth.runicitems.entity.spell.*;
import net.mindoth.runicitems.entity.minion.BlazeMinionEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RunicItems.MOD_ID);

    public static final RegistryObject<EntityType<MeteorEntity>> METEOR =
            ENTITIES.register("meteor", () -> registerEntity(EntityType.Builder.of(MeteorEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F).setCustomClientFactory(MeteorEntity::new), "meteor"));

    public static final RegistryObject<EntityType<WitherSkullEntity>> WITHER_SKULL =
            ENTITIES.register("ri_wither_skull", () -> registerEntity(EntityType.Builder.of(WitherSkullEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).setCustomClientFactory(WitherSkullEntity::new), "ri_wither_skull"));

    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE =
            ENTITIES.register("icicle", () -> registerEntity(EntityType.Builder.of(IcicleEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).setCustomClientFactory(IcicleEntity::new), "icicle"));



    public static final RegistryObject<EntityType<MagicSparkEntity>> MAGIC_SPARK = ENTITIES.register("magic_spark",
            () -> getProjectileEntityType(MagicSparkEntity::new));

    public static final RegistryObject<EntityType<HealingBoltEntity>> HEALING_BOLT = ENTITIES.register("healing_bolt",
            () -> getProjectileEntityType(HealingBoltEntity::new));

    public static final RegistryObject<EntityType<StormyCloudEntity>> STORMY_CLOUD = ENTITIES.register("stormy_cloud",
            () -> getDefaultSizeEntityType(StormyCloudEntity::new));

    public static final RegistryObject<EntityType<MagicalCloudEntity>> MAGICAL_CLOUD = ENTITIES.register("magical_cloud",
            () -> getDefaultSizeEntityType(MagicalCloudEntity::new));

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO = ENTITIES.register("tornado",
            () -> getDefaultSizeEntityType(TornadoEntity::new));

    public static final RegistryObject<EntityType<UnstableCloudEntity>> UNSTABLE_CLOUD = ENTITIES.register("unstable_cloud",
            () -> getDefaultSizeEntityType(UnstableCloudEntity::new));

    public static final RegistryObject<EntityType<DeafeningBlastEntity>> DEAFENING_BLAST = ENTITIES.register("deafening_blast",
            () -> getBigEntityType(DeafeningBlastEntity::new));



    public static final RegistryObject<EntityType<BlazeMinionEntity>> BLAZE_MINION =
            ENTITIES.register("blaze_minion", () -> EntityType.Builder.<BlazeMinionEntity>of(BlazeMinionEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(RunicItems.MOD_ID, "blaze_minion").toString()));

    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION =
            ENTITIES.register("skeleton_minion", () -> EntityType.Builder.<SkeletonMinionEntity>of(SkeletonMinionEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(RunicItems.MOD_ID, "skeleton_minion").toString()));



    private static final EntityType registerEntity(EntityType.Builder builder, String entityName) {
        return (EntityType) builder.build(entityName);
    }
    private RunicItemsEntities() {

    }
    private static <T extends Entity> EntityType<T> getDefaultSizeEntityType(EntityType.EntityFactory<T> factory) {
        return getEntityType(factory, 0, 0);
    }
    private static <T extends Entity> EntityType<T> getProjectileEntityType(EntityType.EntityFactory<T> factory) {
        return getEntityType(factory, 0.5F, 0.5F);
    }
    private static <T extends Entity> EntityType<T> getBigEntityType(EntityType.EntityFactory<T> factory) {
        return getEntityType(factory, 2.0F, 0.5F);
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
