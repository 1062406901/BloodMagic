package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileInversionPillar;

public class BlockInversionPillar extends BlockStringContainer implements IVariantProvider
{
    public static final String[] names = { "raw", "corrosive", "destructive", "vengeful", "steadfast" };

    public BlockInversionPillar()
    {
        super(Material.ROCK, names);

        setUnlocalizedName(Constants.Mod.MODID + ".inversionpillar.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileInversionPillar)
        {
            TileInversionPillar tilePillar = (TileInversionPillar) world.getTileEntity(blockPos);
            tilePillar.removePillarFromMap();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

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
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + names[i]));
        return ret;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileInversionPillar(EnumDemonWillType.values()[meta % 5]);
    }

    @Override
    protected BlockStateContainer createRealBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { stringProp }, new IUnlistedProperty[] { unlistedStringProp, Properties.AnimationProperty });
    }
}
