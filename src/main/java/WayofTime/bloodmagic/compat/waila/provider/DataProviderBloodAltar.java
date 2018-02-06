package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.item.sigil.ItemSigilSeer;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Integrated from WailaPlugins by <a
 * href="https://github.com/tterrag1098">tterrag1098</a>. Originally implemented
 * in ImLookingAtBlood by <a href="https://github.com/Pokefenn">Pokefenn</a>.
 */
public class DataProviderBloodAltar implements IWailaDataProvider {
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_ALTAR))
            return currenttip;

        boolean hasSigil = false;
        boolean hasSeer = false;

        switch (ConfigHandler.compat.wailaAltarDisplayMode) {
            case ALWAYS: {
                hasSigil = hasSeer = true;
                break;
            }
            case SIGIL_HELD: {
                hasSeer = holdingSeerSigil(accessor.getPlayer());
                hasSigil = hasSeer || holdingDivinationSigil(accessor.getPlayer());
                break;
            }
            case SIGIL_CONTAINED: {
                hasSeer = hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER), accessor.getPlayer());
                hasSigil = hasSeer || hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION), accessor.getPlayer());
                break;
            }
        }

        if (!hasSeer && !hasSigil)
            return currenttip;

        if (accessor.getPlayer().isSneaking() || config.getConfig(Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK)) {
            if (accessor.getBlock() instanceof BlockAltar && accessor.getTileEntity() instanceof TileAltar) {
                TileAltar altar = (TileAltar) accessor.getTileEntity();
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarTier", altar.getTier().toInt()));
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarCapacity", altar.getCapacity()));
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentEssence", altar.getCurrentBlood()));

                if (hasSeer) {
                    int charge = accessor.getNBTData().getCompoundTag("bloodAltar").getInteger(Constants.NBT.ALTAR_TOTAL_CHARGE);
                    int progress = accessor.getNBTData().getCompoundTag("bloodAltar").getInteger(Constants.NBT.ALTAR_PROGRESS);
                    int liquidRequired = accessor.getNBTData().getCompoundTag("bloodAltar").getInteger(Constants.NBT.ALTAR_LIQUID_REQ);
                    int craftAmount = 1;
                    if (accessor.getNBTData().getTagList("Items", 10).get(0).getId() == 10)
                        craftAmount = ((NBTTagCompound) accessor.getNBTData().getTagList("Items", 10).get(0)).getByte("Count");
                    currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarProgress.percent", (int) (((double) progress / (double) liquidRequired * 100) / craftAmount) + "%"));
                    currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentCharge", charge));
                }
            }
        } else {
            currenttip.add(TextHelper.localizeEffect("waila.bloodmagic.sneak"));
        }

        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }

    public static boolean hasStack(ItemStack stack, EntityPlayer player) {
        for (ItemStack inventoryStack : player.inventory.mainInventory)
            if (inventoryStack != null && inventoryStack.isItemEqual(stack))
                return true;

        return false;
    }

    private static boolean holdingSeerSigil(EntityPlayer player) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemSigilSeer)
            return true;

        if (player.getHeldItemOffhand().getItem() instanceof ItemSigilSeer)
            return true;

        return false;
    }

    private static boolean holdingDivinationSigil(EntityPlayer player) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemSigilDivination)
            return true;

        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemSigilDivination)
            return true;

        return false;
    }
}
