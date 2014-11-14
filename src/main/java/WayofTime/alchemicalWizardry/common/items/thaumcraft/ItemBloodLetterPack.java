package WayofTime.alchemicalWizardry.common.items.thaumcraft;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBloodLetterPack extends ItemArmor implements ArmourUpgrade
{
    private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;
    
    public static int conversionRate = 100; //LP / half heart
    public static float activationPoint = 0.5f;
    public static int tickRate = 20;

    public ItemBloodLetterPack()
    {
        super(ArmorMaterial.CHAIN, 0, 1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineHelmet");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPlate");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundLeggings");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundBoots");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Crystal of unimaginable power");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

            par3List.add("Stored LP: " + this.getStoredLP(par1ItemStack));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        return this.itemIcon;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "alchemicalwizardry:models/armor/sanguineArmour_layer_1.png";
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return itemStack;
        }

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {
        	return super.onItemRightClick(itemStack, world, player);
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                TileEntity tile = world.getTileEntity(x, y, z);

                if (!(tile instanceof TEAltar))
                {
                    return super.onItemRightClick(itemStack, world, player);
                }
                
                TEAltar altar = (TEAltar)tile;
                
                if(!altar.isActive())
                {
                	int amount = this.getStoredLP(itemStack);
                	if(amount > 0)
                	{
                		int filledAmount = altar.fillMainTank(amount);
                		amount -= filledAmount;
                		this.setStoredLP(itemStack, amount);
                	}
                }
            }
        }
        
        return itemStack;
    }
    
    public void setStoredLP(ItemStack itemStack, int lp)
    {
    	if(!itemStack.hasTagCompound())
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	NBTTagCompound tag = itemStack.getTagCompound();
    	
    	tag.setInteger("storedLP", lp);
    }
    
    public int getStoredLP(ItemStack itemStack)
    {
    	if(!itemStack.hasTagCompound())
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	NBTTagCompound tag = itemStack.getTagCompound();
    	
    	return tag.getInteger("storedLP");
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
    	//This is where I need to do the updating
        return;
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }
}