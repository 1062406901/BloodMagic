package WayofTime.alchemicalWizardry.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class TileInventory extends TileEntity implements IInventory {

    private ItemStack[] inventory;
    private int size;
    private String name;

    public TileInventory(int size, String name) {
        this.inventory = new ItemStack[size];
        this.size = size;
        this.name = name;
    }

    public void dropItems() {
        for (ItemStack stack : inventory)
            getWorld().spawnEntityInWorld(new EntityItem(getWorld(), getPos().getX(), pos.getY(), pos.getZ(), stack));
    }

    // IInventory

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack slotStack = getStackInSlot(index);

        if (slotStack.stackSize > count)
            slotStack.stackSize -= count;
        else if (slotStack.stackSize <= count)
            return null;

        return slotStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return inventory[index];
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory = new ItemStack[size];
    }

    // IWorldNameable

    @Override
    public String getName() {
        return StatCollector.translateToLocal("tile.AlchemicalWizardry." + name + ".name");
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation("tile.AlchemicalWizardry." + name + ".name");
    }
}
