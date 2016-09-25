package WayofTime.bloodmagic.livingArmour.downgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeBattleHungry extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -10, -20, -35, -50, -75 };
    public static final float[] exhaustionAdded = new float[] { 0.02f, 0.04f, 0.06f, 0.08f, 0.1f };
    public static final int[] delay = new int[] { 600, 600, 600, 500, 400 };

    public int timer = 0;

    public LivingArmourUpgradeBattleHungry(int level)
    {
        super(level);
    }

    public void resetTimer()
    {
        this.timer = delay[this.level];
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        if (timer > 0)
        {
            timer--;
            return;
        }

        if (player.ticksExisted % 20 == 0)
        {
            player.addExhaustion(exhaustionAdded[this.level]); //TODO: Change exhaustion added per level.
        }
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.battleHunger";
    }

    @Override
    public int getMaxTier()
    {
        return 5;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("timer", timer);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        timer = tag.getInteger("timer");
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "battleHunger";
    }
}