package WayofTime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

import com.google.common.base.Strings;

@Getter
public class TileAlchemyTable extends TileInventory implements ISidedInventory, ITickable
{
    public static final int orbSlot = 6;
    public static final int toolSlot = 7;
    public static final int outputSlot = 8;

    public EnumFacing direction = EnumFacing.NORTH;
    public boolean isSlave = false;
    public int burnTime = 0;
    public int ticksRequired = 1;

    public BlockPos connectedPos = BlockPos.ORIGIN;
    public List<Integer> blockedSlots = new ArrayList<Integer>();

    public TileAlchemyTable()
    {
        super(9, "alchemyTable");
    }

    public void setInitialTableParameters(EnumFacing direction, boolean isSlave, BlockPos connectedPos)
    {
        this.isSlave = isSlave;
        this.connectedPos = connectedPos;

        if (!isSlave)
        {
            this.direction = direction;
        }
    }

    public boolean isInvisible()
    {
        return isSlave();
    }

    public boolean isInputSlotAccessible(int slot)
    {
        if (slot < 6 && slot >= 0)
        {
            return !blockedSlots.contains(slot);
        }

        return true;
    }

    public void toggleInputSlotAccessible(int slot)
    {
        if (slot < 6 && slot >= 0)
        {
            if (blockedSlots.contains(slot))
            {
                blockedSlots.remove((Object) slot);
            } else
            {
                blockedSlots.add(slot);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        isSlave = tag.getBoolean("isSlave");
        direction = EnumFacing.getFront(tag.getInteger(Constants.NBT.DIRECTION));
        connectedPos = new BlockPos(tag.getInteger(Constants.NBT.X_COORD), tag.getInteger(Constants.NBT.Y_COORD), tag.getInteger(Constants.NBT.Z_COORD));

        burnTime = tag.getInteger("burnTime");
        ticksRequired = tag.getInteger("ticksRequired");

        blockedSlots.clear();
        int[] blockedSlotArray = tag.getIntArray("blockedSlots");
        for (int blocked : blockedSlotArray)
        {
            blockedSlots.add(blocked);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("isSlave", isSlave);
        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
        tag.setInteger(Constants.NBT.X_COORD, connectedPos.getX());
        tag.setInteger(Constants.NBT.Y_COORD, connectedPos.getY());
        tag.setInteger(Constants.NBT.Z_COORD, connectedPos.getZ());

        tag.setInteger("burnTime", burnTime);
        tag.setInteger("ticksRequired", ticksRequired);

        int[] blockedSlotArray = new int[blockedSlots.size()];
        for (int i = 0; i < blockedSlots.size(); i++)
        {
            blockedSlotArray[i] = blockedSlots.get(i);
        }

        tag.setIntArray("blockedSlots", blockedSlotArray);
        return tag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (this.isSlave())
            {
                TileEntity tile = worldObj.getTileEntity(connectedPos);
                if (tile instanceof TileAlchemyTable)
                {
                    return (T) new SidedInvWrapper((TileAlchemyTable) tile, facing);
                }
            } else
            {
                return (T) new SidedInvWrapper(this, facing);
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        switch (side)
        {
        case DOWN:
            return new int[] { outputSlot };
        case UP:
            return new int[] { orbSlot, toolSlot };
        default:
            return new int[] { 0, 1, 2, 3, 4, 5 };
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
    {
        switch (direction)
        {
        case DOWN:
            return index != outputSlot && index != orbSlot && index != toolSlot;
        case UP:
            if (index == orbSlot && stack != null && stack.getItem() instanceof IBloodOrb)
            {
                return true;
            } else if (index == toolSlot)
            {
                return false; //TODO:
            } else
            {
                return true;
            }
        default:
            return getAccessibleInputSlots(direction).contains(index);
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        switch (direction)
        {
        case DOWN:
            return index == outputSlot;
        case UP:
            if (index == orbSlot && stack != null && stack.getItem() instanceof IBloodOrb)
            {
                return true;
            } else if (index == toolSlot)
            {
                return true; //TODO:
            } else
            {
                return true;
            }
        default:
            return getAccessibleInputSlots(direction).contains(index);
        }
    }

    public List<Integer> getAccessibleInputSlots(EnumFacing direction)
    {
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < 6; i++)
        {
            if (isInputSlotAccessible(i))
            {
                list.add(i);
            }
        }

        return list;
    }

    @Override
    public void update()
    {
        if (isSlave())
        {
            return;
        }

        List<ItemStack> inputList = new ArrayList<ItemStack>();

        for (int i = 0; i < 6; i++)
        {
            if (getStackInSlot(i) != null)
            {
                inputList.add(getStackInSlot(i));
            }
        }

        int tier = getTierOfOrb();

        AlchemyTableRecipe recipe = AlchemyTableRecipeRegistry.getMatchingRecipe(inputList, getWorld(), getPos());
        if (recipe != null && (burnTime > 0 || (!worldObj.isRemote && tier >= recipe.getTierRequired() && this.getContainedLp() >= recipe.getLpDrained())))
        {
            if (burnTime == 1)
            {
                IBlockState state = worldObj.getBlockState(pos);
                worldObj.notifyBlockUpdate(getPos(), state, state, 3);
            }

            if (canCraft(inputList, recipe))
            {
                ticksRequired = recipe.getTicksRequired();
                burnTime++;

                if (burnTime == ticksRequired)
                {
                    if (!worldObj.isRemote)
                    {
                        int requiredLp = recipe.getLpDrained();
                        if (requiredLp > 0)
                        {
                            if (!worldObj.isRemote)
                            {
                                consumeLp(requiredLp);
                            }
                        }

                        if (!worldObj.isRemote)
                        {
                            craftItem(inputList, recipe);
                        }
                    }

                    burnTime = 0;

                    IBlockState state = worldObj.getBlockState(pos);
                    worldObj.notifyBlockUpdate(getPos(), state, state, 3);
                } else if (burnTime > ticksRequired + 10)
                {
                    burnTime = 0;
                }
            } else
            {
                burnTime = 0;
            }
        } else
        {
            burnTime = 0;
        }
    }

    public double getProgressForGui()
    {
        return ((double) burnTime) / ticksRequired;
    }

    private boolean canCraft(List<ItemStack> inputList, AlchemyTableRecipe recipe)
    {
        if (recipe == null)
        {
            return false;
        }

        ItemStack outputStack = recipe.getRecipeOutput(inputList);
        ItemStack currentOutputStack = getStackInSlot(outputSlot);
        if (outputStack == null)
            return false;
        if (currentOutputStack == null)
            return true;
        if (!currentOutputStack.isItemEqual(outputStack))
            return false;
        int result = currentOutputStack.stackSize + outputStack.stackSize;
        return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();

    }

    public int getTierOfOrb()
    {
        ItemStack orbStack = getStackInSlot(orbSlot);
        if (orbStack != null)
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                return ((IBloodOrb) orbStack.getItem()).getOrbLevel(orbStack.getMetadata());
            }
        }

        return 0;
    }

    public int getContainedLp()
    {
        ItemStack orbStack = getStackInSlot(orbSlot);
        if (orbStack != null)
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                NBTTagCompound itemTag = orbStack.getTagCompound();

                if (itemTag == null)
                {
                    return 0;
                }

                String ownerUUID = itemTag.getString(Constants.NBT.OWNER_UUID);

                if (Strings.isNullOrEmpty(ownerUUID))
                {
                    return 0;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(ownerUUID);

                return network.getCurrentEssence();
            }
        }

        return 0;
    }

    public void craftItem(List<ItemStack> inputList, AlchemyTableRecipe recipe)
    {
        if (this.canCraft(inputList, recipe))
        {
            ItemStack outputStack = recipe.getRecipeOutput(inputList);
            ItemStack currentOutputStack = getStackInSlot(outputSlot);

            if (currentOutputStack == null)
            {
                setInventorySlotContents(outputSlot, outputStack);
            } else if (currentOutputStack.getItem() == currentOutputStack.getItem())
            {
                currentOutputStack.stackSize += outputStack.stackSize;
            }

            consumeInventory(recipe);
        }
    }

    public int consumeLp(int requested)
    {
        ItemStack orbStack = getStackInSlot(orbSlot);

        if (orbStack != null)
        {
            if (orbStack.getItem() instanceof IBloodOrb)
            {
                if (NetworkHelper.syphonFromContainer(orbStack, requested))
                {
                    return requested;
                }
            }
        }

        return 0;
    }

    public void consumeInventory(AlchemyTableRecipe recipe)
    {
        ItemStack[] input = new ItemStack[6];

        for (int i = 0; i < 6; i++)
        {
            input[i] = getStackInSlot(i);
        }

        ItemStack[] result = recipe.getRemainingItems(input);
        for (int i = 0; i < 6; i++)
        {
            setInventorySlotContents(i, result[i]);
        }
//        for (int i = 0; i < 6; i++)
//        {
//            ItemStack inputStack = getStackInSlot(i);
//            if (inputStack != null)
//            {
//                if (inputStack.getItem().hasContainerItem(inputStack))
//                {
//                    setInventorySlotContents(i, inputStack.getItem().getContainerItem(inputStack));
//                    continue;
//                }
//
//                inputStack.stackSize--;
//                if (inputStack.stackSize <= 0)
//                {
//                    setInventorySlotContents(i, null);
//                    continue;
//                }
//            }
//        }
    }
}
