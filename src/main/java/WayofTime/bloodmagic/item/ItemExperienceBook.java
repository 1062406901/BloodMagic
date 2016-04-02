package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemExperienceBook extends Item implements IVariantProvider
{
    public ItemExperienceBook()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".experienceTome");
        setRegistryName(Constants.BloodMagicItem.EXPERIENCE_TOME.getRegName());
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.experienceTome"));

        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.experienceTome.exp", getStoredExperience(stack)));
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {

        return EnumActionResult.FAIL;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=experiencetome"));
        return ret;
    }

    public void absorbOneLevelExpFromPlayer(ItemStack stack, EntityPlayer player)
    {
        float progress = player.experience;
        if (progress > 0)
        {
            double expDeduction = getExperienceAcquiredToNext(player.experienceLevel, player.experience);
            player.experience = 0;
            player.experienceTotal -= (int) (expDeduction);

            addExperience(stack, expDeduction);
        } else
        {
            player.experienceLevel--;
            int expDeduction = getExperienceForNextLevel(player.experienceLevel - 1);
            player.experienceTotal -= expDeduction;

            addExperience(stack, expDeduction);
        }
    }

    public static void setStoredExperience(ItemStack stack, double exp)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble("experience", exp);
    }

    public static double getStoredExperience(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble("experience");
    }

    public static void addExperience(ItemStack stack, double exp)
    {
        setStoredExperience(stack, getStoredExperience(stack) + exp);
    }

    public static int getExperienceForNextLevel(int currentLevel)
    {
        if (currentLevel <= 16)
        {
            return 2 * currentLevel + 7;
        } else if (currentLevel <= 31)
        {
            return 5 * currentLevel - 38;
        } else
        {
            return 9 * currentLevel - 158;
        }
    }

    public static double getExperienceAcquiredToNext(int currentLevel, double progress)
    {
        return progress * getExperienceForNextLevel(currentLevel);
    }
}
