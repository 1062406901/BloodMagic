package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.common.omega.IEnchantmentGlyph;

public class BlockEnchantmentGlyph extends Block implements IEnchantmentGlyph
{
    public BlockEnchantmentGlyph()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

	@Override
	public int getAdditionalStabilityForFaceCount(World world, BlockPos pos, int meta, int faceCount) 
	{
		switch(meta)
		{
		case 0:
			return -faceCount * 10;
		case 1:
			return -faceCount * 20;
		default: 
			return -faceCount * 20;
		}
	}

	@Override
	public int getEnchantability(World world, BlockPos pos, int meta) 
	{
		switch(meta)
		{
		case 0:
			return 1;
		default:
			return 0;	
		}
	}

	@Override
	public int getEnchantmentLevel(World world, BlockPos pos, int meta) 
	{
		switch(meta)
		{
		case 1:
			return 1;
		default:
			return 0;	
		}
	}	

    @Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i=0; i<2; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return blockState.getBlock().damageDropped(blockState);
    }
}
