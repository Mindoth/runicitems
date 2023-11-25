package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.spellbook.gui.SpellbookContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RunicItems.MOD_ID);
    public static final RegistryObject<MenuType<SpellbookContainer>> WAND_CONTAINER = CONTAINERS.register("wand_container", () -> IForgeMenuType.create(SpellbookContainer::fromNetwork));
}
