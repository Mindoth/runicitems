package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.effect.GhostWalkEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicItemsEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, RunicItems.MOD_ID);

    public static final RegistryObject<GhostWalkEffect> GHOST_WALK = EFFECTS.register("ghost_walk", () -> new GhostWalkEffect(EffectType.BENEFICIAL, 3124687));
}
