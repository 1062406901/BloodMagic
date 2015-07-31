package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;

public class ItemComplexSpellCrystal extends BindableItems
{
    public ItemComplexSpellCrystal()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.complexspellcrystal.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = par1ItemStack.getTagCompound();

            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
            }

            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.coords") + " " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimensionID(par1ItemStack));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par2World.isRemote)
        {
            World world = DimensionManager.getWorld(getDimensionID(par1ItemStack));

            if (world != null)
            {
                NBTTagCompound itemTag = par1ItemStack.getTagCompound();
                TileEntity tileEntity = world.getTileEntity(new BlockPos(itemTag.getInteger("xCoord"), itemTag.getInteger("yCoord"), itemTag.getInteger("zCoord")));

                if (tileEntity instanceof TESpellParadigmBlock)
                {
                    TESpellParadigmBlock tileParad = (TESpellParadigmBlock) tileEntity;

                    tileParad.castSpell(par2World, par3EntityPlayer, par1ItemStack);
                } else
                {
                    return par1ItemStack;
                }
            } else
            {
                return par1ItemStack;
            }
        } else
        {
            return par1ItemStack;
        }
        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        return par1ItemStack;
    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getInteger("dimensionId");
    }
}
