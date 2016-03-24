package WayofTime.bloodmagic.api.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;

/**
 * Base class for all bindable items.
 */
public class ItemBindable extends Item implements IBindable
{
    public ItemBindable()
    {
        super();

        setMaxStackSize(1);
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack)
    {
        return true;
    }

    @Override
    public String getOwnerName(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    @Override
    public String getOwnerUUID(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }
}
