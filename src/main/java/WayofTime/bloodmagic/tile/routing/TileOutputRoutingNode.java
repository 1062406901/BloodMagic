package WayofTime.bloodmagic.tile.routing;

import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.IOutputItemRoutingNode;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode
{
    public TileOutputRoutingNode()
    {
        super(7, "outputNode");
    }

    @Override
    public boolean isOutput(EnumFacing side)
    {
        return true;
    }

    @Override
    public IItemFilter getOutputFilterForSide(EnumFacing side)
    {
//        ItemStack filterStack = this.getFilterStack(side);
//
//        if (filterStack == null || !(filterStack.getItem() instanceof IItemFilterProvider))
//        {
//            return null;
//        }
//
//        IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
//
//        TileEntity tile = worldObj.getTileEntity(pos.offset(side));
//        if (tile instanceof IInventory)
//        {
//            return filter.getOutputItemFilter(filterStack, (IInventory) tile, side.getOpposite());
//        }

        return null;
    }
}
