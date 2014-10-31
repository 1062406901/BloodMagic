package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.alchemy.energy.*;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TEMasterStone extends TileEntity implements IMasterRitualStone
{
    private String currentRitualString;
    private boolean isActive;
    private String owner;
    private String varString1;
    private int cooldown;
    private int var1;
    private int direction;
    public boolean isRunning;
    public int runningTime;

    private NBTTagCompound customRitualTag;

    protected ReagentContainer[] tanks;
    protected Map<Reagent, Integer> attunedTankMap;

    public TEMasterStone()
    {
        tanks = new ReagentContainer[]{new ReagentContainer(1000), new ReagentContainer(1000), new ReagentContainer(1000)};
        this.attunedTankMap = new HashMap();

        isActive = false;
        owner = "";
        cooldown = 0;
        var1 = 0;
        direction = 0;
        varString1 = "";
        currentRitualString = "";
        isRunning = false;
        runningTime = 0;

        this.customRitualTag = new NBTTagCompound();
    }

    public void readClientNBT(NBTTagCompound tag)
    {
        currentRitualString = tag.getString("currentRitualString");
        isRunning = tag.getBoolean("isRunning");
        runningTime = tag.getInteger("runningTime");

        NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = tagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }
    }

    public void writeClientNBT(NBTTagCompound tag)
    {
        tag.setString("currentRitualString", currentRitualString);
        tag.setBoolean("isRunning", isRunning);
        tag.setInteger("runningTime", runningTime);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.tanks.length; i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (this.tanks[i] != null)
            {
                this.tanks[i].writeToNBT(savedTag);
            }
            tagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        isActive = tag.getBoolean("isActive");
        owner = tag.getString("owner");
        cooldown = tag.getInteger("cooldown");
        var1 = tag.getInteger("var1");
        direction = tag.getInteger("direction");
        currentRitualString = tag.getString("currentRitualString");
        isRunning = tag.getBoolean("isRunning");
        runningTime = tag.getInteger("runningTime");

        NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = tagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }

        NBTTagList attunedTagList = tag.getTagList("attunedTankMap", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < attunedTagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = attunedTagList.getCompoundTagAt(i);
            Reagent reagent = ReagentRegistry.getReagentForKey(savedTag.getString("reagent"));
            this.attunedTankMap.put(reagent, savedTag.getInteger("amount"));
        }

        customRitualTag = tag.getCompoundTag("customRitualTag");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("isActive", isActive);
        tag.setString("owner", owner);
        tag.setInteger("cooldown", cooldown);
        tag.setInteger("var1", var1);
        tag.setInteger("direction", direction);
        tag.setString("currentRitualString", currentRitualString);
        tag.setBoolean("isRunning", isRunning);
        tag.setInteger("runningTime", runningTime);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.tanks.length; i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (this.tanks[i] != null)
            {
                this.tanks[i].writeToNBT(savedTag);
            }
            tagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", tagList);

        NBTTagList attunedTagList = new NBTTagList();

        for (Entry<Reagent, Integer> entry : this.attunedTankMap.entrySet())
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            savedTag.setString("reagent", ReagentRegistry.getKeyForReagent(entry.getKey()));
            savedTag.setInteger("amount", entry.getValue());
            attunedTagList.appendTag(savedTag);
        }

        tag.setTag("attunedTankMap", attunedTagList);

        tag.setTag("customRitualTag", customRitualTag);
    }

    public void activateRitual(World world, int crystalLevel, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return;
        }

        String testRitual = Rituals.checkValidRitual(world, xCoord, yCoord, zCoord);

        if (testRitual.equals(""))
        {
            player.addChatMessage(new ChatComponentText("Nothing appears to have happened..."));
            return;
        }

        boolean testLevel = Rituals.canCrystalActivate(testRitual, crystalLevel);

        if (!testLevel)
        {
            player.addChatMessage(new ChatComponentText("Your crystal vibrates pathetically."));

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
            player.addChatMessage(new ChatComponentText("You feel a pull, but you are too weak to push any further."));

            return;
        }

        if (!world.isRemote)
        {
            if (!Rituals.startRitual(this, testRitual, player))
            {
                player.addChatMessage(new ChatComponentText("The ritual appears to actively resist you!"));

                return;
            } else
            {
                data.currentEssence = currentEssence - Rituals.getCostForActivation(testRitual);
                data.markDirty();

                player.addChatMessage(new ChatComponentText("A rush of energy flows through the ritual!"));

                for (int i = 0; i < 12; i++)
                {
                    SpellHelper.sendIndexedParticleToAllAround(world, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);
                }
            }
        }

        cooldown = Rituals.getInitialCooldown(testRitual);
        var1 = 0;
        currentRitualString = testRitual;
        isActive = true;
        isRunning = true;
        direction = Rituals.getDirectionOfRitual(world, xCoord, yCoord, zCoord, testRitual);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public void useOnRitualBroken()
    {
        Rituals.onRitualBroken(this, this.currentRitualString);
    }

    @Override
    public void updateEntity()
    {
        if (isRunning && runningTime < 100)
        {
            runningTime++;
        } else if (!isRunning && runningTime > 0)
        {
            runningTime--;
        }

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
            boolean testRunes = Rituals.checkDirectionOfRitualValid(worldObj, xCoord, yCoord, zCoord, currentRitualString, direction);
            SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);

            if (!testRunes)
            {
                Rituals.onRitualBroken(this, currentRitualString);
                isActive = false;
                currentRitualString = "";
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return;
            }
        }

        if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0)
        {
            if (isRunning)
            {
                isRunning = false;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return;
        } else
        {
            if (!isRunning)
            {
                isRunning = true;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }

        performRitual(worldObj, xCoord, yCoord, zCoord, currentRitualString);
    }

    public void performRitual(World world, int x, int y, int z, String currentRitualString2)
    {
        Rituals.performEffect(this, currentRitualString2);
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
        this.isRunning = active;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getDirection()
    {
        return this.direction;
    }

    @Override
    public World getWorld()
    {
        return this.getWorldObj();
    }

    @Override
    public int getXCoord()
    {
        return xCoord;
    }

    @Override
    public int getYCoord()
    {
        return yCoord;
    }

    @Override
    public int getZCoord()
    {
        return zCoord;
    }

    public String getCurrentRitual()
    {
        return this.currentRitualString;
    }

    public void setCurrentRitual(String str)
    {
        this.currentRitualString = str;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeClientNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readClientNBT(packet.func_148857_g());
    }

    public AxisAlignedBB getRenderBoundingBox()
    {
        double renderExtention = 1.0d;
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - renderExtention, yCoord - renderExtention, zCoord - renderExtention, xCoord + 1 + renderExtention, yCoord + 1 + renderExtention, zCoord + 1 + renderExtention);
        return bb;
    }

    /* ISegmentedReagentHandler */
    @Override
    public int fill(ForgeDirection from, ReagentStack resource, boolean doFill)
    {
        if (doFill)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        int totalFill = 0;

        boolean useTankLimit = !this.attunedTankMap.isEmpty();

        if (resource != null)
        {
            int totalTanksFillable = useTankLimit ? this.getTanksTunedToReagent(resource.reagent) : this.tanks.length;
            int tanksFilled = 0;

            int maxFill = resource.amount;

            for (int i = this.tanks.length - 1; i >= 0; i--)
            {
                ReagentStack remainingStack = resource.copy();
                remainingStack.amount = maxFill - totalFill;

                boolean doesReagentMatch = tanks[i].getReagent() == null ? false : tanks[i].getReagent().isReagentEqual(remainingStack);

                if (doesReagentMatch)
                {
                    totalFill += tanks[i].fill(remainingStack, doFill);
                    tanksFilled++;
                } else
                {
                    continue;
                }

                if (totalFill >= maxFill || tanksFilled >= totalTanksFillable)
                {
                    return totalFill;
                }
            }

            if (tanksFilled >= totalTanksFillable)
            {
                return totalFill;
            }

            for (int i = this.tanks.length - 1; i >= 0; i--)
            {
                ReagentStack remainingStack = resource.copy();
                remainingStack.amount = maxFill - totalFill;

                boolean isTankEmpty = tanks[i].getReagent() == null;

                if (isTankEmpty)
                {
                    totalFill += tanks[i].fill(remainingStack, doFill);
                    tanksFilled++;
                } else
                {
                    continue;
                }

                if (totalFill >= maxFill || tanksFilled >= totalTanksFillable)
                {
                    return totalFill;
                }
            }
        }
        return totalFill;
    }

    @Override
    public ReagentStack drain(ForgeDirection from, ReagentStack resource, boolean doDrain)
    {
        if (resource == null)
        {
            return null;
        }

        if (doDrain)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        int maxDrain = resource.amount;
        Reagent reagent = resource.reagent;
        int drained = 0;

        for (int i = 0; i < tanks.length; i++)
        {
            if (drained >= maxDrain)
            {
                break;
            }

            if (resource.isReagentEqual(tanks[i].getReagent()))
            {
                ReagentStack drainStack = tanks[i].drain(maxDrain - drained, doDrain);
                if (drainStack != null)
                {
                    drained += drainStack.amount;
                }
            }
        }

        return new ReagentStack(reagent, drained);
    }

    /* Only returns the amount from the first available tank */
    @Override
    public ReagentStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        for (int i = 0; i < tanks.length; i++)
        {
            ReagentStack stack = tanks[i].drain(maxDrain, doDrain);
            if (stack != null)
            {
                if (doDrain)
                {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }

                return stack;
            }
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Reagent reagent)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Reagent reagent)
    {
        return true;
    }

    @Override
    public ReagentContainerInfo[] getContainerInfo(ForgeDirection from)
    {
        ReagentContainerInfo[] info = new ReagentContainerInfo[this.getNumberOfTanks()];
        for (int i = 0; i < this.getNumberOfTanks(); i++)
        {
            info[i] = tanks[i].getInfo();
        }
        return info;
    }

    @Override
    public int getNumberOfTanks()
    {
        return tanks.length;
    }

    @Override
    public int getTanksTunedToReagent(Reagent reagent)
    {
        if (this.attunedTankMap.containsKey(reagent) && this.attunedTankMap.get(reagent) != null)
        {
            return this.attunedTankMap.get(reagent);
        }
        return 0;
    }

    @Override
    public void setTanksTunedToReagent(Reagent reagent, int total)
    {
        if (total == 0 && this.attunedTankMap.containsKey(reagent))
        {
            this.attunedTankMap.remove(reagent);
            return;
        }

        this.attunedTankMap.put(reagent, new Integer(total));
    }

    @Override
    public Map<Reagent, Integer> getAttunedTankMap()
    {
        return this.attunedTankMap;
    }

    public boolean areTanksEmpty()
    {
        for (int i = 0; i < this.tanks.length; i++)
        {
            if (tanks[i] != null && tanks[i].getReagent() != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public NBTTagCompound getCustomRitualTag()
    {
        return this.customRitualTag;
    }

    @Override
    public void setCustomRitualTag(NBTTagCompound tag)
    {
        this.customRitualTag = tag;
    }
}