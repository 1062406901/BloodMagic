package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class RitualFelling extends Ritual
{
    public static final String FELLING_RANGE = "fellingRange";
    public static final String CHEST_RANGE = "chest";

    private ArrayList<BlockPos> treePartsCache;
    private Iterator<BlockPos> blockPosIterator;

    private boolean cached = false;
    private BlockPos currentPos;

    public RitualFelling()
    {
        super("ritualFelling", 0, 20000, "ritual." + Constants.Mod.MODID + ".fellingRitual");
        addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(FELLING_RANGE, 14000, 15, 30);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);

        treePartsCache = new ArrayList<BlockPos>();
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        if (!cached || treePartsCache.isEmpty())
        {
            for (BlockPos blockPos : getBlockRange(FELLING_RANGE).getContainedPositions(masterRitualStone.getBlockPos()))
            {
                if (!treePartsCache.contains(blockPos))
                    if (!world.isAirBlock(blockPos) && (world.getBlockState(blockPos).getBlock().isWood(world, blockPos) || world.getBlockState(blockPos).getBlock().isLeaves(world.getBlockState(blockPos), world, blockPos)))
                    {
                        treePartsCache.add(blockPos);
                    }
            }

            cached = true;
            blockPosIterator = treePartsCache.iterator();
        }

        if (blockPosIterator.hasNext() && tileInventory != null && tileInventory instanceof IInventory)
        {
            network.syphon(getRefreshCost());
            currentPos = blockPosIterator.next();
            placeInInventory(world.getBlockState(currentPos), world, currentPos, chestRange.getContainedPositions(masterPos).get(0));
            world.setBlockToAir(currentPos);
            blockPosIterator.remove();
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 10;
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 1, 1, EnumRuneType.EARTH);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualFelling();
    }

    private void placeInInventory(IBlockState blockState, World world, BlockPos blockPos, BlockPos tileEntityPos)
    {
        TileEntity tile = world.getTileEntity(tileEntityPos);
        if (tile != null && blockState.getBlock().getDrops(world, blockPos, world.getBlockState(blockPos), 0) != null)
        {
            if (tile instanceof IInventory)
            {
                for (ItemStack stack : blockState.getBlock().getDrops(world, blockPos, world.getBlockState(blockPos), 0))
                {
                    ItemStack copyStack = stack.copy();
                    Utils.insertStackIntoInventory(copyStack, (IInventory) tile, EnumFacing.DOWN);
                    if (copyStack.stackSize > 0)
                    {
                        world.spawnEntity(new EntityItem(world, blockPos.getX() + 0.4, blockPos.getY() + 2, blockPos.getZ() + 0.4, copyStack));
                    }
                }
            }
        }
    }
}
