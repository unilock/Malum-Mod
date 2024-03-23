package com.sammy.malum.common.item.curiosities.curios.runes;

import com.sammy.malum.registry.common.MobEffectRegistry;
import com.sammy.malum.registry.common.SpiritTypeRegistry;
import com.sammy.malum.registry.common.item.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.CurioHelper;
import team.lodestar.lodestone.helpers.EntityHelper;

import java.util.function.Consumer;

public class RuneAlimentCleansingItem extends MalumRuneCurioItem {

    public RuneAlimentCleansingItem(Properties builder) {
        super(builder, SpiritTypeRegistry.AQUEOUS_SPIRIT);
    }

    @Override
    public void addExtraTooltipLines(Consumer<Component> consumer) {
        consumer.accept(positiveEffect("shorten_negative_effect"));
    }

    public static void onPotionApplied(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (event.getOldEffectInstance() == null && CurioHelper.hasCurioEquipped(entity, ItemRegistry.RUNE_OF_ALIMENT_CLEANSING.get())) {
            MobEffectInstance effect = event.getEffectInstance();
            MobEffect type = effect.getEffect();
            float multiplier = MobEffectRegistry.ALCHEMICAL_PROFICIENCY_MAP.getOrDefault(ForgeRegistries.MOB_EFFECTS.getKey(type), 1f);
            if (type.getCategory().equals(MobEffectCategory.HARMFUL)) {
                EntityHelper.shortenEffect(effect, entity, (int) (effect.getDuration() * 0.33f * multiplier));
            }
        }
    }
}
