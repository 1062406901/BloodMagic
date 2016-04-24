package WayofTime.bloodmagic.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.tile.TilePlinth;
import WayofTime.bloodmagic.util.Utils;

public class BlockPedestal extends BlockStringContainer
{
    public static String[] names = { "pedestal", "plinth" };

    public BlockPedestal()
    {
        super(Material.ROCK, names);

        setUnlocalizedName(Constants.Mod.MODID + ".");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        switch (getMetaFromState(state))
        {
        case 0:
        {
            // TileEntity plinth = world.getTileEntity(pos);
            //
            // if (plinth!= null && plinth instanceof TilePlinth) {
            // Utils.insertItemToTile((TilePlinth) plinth, player);
            // }
        }

        case 1:
        {
            TileEntity plinth = world.getTileEntity(pos);

            if (plinth == null || player.isSneaking())
                return false;

            if (plinth instanceof TilePlinth)
            {
                Utils.insertItemToTile((TilePlinth) plinth, player);
                return true;
            }
        }
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return false;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos pos)
//    {
//        IBlockState state = blockAccess.getBlockState(pos);
//
//        if (getMetaFromState(state) == 0)
//            setBlockBounds(0.5F - 0.3125F, 0.0F, 0.5F - 0.3125F, 0.5F + 0.3125F, 0.6F, 0.5F + 0.3125F);
//        else if (getMetaFromState(state) == 1)
//            setBlockBounds(0.1F, 0.0F, 0.1F, 1.0F - 0.1F, 0.8F, 1.0F - 0.1F);
//
//    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return meta == 0 ? null : new TilePlinth();
    }
}
