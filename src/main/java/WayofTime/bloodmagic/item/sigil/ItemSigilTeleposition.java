package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.teleport.TeleportQueue;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSigilTeleposition extends ItemSigilBase {
    public ItemSigilTeleposition() {
        super("teleposition");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        if (!stack.hasTagCompound())
            return;
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null && stack.getTagCompound().hasKey(Constants.NBT.DIMENSION_ID) && stack.getTagCompound().hasKey(Constants.NBT.X_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Y_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Z_COORD)) {
            tooltip.add(" ");
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.coords", getValue(tag, Constants.NBT.X_COORD), getValue(tag, Constants.NBT.Y_COORD), getValue(tag, Constants.NBT.Z_COORD)));
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.dimension", getValue(tag, Constants.NBT.DIMENSION_ID)));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && NBTHelper.checkNBT(stack) != null && stack.getTagCompound().hasKey(Constants.NBT.DIMENSION_ID) && stack.getTagCompound().hasKey(Constants.NBT.X_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Y_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Z_COORD)) {
            BlockPos blockPos = new BlockPos(getValue(stack.getTagCompound(), Constants.NBT.X_COORD), getValue(stack.getTagCompound(), Constants.NBT.Y_COORD), getValue(stack.getTagCompound(), Constants.NBT.Z_COORD)).up();
            if (world.provider.getDimension() == getValue(stack.getTagCompound(), Constants.NBT.DIMENSION_ID)) {
                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(blockPos, player, getOwnerUUID(stack), true));
            }
            // FIXME - Fix cross-dimension teleports causing major desync
//            } else {
//                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(blockPos, player, getOwnerUUID(stack), world, getValue(stack.getTagCompound(), Constants.NBT.DIMENSION_ID), true));
//            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        if (!world.isRemote && player.isSneaking() && NBTHelper.checkNBT(stack) != null) {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileTeleposer) {
                stack.getTagCompound().setInteger(Constants.NBT.DIMENSION_ID, world.provider.getDimension());
                stack.getTagCompound().setInteger(Constants.NBT.X_COORD, pos.getX());
                stack.getTagCompound().setInteger(Constants.NBT.Y_COORD, pos.getY());
                stack.getTagCompound().setInteger(Constants.NBT.Z_COORD, pos.getZ());

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }

    public int getValue(NBTTagCompound tag, String key) {
        return tag.getInteger(key);
    }
}
