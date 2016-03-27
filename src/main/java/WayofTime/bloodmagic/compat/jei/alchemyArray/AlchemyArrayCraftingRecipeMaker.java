package WayofTime.bloodmagic.compat.jei.alchemyArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;

import com.google.common.collect.BiMap;

public class AlchemyArrayCraftingRecipeMaker
{
    @Nonnull
    public static List<AlchemyArrayCraftingRecipeJEI> getRecipes()
    {
        Map<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry.getRecipes();

        ArrayList<AlchemyArrayCraftingRecipeJEI> recipes = new ArrayList<AlchemyArrayCraftingRecipeJEI>();

        for (Map.Entry<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap.entrySet())
        {
            List<ItemStack> input = itemStackAlchemyArrayRecipeEntry.getValue().getInput();
            BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry.getValue().catalystMap;

            for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet())
            {
                ItemStack catalyst = entry.getKey().toStack();
                if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input, catalyst) instanceof AlchemyArrayEffectCrafting)
                {
                    ItemStack output = ((AlchemyArrayEffectCrafting) itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst)).getOutputStack();

                    AlchemyArrayCraftingRecipeJEI recipe = new AlchemyArrayCraftingRecipeJEI(input, catalyst, output);
                    recipes.add(recipe);
                }
            }
        }

        return recipes;
    }
}
