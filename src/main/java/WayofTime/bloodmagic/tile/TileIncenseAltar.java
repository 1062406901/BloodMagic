package WayofTime.bloodmagic.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.IIncensePath;
import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class TileIncenseAltar extends TileInventory implements ITickable
{
    public AreaDescriptor incenseArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public static int maxCheckRange = 5;
    public Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<EnumTranquilityType, Double>();

    public double incenseAddition = 0; //Self-sacrifice is multiplied by 1 plus this value.
    public int roadDistance = 0; //Number of road blocks laid down

    public TileIncenseAltar()
    {
        super(1, "incenseAltar");
    }

    @Override
    public void update()
    {
        AxisAlignedBB aabb = incenseArea.getAABB(getPos());
        List<EntityPlayer> playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
        if (playerList.isEmpty())
        {
            return;
        }

    }

    public void recheckConstruction()
    {
        //TODO: Check the physical construction of the incense altar to determine the maximum length.
        int maxLength = 3; //Max length of the path. The path starts two blocks away from the center block.
        int yOffset = 0;

        Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<EnumTranquilityType, Double>();

        for (int currentDistance = 2; currentDistance < currentDistance + maxLength; currentDistance++)
        {
            boolean canFormRoad = false;

            level: for (int i = -maxCheckRange + yOffset; i <= maxCheckRange + yOffset; i++)
            {
                BlockPos verticalPos = pos.add(0, i, 0);

                canFormRoad = true;
                for (EnumFacing horizontalFacing : EnumFacing.HORIZONTALS)
                {
                    BlockPos facingOffsetPos = verticalPos.offset(horizontalFacing, currentDistance);
                    for (int j = -1; j <= 1; j++)
                    {
                        BlockPos offsetPos = facingOffsetPos.offset(horizontalFacing.rotateY(), j);
                        IBlockState state = worldObj.getBlockState(offsetPos);
                        Block block = state.getBlock();
                        if (!(block instanceof IIncensePath && ((IIncensePath) block).getLevelOfPath(worldObj, offsetPos, state) >= currentDistance - 2))
                        {
                            canFormRoad = false;
                            break level;
                        }
                    }
                }

                if (canFormRoad)
                {
                    yOffset = i;
                }
            }

            if (canFormRoad)
            {
                for (int i = -currentDistance; i <= currentDistance; i++)
                {
                    for (int j = -currentDistance; j <= currentDistance; j++)
                    {
                        if (Math.abs(i) != currentDistance && Math.abs(j) != currentDistance)
                        {
                            break; //TODO: Can make this just set j to currentDistance to speed it up.
                        }

                        for (int y = -1 + yOffset; y <= 1 + yOffset; y++)
                        {
                            BlockPos offsetPos = pos.add(i, yOffset, j);
                            IBlockState state = worldObj.getBlockState(offsetPos);
                            Block block = state.getBlock();
                            TranquilityStack stack = IncenseTranquilityRegistry.getTranquilityOfBlock(worldObj, offsetPos, block, state);
                            if (stack != null)
                            {
                                if (!tranquilityMap.containsKey(stack.type))
                                {
                                    tranquilityMap.put(stack.type, stack.value);
                                } else
                                {
                                    tranquilityMap.put(stack.type, tranquilityMap.get(stack.type) + stack.value);
                                }
                            }
                        }
                    }
                }
            } else
            {
                roadDistance = currentDistance - 2;
                break;
            }
        }

        this.tranquilityMap = tranquilityMap;

        double totalTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
        {
            totalTranquility += entry.getValue();
        }

        if (totalTranquility <= 0)
        {
            return;
        }

        double appliedTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
        {
            appliedTranquility += Math.pow(entry.getValue(), 0.9);
        }
    }
}
