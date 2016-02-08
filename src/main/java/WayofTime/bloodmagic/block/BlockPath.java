package WayofTime.bloodmagic.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.incense.IIncensePath;
import WayofTime.bloodmagic.block.base.BlockString;

public class BlockPath extends BlockString implements IIncensePath
{
    public static final String[] names = { "wood", "woodTile", "stone", "stoneTile", "wornstone", "wornstoneTile", "obsidian", "obsidianTile" };

    public BlockPath()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".path.");
        setRegistryName(Constants.BloodMagicBlock.PATH.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);

        setHarvestLevel("axe", 0, getStateFromMeta(0));
        setHarvestLevel("axe", 0, getStateFromMeta(1));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(2));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(3));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(4));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(5));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(6));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(7));
    }

    @Override
    public int getLevelOfPath(World world, BlockPos pos, IBlockState state)
    {
        switch (this.getMetaFromState(state))
        {
        case 0:
            return 2;
        case 2:
            return 4;
        case 4:
            return 6;
        default:
            return 0;
        }
    }
}
