package WayofTime.bloodmagic.tile.routing;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileFilteredRoutingNode extends TileRoutingNode implements ISidedInventory
{
    public int currentActiveSlot = 0;

    public TileFilteredRoutingNode(int size, String name)
    {
        super(size, name);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        currentActiveSlot = tag.getInteger("currentSlot");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("currentSlot", currentActiveSlot);
    }

    public void swapFilters(int requestedSlot)
    {
        this.setInventorySlotContents(currentActiveSlot + 1, this.getStackInSlot(0));
        this.setInventorySlotContents(0, this.getStackInSlot(requestedSlot + 1));
        this.setInventorySlotContents(requestedSlot + 1, null);
        currentActiveSlot = requestedSlot;
        this.markDirty();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }
}
