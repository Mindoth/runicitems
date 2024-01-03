package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.runicitems.spell.abstractspell.SpellProjectileEntity;
import net.mindoth.runicitems.spell.tornado.TornadoEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RunicItems.MOD_ID);

    public static final RegistryObject<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE
            = registerEntity(EntityType.Builder.<SpellProjectileEntity>of(SpellProjectileEntity::new,
            EntityClassification.MISC).sized(0.75F, 0.75F).setCustomClientFactory(SpellProjectileEntity::new), "projectile");

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO
            = registerEntity(EntityType.Builder.<TornadoEntity>of(TornadoEntity::new,
            EntityClassification.MISC).sized(1.0F, 1.0F).setCustomClientFactory(TornadoEntity::new), "tornado");

    private static final <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }
}
