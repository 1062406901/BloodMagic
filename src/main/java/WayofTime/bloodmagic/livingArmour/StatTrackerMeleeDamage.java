package WayofTime.bloodmagic.livingArmour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;

public class StatTrackerMeleeDamage extends StatTracker
{
    public double totalDamageDealt = 0;

    public static HashMap<LivingArmour, Double> changeMap = new HashMap<LivingArmour, Double>();
    public static int[] damageRequired = new int[] { 200, 800, 1300, 2500, 3800, 5000, 7000, 9200, 11500, 140000 };

    public static void incrementCounter(LivingArmour armour, double damage)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + damage : damage);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.meleeDamage";
    }

    @Override
    public void resetTracker()
    {
        this.totalDamageDealt = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalDamageDealt = tag.getDouble(Constants.Mod.MODID + ".tracker.meleeDamage");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.meleeDamage", totalDamageDealt);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalDamageDealt += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        // TODO Auto-generated method stub
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalDamageDealt >= damageRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeMeleeDamage(i));
            }
        }

        return upgradeList;
    }
}
