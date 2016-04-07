package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeSprintAttack extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 3, 7, 15, 35, 49 };
    public static final double[] damageBoost = new double[] { 0.5, 1, 1.5, 2, 2.5 };
    public static final double[] knockbackModifier = new double[] { 1, 2, 3, 4, 5 };

    public LivingArmourUpgradeSprintAttack(int level)
    {
        super(level);
    }

    @Override
    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon)
    {
        if (wearer.isSprinting())
        {
            return getDamageModifier();
        }

        return 0;
    }

    @Override
    public double getKnockbackOnHit(EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon)
    {
        if (wearer.isSprinting())
        {
            return getKnockbackModifier();
        }

        return 0;
    }

    public double getDamageModifier()
    {
        return damageBoost[this.level];
    }

    public double getKnockbackModifier()
    {
        return knockbackModifier[this.level];
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.sprintAttack";
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
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "sprintAttack";
    }
}