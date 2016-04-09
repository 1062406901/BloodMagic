package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.tile.TileTeleposer;

public class BlockTeleposer extends BlockContainer implements IVariantProvider
{
    public BlockTeleposer()
    {
        super(Material.rock);

        setCreativeTab(BloodMagic.tabBloodMagic);
        setUnlocalizedName(Constants.Mod.MODID + ".teleposer");
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack playerItem = heldItem;

        if (playerItem != null && playerItem.getItem() instanceof ItemTelepositionFocus)
            ((ItemTelepositionFocus) playerItem.getItem()).setBlockPos(playerItem, world, pos);
        else if (world.getTileEntity(pos) instanceof TileTeleposer)
            player.openGui(BloodMagic.instance, Constants.Gui.TELEPOSER_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileTeleposer tileTeleposer = (TileTeleposer) world.getTileEntity(blockPos);
        if (tileTeleposer != null)
            tileTeleposer.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileTeleposer();
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "normal"));
        return ret;
    }
}
