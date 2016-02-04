package WayofTime.bloodmagic.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import WayofTime.bloodmagic.api.iface.IActivatable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.event.BoundToolEvent;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.Multiset;

@Getter
public class ItemBoundTool extends ItemBindable implements IActivatable
{
    private Set<Block> effectiveBlocks;
    protected final String tooltipBase;
    private final String name;
    private final int damage;

    public Map<ItemStack, Boolean> heldDownMap = new HashMap<ItemStack, Boolean>();
    public Map<ItemStack, Integer> heldDownCountMap = new HashMap<ItemStack, Integer>();

    public final int chargeTime = 30;

    public ItemBoundTool(String name, int damage, int lpUsed, Set<Block> effectiveBlocks)
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".bound." + name);
        setLPUsed(lpUsed);

        this.name = name;
        this.tooltipBase = "tooltip.BloodMagic.bound." + name + ".";
        this.effectiveBlocks = effectiveBlocks;
        this.damage = damage;
    }

    public ItemBoundTool(String name, int damage, int lpUsed)
    {
        this(name, damage, lpUsed, null);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block)
    {
        return this.effectiveBlocks.contains(block) ? 8.0F : 1.0F;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn instanceof EntityPlayer && getActivated(stack) && isSelected && getBeingHeldDown(stack) && stack == ((EntityPlayer) entityIn).getCurrentEquippedItem())
        {
            EntityPlayer player = (EntityPlayer) entityIn;
            setHeldDownCount(stack, Math.min(player.getItemInUseDuration(), chargeTime));
        }
        else if (!isSelected)
        {
            setBeingHeldDown(stack, false);
        }
    }

    protected int getHeldDownCount(ItemStack stack)
    {
        if (!heldDownCountMap.containsKey(stack))
        {
            return 0;
        }

        return heldDownCountMap.get(stack);
    }

    protected void setHeldDownCount(ItemStack stack, int count)
    {
        heldDownCountMap.put(stack, count);
    }

    protected boolean getBeingHeldDown(ItemStack stack)
    {
        if (!heldDownMap.containsKey(stack))
        {
            return false;
        }

        return heldDownMap.get(stack);
    }

    protected void setBeingHeldDown(ItemStack stack, boolean heldDown)
    {
        heldDownMap.put(stack, heldDown);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        // if (!world.isRemote)
        {
            if (player.isSneaking())
                setActivatedState(stack, !getActivated(stack));
            // if (getActivated(stack) && ItemBindable.syphonBatteries(stack,
            // player, getLPUsed()))
            // return stack;
//            if (getActivated(stack) && ItemBindable.syphonNetwork(stack, player, getLPUsed()))
//                return stack;

            if (!player.isSneaking() && getActivated(stack))
            {
                BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return event.result;

                player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
                setBeingHeldDown(stack, true);
            }
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        this.onItemRightClick(stack, world, player);

        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
        if (!playerIn.isSneaking() && getActivated(stack))
        {
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            BoundToolEvent.Release event = new BoundToolEvent.Release(playerIn, stack, i);
            if (MinecraftForge.EVENT_BUS.post(event))
                return;

            i = event.charge;

            onBoundRelease(stack, worldIn, playerIn, Math.min(i, chargeTime));
            setBeingHeldDown(stack, false);
        }
    }

    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge)
    {

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        double damage = getActivated(stack) ? this.damage : 1.0D;
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", damage, 0));
        return multimap;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getItemEnchantability()
    {
        return 50;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (StatCollector.canTranslate(tooltipBase + "desc"))
            tooltip.add(TextHelper.localizeEffect(tooltipBase + "desc"));

        tooltip.add(TextHelper.localize("tooltip.BloodMagic." + (getActivated(stack) ? "activated" : "deactivated")));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return getActivated(stack) && getBeingHeldDown(stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return ((double) -Math.min(getHeldDownCount(stack), chargeTime) / chargeTime) + 1;
    }

    protected static void dropStacks(Multiset<ItemStackWrapper> drops, World world, BlockPos posToDrop)
    {
        for (Multiset.Entry<ItemStackWrapper> entry : drops.entrySet())
        {
            int count = entry.getCount();
            ItemStackWrapper stack = entry.getElement();
            int maxStackSize = stack.item.getItemStackLimit(stack.toStack(1));

            while (count >= maxStackSize)
            {
                world.spawnEntityInWorld(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(maxStackSize)));
                count -= maxStackSize;
            }

            if (count > 0)
                world.spawnEntityInWorld(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(count)));
        }
    }

    public boolean getActivated(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    public ItemStack setActivatedState(ItemStack stack, boolean activated)
    {
        NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);

        return stack;
    }
}
