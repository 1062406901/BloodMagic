package WayofTime.bloodmagic.block;

import net.minecraft.block.material.Material;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;

public class BlockBloodStoneBrick extends BlockString
{
    public static final String[] names = { "large", "brick" };

    public BlockBloodStoneBrick()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".bloodstonebrick.");
        setRegistryName(Constants.BloodMagicBlock.BLOOD_STONE.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 2);
    }
}
