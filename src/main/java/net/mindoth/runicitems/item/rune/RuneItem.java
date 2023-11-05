package net.mindoth.runicitems.item.rune;

import net.mindoth.runicitems.itemgroup.RunicItemsItemGroup;
import net.mindoth.runicitems.registries.RunicItemsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RuneItem extends Item {
    public RuneItem(Properties pProperties, int drain) {
        super(pProperties.tab(RunicItemsItemGroup.RUNIC_ITEMS_TAB));
        this.drain = drain;
    }

    private final int drain;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.runicitems.rune_drain")
                .append(Component.literal(": " + getRuneDrain()).withStyle(ChatFormatting.GRAY)));

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    public int getRuneDrain() {
        return this.drain;
    }
}
