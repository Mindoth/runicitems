package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.spellbook.gui.SpellbookContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RunicItems.MOD_ID);
    public static final RegistryObject<ContainerType<SpellbookContainer>> SPELLBOOK_CONTAINER = CONTAINERS.register("spellbook_container", () -> IForgeContainerType.create(SpellbookContainer::fromNetwork));
}
