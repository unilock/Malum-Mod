package com.sammy.malum.core.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sammy.malum.MalumHelper;
import com.sammy.malum.core.registry.content.RecipeSerializerRegistry;
import com.sammy.malum.core.systems.recipe.IngredientWithCount;
import com.sammy.malum.core.systems.recipe.ItemWithCount;
import com.sammy.malum.core.systems.spirit.MalumSpiritType;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SpiritInfusionRecipeBuilder
{
    private boolean retainPrimeItem;
    private final IngredientWithCount input;

    private final ItemWithCount output;

    private final List<ItemWithCount> spirits = Lists.newArrayList();
    private final List<IngredientWithCount> extraItems = Lists.newArrayList();

    public SpiritInfusionRecipeBuilder(Ingredient input, int inputCount, Item output, int outputCount)
    {
        this.input = new IngredientWithCount(input, inputCount);
        this.output = new ItemWithCount(output, outputCount);
    }
    public SpiritInfusionRecipeBuilder(Item input, int inputCount, Item output, int outputCount)
    {
        this.input = new IngredientWithCount(Ingredient.of(input), inputCount);
        this.output = new ItemWithCount(output, outputCount);
    }
    public SpiritInfusionRecipeBuilder addExtraItem(Ingredient ingredient, int count)
    {
        extraItems.add(new IngredientWithCount(ingredient, count));
        return this;
    }
    public SpiritInfusionRecipeBuilder addExtraItem(Item input, int count)
    {
        extraItems.add(new IngredientWithCount(Ingredient.of(input), count));
        return this;
    }
    public SpiritInfusionRecipeBuilder addSpirit(MalumSpiritType type, int count)
    {
        spirits.add(new ItemWithCount(type.splinterItem(), count));
        return this;
    }
    public SpiritInfusionRecipeBuilder retainsPrimeItem()
    {
        retainPrimeItem = true;
        return this;
    }
    public void build(Consumer<FinishedRecipe> consumerIn, String recipeName)
    {
        build(consumerIn, MalumHelper.prefix("spirit_infusion/" + recipeName));
    }
    public void build(Consumer<FinishedRecipe> consumerIn)
    {
        build(consumerIn, output.item.getRegistryName().getPath());
    }
    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id)
    {
        consumerIn.accept(new SpiritInfusionRecipeBuilder.Result(id, retainPrimeItem, input, output, spirits, extraItems));
    }

    public static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;

        private final boolean retainPrimeItem;
        private final IngredientWithCount input;

        private final ItemWithCount output;

        private final List<ItemWithCount> spirits;
        private final List<IngredientWithCount> extraItems;


        public Result(ResourceLocation id, boolean retainPrimeItem, IngredientWithCount input, ItemWithCount output, List<ItemWithCount> spirits, List<IngredientWithCount> extraItems)
        {
            this.id = id;
            this.retainPrimeItem = retainPrimeItem;
            this.input = input;
            this.output = output;
            this.spirits = spirits;
            this.extraItems = extraItems;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject inputObject = input.serialize();

            JsonObject outputObject = output.serialize();
            JsonArray extraItems = new JsonArray();
            for (IngredientWithCount extraItem : this.extraItems) {
                extraItems.add(extraItem.serialize());
            }
            JsonArray spirits = new JsonArray();
            for (ItemWithCount spirit : this.spirits) {
                spirits.add(spirit.serialize());
            }
            json.addProperty("retain_prime_item", retainPrimeItem);

            json.add("input", inputObject);
            json.add("output", outputObject);
            json.add("extra_items", extraItems);
            json.add("spirits", spirits);
        }

        @Override
        public ResourceLocation getId()
        {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return RecipeSerializerRegistry.INFUSION_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement()
        {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId()
        {
            return null;
        }
    }
}