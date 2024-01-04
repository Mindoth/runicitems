package net.mindoth.runicitems.item.spellbook.inventory;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.spellbook.SpellbookType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SpellbookManager extends WorldSavedData {
    private static final String NAME = RunicItems.MOD_ID + "_spellbook_data";

    private static final HashMap<UUID, SpellbookData> data = new HashMap<>();

    public static final SpellbookManager blankClient = new SpellbookManager();

    public SpellbookManager() {
        super(NAME);
    }

    public HashMap<UUID, SpellbookData> getMap() { return data; }

    public static SpellbookManager get() {
        if ( Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER )
            return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage().computeIfAbsent(SpellbookManager::new, NAME);
        else
            return blankClient;
    }

    public Optional<SpellbookData> getSpellbook(UUID uuid) {
        if ( data.containsKey(uuid) )
            return Optional.of(data.get(uuid));
        return Optional.empty();
    }

    public SpellbookData getOrCreateSpellbook(UUID uuid, SpellbookType tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new SpellbookData(id, tier);
        });
    }

    public void removeSpellbook(UUID uuid) {
        getSpellbook(uuid).ifPresent(spellbook -> {
            spellbook.getOptional().invalidate();
            data.remove(uuid);
            setDirty();
        });
    }

    public LazyOptional<IItemHandler> getCapability(UUID uuid) {
        if ( data.containsKey(uuid) )
            return data.get(uuid).getOptional();

        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack) {
        if ( stack.getOrCreateTag().contains("UUID") ) {
            UUID uuid = stack.getTag().getUUID("UUID");
            if ( data.containsKey(uuid) )
                return data.get(uuid).getOptional();
        }

        return LazyOptional.empty();
    }

    @Override
    public void load(CompoundNBT nbt) {
        if ( nbt.contains("Spellbooks") ) {
            ListNBT list = nbt.getList("Spellbooks", Constants.NBT.TAG_COMPOUND);
            list.forEach((spellbookNBT) -> SpellbookData.fromNBT((CompoundNBT) spellbookNBT).ifPresent((spellbook) -> data.put(spellbook.getUuid(), spellbook)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(CompoundNBT compound) {
        ListNBT spellbooks = new ListNBT();
        data.forEach(((uuid, spellbookData) -> spellbooks.add(spellbookData.toNBT())));
        compound.put("Spellbooks", spellbooks);
        return compound;
    }
}
