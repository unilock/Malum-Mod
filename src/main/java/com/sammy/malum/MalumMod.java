package com.sammy.malum;

import com.sammy.malum.compability.farmersdelight.*;
import com.sammy.malum.config.*;
import com.sammy.malum.registry.common.item.tabs.*;
import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ConfigType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import org.apache.logging.log4j.*;

import static com.sammy.malum.registry.client.ParticleRegistry.*;
import static com.sammy.malum.registry.common.AttributeRegistry.*;
import static com.sammy.malum.registry.common.ContainerRegistry.*;
import static com.sammy.malum.registry.common.MobEffectRegistry.*;
import static com.sammy.malum.registry.common.SoundRegistry.*;
import static com.sammy.malum.registry.common.block.BlockEntityRegistry.*;
import static com.sammy.malum.registry.common.block.BlockRegistry.*;
import static com.sammy.malum.registry.common.entity.EntityRegistry.*;
import static com.sammy.malum.registry.common.item.EnchantmentRegistry.*;
import static com.sammy.malum.registry.common.item.ItemRegistry.*;
import static com.sammy.malum.registry.common.item.tabs.CreativeTabRegistry.*;
import static com.sammy.malum.registry.common.recipe.RecipeSerializerRegistry.*;
import static com.sammy.malum.registry.common.recipe.RecipeTypeRegistry.*;
import static com.sammy.malum.registry.common.worldgen.FeatureRegistry.*;
import static com.sammy.malum.registry.common.worldgen.StructureRegistry.*;

@SuppressWarnings("unused")
public class MalumMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MALUM = "malum";
    public static final RandomSource RANDOM = RandomSource.create();

    public static ResourceLocation malumPath(String path) {
        return new ResourceLocation(MALUM, path);
    }


    @Override
    public void onInitialize() {

        ConfigRegistry.registerConfig(MalumMod.MALUM, ConfigType.COMMON, CommonConfig.SPEC);

        ENCHANTMENTS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        ITEMS.register();
        ENTITY_TYPES.register();
        EFFECTS.register();
        PARTICLES.register();
        SOUNDS.register();
        CONTAINERS.register();
        ATTRIBUTES.register();
        RECIPE_TYPES.register();
        RECIPE_SERIALIZERS.register();
        FEATURE_TYPES.register();
        STRUCTURES.register();
        CREATIVE_MODE_TABS.register();

        FarmersDelightCompat.init();


        modBus.addListener(CreativeTabRegistry::populateItemGroups);
    }
}