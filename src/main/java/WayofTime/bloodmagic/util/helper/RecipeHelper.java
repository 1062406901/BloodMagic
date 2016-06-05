package WayofTime.bloodmagic.util.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeHelper
{
    public static IRecipe getRecipeForOutput(ItemStack stack)
    {
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList())
        {
            if (recipe != null)
            {
                ItemStack resultStack = recipe.getRecipeOutput();
                if (resultStack != null && resultStack.getItem() != null)
                {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage())
                    {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }
}
