package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class BlockRitualStone extends BlockEnum<EnumRuneType> implements IRitualStone
{
    public BlockRitualStone()
    {
        super(Material.IRON, EnumRuneType.class);

        setUnlocalizedName(BloodMagic.MODID + ".ritualStone.");
        setCreativeTab(BloodMagic.TAB_BM);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return false;
    }

    @Override
    public boolean isRuneType(World world, BlockPos pos, EnumRuneType runeType)
    {
        return runeType == this.getTypes()[getMetaFromState(world.getBlockState(pos))];
    }

    @Override
    public void setRuneType(World world, BlockPos pos, EnumRuneType runeType)
    {
        int meta = runeType.ordinal();
        IBlockState newState = RegistrarBloodMagicBlocks.RITUAL_STONE.getStateFromMeta(meta);
        world.setBlockState(pos, newState);
    }
}
