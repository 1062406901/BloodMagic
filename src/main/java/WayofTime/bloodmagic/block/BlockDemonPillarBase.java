package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockEnumPillar;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonPillarBase<E extends Enum<E> & IStringSerializable> extends BlockEnumPillar<E> implements IVariantProvider
{
    public BlockDemonPillarBase(String baseName, Material materialIn, Class<E> enumClass)
    {
        super(materialIn, enumClass);

        setUnlocalizedName(Constants.Mod.MODID + "." + baseName + ".");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();

        //This is done to make the ItemBlocks have the proper model
        EnumFacing.Axis[] axis = new EnumFacing.Axis[] { EnumFacing.Axis.Y, EnumFacing.Axis.X, EnumFacing.Axis.Z };

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < this.getTypes().length; j++)
            {
                ret.add(new ImmutablePair<Integer, String>(i * 5 + j, "axis=" + axis[i] + ",type=" + this.getTypes()[j]));
            }
        }

        return ret;
    }
}