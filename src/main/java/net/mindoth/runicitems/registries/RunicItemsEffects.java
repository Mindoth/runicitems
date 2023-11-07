package net.mindoth.runicitems.registries;

import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.effect.SummonTimer;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RunicItemsEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registry.MOB_EFFECT_REGISTRY, RunicItems.MOD_ID);

    public static final RegistryObject<SummonTimer> BLAZE_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("blaze_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> SKELETON_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("skeleton_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
}
