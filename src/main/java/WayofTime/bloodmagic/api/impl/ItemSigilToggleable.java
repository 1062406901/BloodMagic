package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IActivatable;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for all toggleable sigils.
 */
public class ItemSigilToggleable extends ItemSigil implements IActivatable
{
    public ItemSigilToggleable(int lpUsed)
    {
        super(lpUsed);
    }

    @Override
    public boolean getActivated(ItemStack stack)
    {
        return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    @Override
    public ItemStack setActivatedState(ItemStack stack, boolean activated)
    {
        if (!stack.isEmpty())
        {
            NBTHelper.checkNBT(stack).getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);
            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && !isUnusable(stack))
        {
            if (player.isSneaking())
                setActivatedState(stack, !getActivated(stack));
            if (getActivated(stack) && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
                return super.onItemRightClick(world, player, hand);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (Strings.isNullOrEmpty(getOwnerUUID(stack)) || player.isSneaking()) // Make sure Sigils are bound before handling. Also ignores while toggling state
            return EnumActionResult.PASS;

        return (NetworkHelper.getSoulNetwork(getOwnerUUID(player.getHeldItem(hand))).syphonAndDamage(player, getLpUsed()) && onSigilUse(player.getHeldItem(hand), player, world, pos, side, hitX, hitY, hitZ)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    public boolean onSigilUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP && getActivated(stack))
        {
            if (entityIn.ticksExisted % 100 == 0)
            {
                if (!NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage((EntityPlayer) entityIn, getLpUsed()))
                {
                    setActivatedState(stack, false);
                }
            }

            onSigilUpdate(stack, worldIn, (EntityPlayer) entityIn, itemSlot, isSelected);
        }
    }

    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
    }
}
