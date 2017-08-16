package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.key.IKeybindable;
import WayofTime.bloodmagic.client.key.KeyBindings;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ItemSigilHolding extends ItemSigilBase implements IKeybindable, IAltarReader, ISigil.Holding {
    public static final int inventorySize = 5;

    public ItemSigilHolding() {
        super("holding");
    }

    @Override
    public void onKeyPressed(ItemStack stack, EntityPlayer player, KeyBindings key, boolean showInChat) {
        if (stack == player.getHeldItemMainhand() && stack.getItem() instanceof ItemSigilHolding && key.equals(KeyBindings.OPEN_HOLDING)) {
            Utils.setUUID(stack);
            player.openGui(BloodMagic.instance, Constants.Gui.SIGIL_HOLDING_GUI, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    @Override
    public String getHighlightTip(ItemStack stack, String displayName) {
        List<ItemStack> inv = getInternalInventory(stack);
        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack item = inv.get(currentSlot);

        if (item.isEmpty())
            return displayName;
        else
            return TextHelper.localizeEffect("item.bloodmagic.sigil.holding.display", displayName, item.getDisplayName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.holding.press", KeyBindings.OPEN_HOLDING.getKey().getDisplayName()));

        if (!stack.hasTagCompound())
            return;

        List<ItemStack> inv = getInternalInventory(stack);
        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack item = inv.get(currentSlot);

        for (int i = 0; i < inventorySize; i++) {
            ItemStack invStack = inv.get(i);
            if (!invStack.isEmpty())
                if (!item.isEmpty() && invStack == item)
                    tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, "&o&n" + invStack.getDisplayName()));
                else
                    tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, invStack.getDisplayName()));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        int currentSlot = getCurrentItemOrdinal(stack);
        List<ItemStack> inv = getInternalInventory(stack);
        ItemStack itemUsing = inv.get(currentSlot);

        if (itemUsing.isEmpty() || Strings.isNullOrEmpty(((IBindable) itemUsing.getItem()).getOwnerUUID(itemUsing)))
            return EnumActionResult.PASS;

        EnumActionResult result = itemUsing.getItem().onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        saveInventory(stack, inv);

        return result;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        int currentSlot = getCurrentItemOrdinal(stack);
        List<ItemStack> inv = getInternalInventory(stack);
        ItemStack itemUsing = inv.get(currentSlot);

        if (itemUsing.isEmpty() || Strings.isNullOrEmpty(((IBindable) itemUsing.getItem()).getOwnerUUID(itemUsing)))
            return ActionResult.newResult(EnumActionResult.PASS, stack);

        itemUsing.getItem().onItemRightClick(world, player, hand);

        saveInventory(stack, inv);

        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Nonnull
    @Override
    public ItemStack getHeldItem(ItemStack holdingStack, EntityPlayer player) {
        return getInternalInventory(holdingStack).get(getCurrentItemOrdinal(holdingStack));
    }

    public void saveInventory(ItemStack itemStack, List<ItemStack> inventory) {
        NBTTagCompound itemTag = itemStack.getTagCompound();

        if (itemTag == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventorySize; i++) {
            if (!inventory.get(i).isEmpty()) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte(Constants.NBT.SLOT, (byte) i);
                inventory.get(i).writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        itemTag.setTag(Constants.NBT.ITEMS, itemList);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (itemStack.getTagCompound() != null) {
            this.tickInternalInventory(itemStack, world, entity, itemSlot, isSelected);
        }
    }

    public void tickInternalInventory(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        List<ItemStack> inv = getInternalInventory(itemStack);

        for (int i = 0; i < inventorySize; i++) {
            ItemStack stack = inv.get(i);
            if (stack.isEmpty()) {
                continue;
            }

            stack.getItem().onUpdate(stack, world, entity, itemSlot, isSelected);
        }
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        return Collections.emptyList();
    }

    public static int next(int mode) {
        int index = mode + 1;

        if (index >= inventorySize) {
            index = 0;
        }

        return index;
    }

    public static int prev(int mode) {
        int index = mode - 1;

        if (index < 0) {
            index = inventorySize;
        }

        return index;
    }

    private static void initModeTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack = NBTHelper.checkNBT(stack);
            stack.getTagCompound().setInteger(Constants.NBT.CURRENT_SIGIL, inventorySize);
        }
    }

    public static ItemStack getItemStackInSlot(ItemStack itemStack, int slot) {
        if (itemStack.getItem() instanceof ItemSigilHolding) {
            List<ItemStack> inv = getInternalInventory(itemStack);
            if (inv != null)
                return inv.get(slot == 5 ? 4 : slot);
            else
                return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    public static int getCurrentItemOrdinal(ItemStack stack) {
        if (stack.getItem() instanceof ItemSigilHolding) {
            initModeTag(stack);
            int currentSigil = stack.getTagCompound().getInteger(Constants.NBT.CURRENT_SIGIL);
            currentSigil = MathHelper.clamp(currentSigil, 0, inventorySize - 1);
            return currentSigil;
        }

        return 0;
    }

    public static List<ItemStack> getInternalInventory(ItemStack stack) {
        initModeTag(stack);
        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound == null) {
            return NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        }

        NBTTagList tagList = tagCompound.getTagList(Constants.NBT.ITEMS, 10);

        if (tagList.hasNoTags()) {
            return NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        }

        List<ItemStack> inv = NonNullList.withSize(inventorySize, ItemStack.EMPTY);

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound data = tagList.getCompoundTagAt(i);
            byte j = data.getByte(Constants.NBT.SLOT);

            if (j >= 0 && j < inv.size()) {
                inv.set(j, new ItemStack(data));
            }
        }

        return inv;
    }

    public static void cycleToNextSigil(ItemStack itemStack, int mode) {
        if (itemStack.getItem() instanceof ItemSigilHolding) {
            initModeTag(itemStack);

            int index = mode;
            if (mode == 120 || mode == -120) {
                int currentIndex = getCurrentItemOrdinal(itemStack);
                ItemStack currentItemStack = getItemStackInSlot(itemStack, currentIndex);
                if (currentItemStack.isEmpty())
                    return;
                if (mode < 0) {
                    index = next(currentIndex);
                    currentItemStack = getItemStackInSlot(itemStack, index);

                    while (currentItemStack.isEmpty()) {
                        index = next(index);
                        currentItemStack = getItemStackInSlot(itemStack, index);
                    }
                } else {
                    index = prev(currentIndex);
                    currentItemStack = getItemStackInSlot(itemStack, index);

                    while (currentItemStack.isEmpty()) {
                        index = prev(index);
                        currentItemStack = getItemStackInSlot(itemStack, index);
                    }
                }
            }

            itemStack.getTagCompound().setInteger(Constants.NBT.CURRENT_SIGIL, index);
        }
    }
}
