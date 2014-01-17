package WayofTime.alchemicalWizardry.common.items.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class AverageLengtheningCatalyst extends LengtheningCatalyst
{
    public AverageLengtheningCatalyst(int id)
    {
        super(id, 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:AverageLengtheningCatalyst");
    }
}