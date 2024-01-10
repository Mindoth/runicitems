package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.spell.blizzard.BlizzardEntity;
import net.mindoth.runicitems.spell.fireball.FireballEntity;
import net.mindoth.runicitems.spell.raisedead.SkeletonMinionEntity;
import net.mindoth.runicitems.spell.tornado.TornadoEntity;
import net.mindoth.runicitems.spell.waterbolt.WaterBoltEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RunicItems.MOD_ID);

    public static final RegistryObject<EntityType<BlizzardEntity>> BLIZZARD
            = registerEntity(EntityType.Builder.<BlizzardEntity>of(BlizzardEntity::new,
            EntityClassification.MISC).sized(0.75F, 0.75F).setCustomClientFactory(BlizzardEntity::new), "blizzard");

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO
            = registerEntity(EntityType.Builder.<TornadoEntity>of(TornadoEntity::new,
            EntityClassification.MISC).sized(1.0F, 1.0F).setCustomClientFactory(TornadoEntity::new), "tornado");

    public static final RegistryObject<EntityType<FireballEntity>> FIREBALL
            = registerEntity(EntityType.Builder.<FireballEntity>of(FireballEntity::new,
            EntityClassification.MISC).sized(1.0F, 1.0F).setCustomClientFactory(FireballEntity::new), "fireball");

    public static final RegistryObject<EntityType<WaterBoltEntity>> WATERBOLT
            = registerEntity(EntityType.Builder.<WaterBoltEntity>of(WaterBoltEntity::new,
            EntityClassification.MISC).sized(0.5F, 0.5F).setCustomClientFactory(WaterBoltEntity::new), "waterbolt");


    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION =
            ENTITIES.register("skeleton_minion", () -> EntityType.Builder.<SkeletonMinionEntity>of(SkeletonMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(RunicItems.MOD_ID, "skeleton_minion").toString()));


    private static final <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}
