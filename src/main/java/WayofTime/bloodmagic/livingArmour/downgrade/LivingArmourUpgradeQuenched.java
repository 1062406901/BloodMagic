package WayofTime.bloodmagic.livingArmour.downgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeQuenched extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -100 };

    public LivingArmourUpgradeQuenched(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {

    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.quenched";
    }

    @Override
    public int getMaxTier()
    {
        return 1;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "quenched";
    }
}