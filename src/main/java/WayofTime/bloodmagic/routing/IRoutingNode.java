package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRoutingNode
{
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master);

    public BlockPos getBlockPos();

    public List<BlockPos> getConnected();

    public BlockPos getMasterPos();

    public boolean isConnectionEnabled(BlockPos testPos);
}
