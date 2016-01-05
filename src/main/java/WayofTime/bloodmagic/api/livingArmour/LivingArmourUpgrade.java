package WayofTime.bloodmagic.api.livingArmour;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class LivingArmourUpgrade
{
    public static String chatBase = "chat.BloodMagic.livingArmour.upgrade.";
    public static String tooltipBase = "tooltip.BloodMagic.livingArmour.upgrade.";

    /**
     * Upgrade level 0 is the first upgrade. Upgrade goes from 0 to getMaxTier()
     * - 1.
     */
    protected int level = 0;

    /**
     * The LivingArmourUpgrade must have a constructor that has a single integer
     * parameter. Upgrades may have other constructors, but must have one of
     * these.
     * 
     * @param level
     *        The level of the upgrade
     */
    public LivingArmourUpgrade(int level)
    {
        this.level = Math.min(level, getMaxTier() - 1);
    }

    public int getUpgradeLevel()
    {
        return this.level;
    }

    public abstract String getUniqueIdentifier();

    public abstract String getUnlocalizedName();

    /**
     * @return
     */
    public abstract int getMaxTier();

    public abstract int getCostOfUpgrade();

    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        return HashMultimap.<String, AttributeModifier>create();
    }

    public abstract void writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);
}
