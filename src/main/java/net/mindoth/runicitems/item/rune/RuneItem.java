package net.mindoth.runicitems.item.rune;

import net.mindoth.runicitems.item.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.spells.AbstractSpell;
import net.minecraft.world.item.Item;

public class RuneItem extends Item {

    public RuneItem(Properties pProperties, int drain) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
        this.drain = drain;
    }

    private final int drain;

    /*@OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.runicitems.rune_drain")
                .append(Component.literal(": " + getRuneDrain()).withStyle(ChatFormatting.GRAY)));

        super.appendHoverText(stack, world, tooltip, flagIn);
    }*/

    public int getRuneDrain() {
        return this.drain;
    }
}
