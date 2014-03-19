package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemComponents extends Item
{
    private static final String[] ITEM_NAMES = new String[]{"QuartzRod", "EmptyCore", "MagicalesCable", "WoodBrace", "StoneBrace", "ProjectileCore", "SelfCore","MeleeCore","ParadigmBackPlate","OutputCable","FlameCore","IcyCore","GustCore","EarthenCore","InputCable","CrackedRunicPlate","RunicPlate","ScribedRunicPlate","DefaultCore","OffensiveCore","DefensiveCore","EnvironmentalCore","PowerCore","CostCore","PotencyCore"};

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public ItemComponents(int id)
    {
        super(id);
        this.maxStackSize = 64;
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        icons = new Icon[ITEM_NAMES.length];

        for (int i = 0; i < ITEM_NAMES.length; ++i)
        {
            icons[i] = iconRegister.registerIcon("AlchemicalWizardry:" + "baseItem" + ITEM_NAMES[i]);
        }
    }
    
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

            if (recipe != null)
            {
            	par3List.add("Used in alchemy");
                par3List.add(EnumChatFormatting.BLUE + "Recipe:");

                for (ItemStack item : recipe)
                {
                    if (item != null)
                    {
                        par3List.add("" + item.getDisplayName());
                    }
                }
            }
        } else
        {
        	ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);
        	if(recipe!=null)
        	{
        		par3List.add("Used in alchemy");
        		par3List.add("-Press " + EnumChatFormatting.BLUE + "shift" + EnumChatFormatting.GRAY + " for Recipe-");
        	}	
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        //This is what will do all the localisation things on the alchemy components so you dont have to set it :D
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ITEM_NAMES.length - 1);
        return ("" + "item.bloodMagicBaseItem." + ITEM_NAMES[meta]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        int j = MathHelper.clamp_int(meta, 0, ITEM_NAMES.length - 1);
        return icons[j];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs creativeTab, List list)
    {
        for (int meta = 0; meta < ITEM_NAMES.length; ++meta)
        {
            list.add(new ItemStack(id, 1, meta));
        }
    }
}
