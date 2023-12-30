package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.spell.icicle.IcicleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RunicItems.MOD_ID);


    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE
            = registerEntity(EntityType.Builder.<IcicleEntity>of(IcicleEntity::new,
            EntityClassification.MISC).sized(0.5F, 0.5F).setCustomClientFactory(IcicleEntity::new), "icicle");


    private static final <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}
