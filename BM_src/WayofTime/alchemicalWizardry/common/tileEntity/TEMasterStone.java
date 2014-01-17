package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.rituals.Rituals;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TEMasterStone extends TileEntity
{
    private int currentRitual;
    private boolean isActive;
    private String owner;
    private int cooldown;
    private int var1;
    private int direction;

    public TEMasterStone()
    {
        currentRitual = 0;
        isActive = false;
        owner = "";
        cooldown = 0;
        var1 = 0;
        direction = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        currentRitual = par1NBTTagCompound.getInteger("currentRitual");
        isActive = par1NBTTagCompound.getBoolean("isActive");
        owner = par1NBTTagCompound.getString("owner");
        cooldown = par1NBTTagCompound.getInteger("cooldown");
        var1 = par1NBTTagCompound.getInteger("var1");
        direction = par1NBTTagCompound.getInteger("direction");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("currentRitual", currentRitual);
        par1NBTTagCompound.setBoolean("isActive", isActive);
        par1NBTTagCompound.setString("owner", owner);
        par1NBTTagCompound.setInteger("cooldown", cooldown);
        par1NBTTagCompound.setInteger("var1", var1);
        par1NBTTagCompound.setInteger("direction", direction);
    }

    public void activateRitual(World world, int crystalLevel)
    {
        int testRitual = Rituals.checkValidRitual(world, xCoord, yCoord, zCoord);

        if (testRitual == 0)
        {
            return;
        }

        boolean testLevel = Rituals.canCrystalActivate(testRitual, crystalLevel);

        if (!testLevel)
        {
            return;
        }

        if (world.isRemote)
        {
            return;
        }

        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;

        if (currentEssence < Rituals.getCostForActivation(testRitual))
        {
            //TODO Bad stuff
            return;
        }

        if (!world.isRemote)
        {
            data.currentEssence = currentEssence - Rituals.getCostForActivation(testRitual);
            data.markDirty();

            for (int i = 0; i < 12; i++)
            {
                PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short) 1));
            }
        }

        cooldown = Rituals.getInitialCooldown(testRitual);
        var1 = 0;
        currentRitual = testRitual;
        isActive = true;
        direction = Rituals.getDirectionOfRitual(world, xCoord, yCoord, zCoord, testRitual);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    @Override
    public void updateEntity()
    {
        if (!isActive)
        {
            return;
        }

        int worldTime = (int) (worldObj.getWorldTime() % 24000);

        if (worldObj.isRemote)
        {
            return;
        }

        if (worldTime % 100 == 0)
        {
            boolean testRunes = Rituals.checkDirectionOfRitualValid(worldObj, xCoord, yCoord, zCoord, currentRitual, direction);
            PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short) 1));

            if (!testRunes)
            {
                isActive = false;
                currentRitual = 0;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                //PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(xCoord, yCoord, zCoord, (short)3));
                return;
            }
        }

        if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0)
        {
            return;
        }

        performRitual(worldObj, xCoord, yCoord, zCoord, currentRitual);
    }

    public void performRitual(World world, int x, int y, int z, int ritualID)
    {
        Rituals.performEffect(this, ritualID);
    }

    public String getOwner()
    {
        return owner;
    }

    public void setCooldown(int newCooldown)
    {
        this.cooldown = newCooldown;
    }

    public int getCooldown()
    {
        return this.cooldown;
    }

    public void setVar1(int newVar1)
    {
        this.var1 = newVar1;
    }

    public int getVar1()
    {
        return this.var1;
    }

    public void setActive(boolean active)
    {
        this.isActive = active;
    }

    public int getDirection()
    {
        return this.direction;
    }
}