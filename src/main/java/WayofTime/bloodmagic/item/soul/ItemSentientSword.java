package WayofTime.bloodmagic.item.soul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IActivatable;
import WayofTime.bloodmagic.api.iface.ISentientSwordEffectProvider;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillWeapon;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemSentientSword extends ItemSword implements IDemonWillWeapon, IActivatable, IMeshProvider
{
    public int[] soulBracket = new int[] { 16, 60, 200, 400 };
    public double[] damageAdded = new double[] { 1, 1.5, 2, 2.5 };
    public double[] soulDrainPerSwing = new double[] { 0.05, 0.1, 0.2, 0.4 };
    public double[] soulDrop = new double[] { 2, 4, 8, 12 };
    public double[] staticDrop = new double[] { 1, 1, 2, 3 };

    public ItemSentientSword()
    {
        super(ModItems.soulToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".sentientSword");
        setRegistryName(Constants.BloodMagicItem.SENTIENT_SWORD.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (super.hitEntity(stack, target, attacker))
        {
            if (attacker instanceof EntityPlayer)
            {
                EntityPlayer attackerPlayer = (EntityPlayer) attacker;
                ItemStack offStack = attackerPlayer.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
                if (offStack != null && offStack.getItem() instanceof ISentientSwordEffectProvider)
                {
                    ISentientSwordEffectProvider provider = (ISentientSwordEffectProvider) offStack.getItem();
                    if (provider.providesEffectForWill(EnumDemonWillType.DEFAULT))
                    {
                        provider.applyOnHitEffect(EnumDemonWillType.DEFAULT, stack, offStack, attacker, target);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public EnumDemonWillType getCurrentType(ItemStack stack)
    {
        return EnumDemonWillType.DEFAULT;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (player.isSneaking())
            setActivatedState(stack, !getActivated(stack));

        if (getActivated(stack))
        {
            double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(getCurrentType(stack), player);
            int level = getLevel(stack, soulsRemaining);

            double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
            double extraDamage = level >= 0 ? damageAdded[level] : 0;

            setDrainOfActivatedSword(stack, drain);
            setDamageOfActivatedSword(stack, 7 + extraDamage);
            setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
            setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);
        }

        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack.getItem() != newStack.getItem();
    }

    private int getLevel(ItemStack stack, double soulsRemaining)
    {
        int lvl = -1;
        for (int i = 0; i < soulBracket.length; i++)
        {
            if (soulsRemaining >= soulBracket[i])
            {
                lvl = i;
            }
        }

        return lvl;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.sentientSword.desc"))));

        if (getActivated(stack))
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.activated"));
        else
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.deactivated"));

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (getActivated(stack))
        {
            double drain = this.getDrainOfActivatedSword(stack);
            if (drain > 0)
            {
                EnumDemonWillType type = getCurrentType(stack);
                double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);

                if (drain > soulsRemaining)
                {
                    setActivatedState(stack, false);
                    return false;
                } else
                {
                    PlayerDemonWillHandler.consumeDemonWill(type, player, drain);
                }
            }
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new CustomMeshDefinitionActivatable("ItemSentientSword");
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation()
    {
        return null;
    }

    @Override
    public List<String> getVariants()
    {
        List<String> ret = new ArrayList<String>();
        ret.add("active=true");
        ret.add("active=false");
        return ret;
    }

    @Override
    public boolean getActivated(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    @Override
    public ItemStack setActivatedState(ItemStack stack, boolean activated)
    {
        NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);

        return stack;
    }

    @Override
    public List<ItemStack> getRandomDemonWillDrop(EntityLivingBase killedEntity, EntityLivingBase attackingEntity, ItemStack stack, int looting)
    {
        List<ItemStack> soulList = new ArrayList<ItemStack>();

        if (getActivated(stack))
        {
            IDemonWill soul = ((IDemonWill) ModItems.monsterSoul);

            for (int i = 0; i <= looting; i++)
            {
                if (i == 0 || attackingEntity.worldObj.rand.nextDouble() < 0.4)
                {
                    ItemStack soulStack = soul.createWill(0, this.getDropOfActivatedSword(stack) * attackingEntity.worldObj.rand.nextDouble() + this.getStaticDropOfActivatedSword(stack));
                    soulList.add(soulStack);
                }
            }
        }

        return soulList;
    }

    //TODO: Change attack speed.
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getActivated(stack) ? getDamageOfActivatedSword(stack) : 2, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
        }
        return multimap;
    }

    public double getDamageOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_DAMAGE);
    }

    public void setDamageOfActivatedSword(ItemStack stack, double damage)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_DAMAGE, damage);
    }

    public double getDrainOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN);
    }

    public void setDrainOfActivatedSword(ItemStack stack, double drain)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN, drain);
    }

    public double getStaticDropOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP);
    }

    public void setStaticDropOfActivatedSword(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP, drop);
    }

    public double getDropOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_DROP);
    }

    public void setDropOfActivatedSword(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_DROP, drop);
    }
}
