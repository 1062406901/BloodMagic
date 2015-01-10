package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaWater;

public class OmegaArmourWater extends OmegaArmour
{
	public OmegaArmourWater(int armorType) 
	{
		super(armorType);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaWater.png";
    }
	
	@Override
	public ModelBiped getChestModel()
	{
		return new ModelOmegaWater(1.0f, true, true, false, true);
	}
	
	@Override
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaWater(0.5f, false, false, true, false);
	}
}
