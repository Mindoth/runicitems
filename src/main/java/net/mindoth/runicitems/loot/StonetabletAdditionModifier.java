package net.mindoth.runicitems.loot;

import com.google.gson.JsonObject;
import net.mindoth.runicitems.config.RunicItemsCommonConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class StonetabletAdditionModifier extends LootModifier {
    private final Item addedItem;

    protected StonetabletAdditionModifier(ILootCondition[] conditionsIn, Item addeditemIn) {
        super(conditionsIn);
        this.addedItem = addeditemIn;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        double r = context.getRandom().nextFloat();
        if ( generatedLoot.get(0).getItem() == Items.TOTEM_OF_UNDYING && r <= RunicItemsCommonConfig.TABLET_CHANCE.get() && r > 0 ) {
            generatedLoot.clear();
            generatedLoot.add(new ItemStack(addedItem, 1));
        }
        return generatedLoot;

    }

    public static class Serializer extends GlobalLootModifierSerializer<StonetabletAdditionModifier> {

        @Override
        public StonetabletAdditionModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditionsIn) {
            Item addedItem = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation((JSONUtils.getAsString(object, "item"))));
            return new StonetabletAdditionModifier(conditionsIn, addedItem);
        }

        @Override
        public JsonObject write(StonetabletAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(instance.addedItem).toString());
            return new JsonObject();
        }
    }
}
