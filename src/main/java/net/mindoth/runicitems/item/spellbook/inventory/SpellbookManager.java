package net.mindoth.runicitems.item.spellbook.inventory;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.spellbook.SpellbookType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SpellbookManager extends SavedData {
    private static final String NAME = RunicItems.MOD_ID + "_wand_data";

    private static final HashMap<UUID, SpellbookData> data = new HashMap<>();

    public static final SpellbookManager blankClient = new SpellbookManager();

    public HashMap<UUID, SpellbookData> getMap() { return data; }

    public static SpellbookManager get() {
        if ( Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ) {
            return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(SpellbookManager::load, SpellbookManager::new, NAME);
        }
        else return blankClient;
    }

    public Optional<SpellbookData> getWand(UUID uuid) {
        if ( data.containsKey(uuid) ) {
            return Optional.of(data.get(uuid));
        }
        return Optional.empty();
    }

    public SpellbookData getOrCreateWand(UUID uuid, SpellbookType tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new SpellbookData(id, tier);
        });
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack) {
        if ( stack.getOrCreateTag().contains("UUID") ) {
            UUID uuid = stack.getTag().getUUID("UUID");
            if ( data.containsKey(uuid) ) {
                return data.get(uuid).getOptional();
            }
        }
        return LazyOptional.empty();
    }

    public static SpellbookManager load(CompoundTag nbt) {
        if ( nbt.contains("Wands") ) {
            ListTag list = nbt.getList("Wands", Tag.TAG_COMPOUND);
            list.forEach((wandNBT) -> SpellbookData.fromNBT((CompoundTag) wandNBT).ifPresent((wand) -> data.put(wand.getUuid(), wand)));
        }
        return new SpellbookManager();
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound) {
        ListTag wands = new ListTag();
        data.forEach(((uuid, spellbookData) -> wands.add(spellbookData.toNBT())));
        compound.put("Wands", wands);
        return compound;
    }
}
