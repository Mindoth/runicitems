package net.mindoth.runicitems.item.spellbook.inventory;

import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SpellbookUtils {

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public static Optional<UUID> getUUID(@Nonnull ItemStack stack) {
        if ( stack.getItem() instanceof SpellbookItem && stack.hasTag() && stack.getTag().contains("UUID") ) {
            return Optional.of(stack.getTag().getUUID("UUID"));
        }
        else return Optional.empty();
    }
}
