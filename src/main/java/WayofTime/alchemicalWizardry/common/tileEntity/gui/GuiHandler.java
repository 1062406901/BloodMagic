package WayofTime.alchemicalWizardry.common.tileEntity.gui;

import WayofTime.alchemicalWizardry.common.items.sigil.holding.ContainerHolding;
import WayofTime.alchemicalWizardry.common.items.sigil.holding.GuiHolding;
import WayofTime.alchemicalWizardry.common.items.sigil.holding.InventoryHolding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiCategories;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiEntry;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiIndex;
import WayofTime.alchemicalWizardry.book.compact.Category;
import WayofTime.alchemicalWizardry.book.registries.EntryRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.container.ContainerTeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.container.ContainerWritingTable;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity;

        switch (id)
        {
            case 0:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TEWritingTable)
                {
                    return new ContainerWritingTable(player.inventory, (TEWritingTable) tileEntity);
                }

            case 1:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TETeleposer)
                {
                    return new ContainerTeleposer(player.inventory, (TETeleposer) tileEntity);
                }

            case 3:
                return new ContainerHolding(player, new InventoryHolding(player.getHeldItem()));
        }

        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity;
        
        ItemStack held = player.getHeldItem();

        switch (id)
        {
            case 0:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TEWritingTable)
                {
                    return new GuiWritingTable(player.inventory, (TEWritingTable) tileEntity);
                }

                break;

            case 1:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TETeleposer)
                {
                    return new GuiTeleposer(player.inventory, (TETeleposer) tileEntity);
                }

                break;
                
            case 2:
            	
            	if(held.hasTagCompound() && held.getTagCompound().getString("CATEGORY") != null){
            		if(held.hasTagCompound() && held.getTagCompound().getString("KEY") != null && held.getTagCompound().getString("KEY") != "0"){
            			String cate = held.getTagCompound().getString("CATEGORY");
            			String key = held.getTagCompound().getString("KEY");
            			int page = held.getTagCompound().getInteger("PAGE");
            			if(EntryRegistry.categoryMap.containsKey(cate)){
            				Category category = EntryRegistry.categoryMap.get(cate);
            				return new GuiEntry(key, player, category, page);
            			}else{
                        	return new GuiCategories(player);
            			}
            		}else if(held.hasTagCompound() && held.getTagCompound().getString("CATEGORY") != null){
            			String cate = held.getTagCompound().getString("CATEGORY");
            			int page = held.getTagCompound().getInteger("PAGE");
            			if(EntryRegistry.categoryMap.containsKey(cate)){
            				Category category = EntryRegistry.categoryMap.get(cate);
            				return new GuiIndex(category, player, page);
            			}
            		}
            	}
            	return new GuiCategories(player);

            case 3:
                return new GuiHolding(player, new InventoryHolding(player.getHeldItem()));
        }

        return null;
    }
}