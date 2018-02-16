package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.event.ItemBindEvent;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.handler.event.GenericHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;

public class BindableHelper {
    /**
     * Sets the Owner Name of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     */
    public static void setItemOwnerName(ItemStack stack, String ownerName) {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, ownerName);
    }

    /**
     * Sets the Owner UUID of the item without checking if it is already bound.
     * Also bypasses {@link ItemBindEvent}.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerUUID - The UUID to bind the ItemStack to
     */
    public static void setItemOwnerUUID(ItemStack stack, String ownerUUID) {
        stack = NBTHelper.checkNBT(stack);

        stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, ownerUUID);
    }

    // Everything below is to be removed.

    /**
     * Deprecated.
     * <p>
     * Built into {@link IBindable} now.
     *
     * @param stack - The ItemStack to check the owner of
     * @return - The username of the ItemStack's owner
     */
    @Deprecated
    public static String getOwnerName(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        return PlayerHelper.getUsernameFromStack(stack);
    }

    /**
     * Deprecated.
     * <p>
     * Built into {@link IBindable} now.
     *
     * @param stack - The ItemStack to check the owner of
     * @return - The UUID of the ItemStack's owner
     */
    @Deprecated
    public static String getOwnerUUID(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);
    }

    /**
     * Deprecated.
     * <p>
     * Now handled automatically with
     * {@link GenericHandler#onInteract(PlayerInteractEvent.RightClickItem)}
     *
     * @param stack  - The ItemStack to bind
     * @param player - The Player to bind the ItemStack to
     * @return - Whether binding was successful
     */
    @Deprecated
    public static boolean checkAndSetItemOwner(ItemStack stack, EntityPlayer player) {
        return !PlayerHelper.isFakePlayer(player) && checkAndSetItemOwner(stack, PlayerHelper.getUUIDFromPlayer(player), player.getName());
    }

    /**
     * Deprecated.
     * <p>
     * Now handled automatically with
     * {@link GenericHandler#onInteract(PlayerInteractEvent.RightClickItem)}
     *
     * @param stack           - The ItemStack to bind
     * @param uuid            - The username to bind the ItemStack to
     * @param currentUsername - The current name of the player.
     * @return - Whether the binding was successful
     */
    @Deprecated
    public static boolean checkAndSetItemOwner(ItemStack stack, String uuid, String currentUsername) {
        stack = NBTHelper.checkNBT(stack);

        if (!(stack.getItem() instanceof IBindable))
            return false;

        String currentOwner = stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);

        if (currentOwner == "") //The player has not been set yet, so set everything.
        {
            MinecraftForge.EVENT_BUS.post(new ItemBindEvent(PlayerHelper.getPlayerFromUUID(uuid), uuid, stack));
            ((IBindable) stack.getItem()).onBind(PlayerHelper.getPlayerFromUUID(uuid), stack);
            stack.getTagCompound().setString(Constants.NBT.OWNER_UUID, uuid);
            stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, currentUsername);
            return true;
        } else if (currentOwner.equals(uuid)) //The player has been set, so this will simply update the display name
        {
            stack.getTagCompound().setString(Constants.NBT.OWNER_NAME, currentUsername);
        }

        return true;
    }

    /**
     * Deprecated.
     * <p>
     * Now handled automatically with
     * {@link GenericHandler#onInteract(PlayerInteractEvent.RightClickItem)}
     *
     * @param stack           - ItemStack to check
     * @param uuid            - UUID of the Player
     * @param currentUsername - The current name of the player.
     */
    @Deprecated
    public static boolean checkAndSetItemOwner(ItemStack stack, UUID uuid, String currentUsername) {
        return checkAndSetItemOwner(stack, uuid.toString(), currentUsername);
    }

    /**
     * Deprecated.
     *
     * @param stack     - The ItemStack to bind
     * @param ownerName - The username to bind the ItemStack to
     * @see #setItemOwnerName(ItemStack, String)
     */
    @Deprecated
    public static void setItemOwner(ItemStack stack, String ownerName) {
        setItemOwnerName(stack, ownerName);
    }
}
