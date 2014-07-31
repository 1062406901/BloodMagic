package WayofTime.alchemicalWizardry.common.harvest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

public class CactusReedHarvestHandler implements IHarvestHandler
{
	public boolean canHandleBlock(Block block) 
	{
		return block == Blocks.reeds || block == Blocks.cactus;
	}

	@Override
	public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta) 
	{
		if(!this.canHandleBlock(block))
		{
			return false;
		}
		
		if(world.getBlock(xCoord, yCoord-1, zCoord) != block || world.getBlock(xCoord, yCoord-2, zCoord) != block)
		{
			return false;
		}

		world.func_147480_a(xCoord, yCoord, zCoord, true);

		return true;
	}
}