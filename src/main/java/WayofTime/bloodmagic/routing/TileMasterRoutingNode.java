package WayofTime.bloodmagic.routing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;

public class TileMasterRoutingNode extends TileEntity implements IMasterRoutingNode, ITickable
{
    // A list of connections
    private HashMap<BlockPos, List<BlockPos>> connectionMap = new HashMap<BlockPos, List<BlockPos>>();
    private List<BlockPos> generalNodeList = new LinkedList<BlockPos>();
    private List<BlockPos> outputNodeList = new LinkedList<BlockPos>();
    private List<BlockPos> inputNodeList = new LinkedList<BlockPos>();

    @Override
    public void update()
    {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        NBTTagList tags = new NBTTagList();
        for (BlockPos pos : generalNodeList)
        {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_GENERAL, tags);

        tags = new NBTTagList();
        for (BlockPos pos : inputNodeList)
        {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_INPUT, tags);

        tags = new NBTTagList();
        for (BlockPos pos : outputNodeList)
        {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_OUTPUT, tags);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        NBTTagList tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_GENERAL, 10);
        for (int i = 0; i < tags.tagCount(); i++)
        {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            generalNodeList.add(newPos);
        }

        tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_INPUT, 10);
        for (int i = 0; i < tags.tagCount(); i++)
        {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            inputNodeList.add(newPos);
        }

        tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_OUTPUT, 10);
        for (int i = 0; i < tags.tagCount(); i++)
        {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            outputNodeList.add(newPos);
        }
    }

    @Override
    public boolean isConnected(List<BlockPos> path, BlockPos nodePos)
    {
        //TODO: Figure out how to make it so the path is obtained
//        if (!connectionMap.containsKey(nodePos))
//        {
//            return false;
//        }
        TileEntity tile = worldObj.getTileEntity(nodePos);
        if (!(tile instanceof IRoutingNode))
        {
//            connectionMap.remove(nodePos);
            return false;
        }

        IRoutingNode node = (IRoutingNode) tile;
        List<BlockPos> connectionList = node.getConnected();
//        List<BlockPos> testPath = path.subList(0, path.size());
        path.add(nodePos);
        for (BlockPos testPos : connectionList)
        {
            if (path.contains(testPos))
            {
                continue;
            }

            if (testPos.equals(this.getPos()) && node.isConnectionEnabled(testPos))
            {
//                path.clear();
//                path.addAll(testPath);
                return true;
            } else if (NodeHelper.isNodeConnectionEnabled(worldObj, node, testPos))
            {
                if (isConnected(path, testPos))
                {
//                    path.clear();
//                    path.addAll(testPath);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isConnectionEnabled(BlockPos testPos)
    {
        return true;
    }

    @Override
    public void addNodeToList(IRoutingNode node)
    {
        BlockPos newPos = node.getBlockPos();
        if (!generalNodeList.contains(newPos))
        {
            generalNodeList.add(newPos);
        }
        if (node instanceof TileInputRoutingNode && !inputNodeList.contains(newPos))
        {
            inputNodeList.add(newPos);
        }
        if (node instanceof TileOutputRoutingNode && !outputNodeList.contains(newPos))
        {
            outputNodeList.add(newPos);
        }
    }

    @Override
    public void addConnections(BlockPos pos, List<BlockPos> connectionList)
    {
        for (BlockPos testPos : connectionList)
        {
            addConnection(pos, testPos);
        }
    }

    @Override
    public void addConnection(BlockPos pos1, BlockPos pos2)
    {
        if (connectionMap.containsKey(pos1) && !connectionMap.get(pos1).contains(pos2))
        {
            connectionMap.get(pos1).add(pos2);
        } else
        {
            List<BlockPos> list = new LinkedList<BlockPos>();
            list.add(pos2);
            connectionMap.put(pos1, list);
        }

        if (connectionMap.containsKey(pos2) && !connectionMap.get(pos2).contains(pos1))
        {
            connectionMap.get(pos2).add(pos1);
        } else
        {
            List<BlockPos> list = new LinkedList<BlockPos>();
            list.add(pos1);
            connectionMap.put(pos2, list);
        }
    }

    @Override
    public void removeConnection(BlockPos pos1, BlockPos pos2)
    {
        if (connectionMap.containsKey(pos1))
        {
            List<BlockPos> posList = connectionMap.get(pos1);
            posList.remove(pos2);
            if (posList.isEmpty())
            {
                connectionMap.remove(pos1);
            }
        }

        if (connectionMap.containsKey(pos2))
        {
            List<BlockPos> posList = connectionMap.get(pos2);
            posList.remove(pos1);
            if (posList.isEmpty())
            {
                connectionMap.remove(pos2);
            }
        }
    }

    @Override
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
    {
        return;
    }

    @Override
    public BlockPos getBlockPos()
    {
        return this.getPos();
    }

    @Override
    public List<BlockPos> getConnected()
    {
        return new LinkedList<BlockPos>();
    }

    @Override
    public BlockPos getMasterPos()
    {
        return this.getPos();
    }

    @Override
    public boolean isMaster(IMasterRoutingNode master)
    {
        return false;
    }

    @Override
    public void addConnection(BlockPos pos1)
    {
        // Empty
    }

    @Override
    public void removeConnection(BlockPos pos1)
    {
        generalNodeList.remove(pos1);
        inputNodeList.remove(pos1);
        outputNodeList.remove(pos1);
    }

    @Override
    public void removeAllConnections()
    {
        // Empty
    }
}
