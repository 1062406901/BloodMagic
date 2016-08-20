package WayofTime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class DungeonRoom
{
    protected Map<DungeonStructure, BlockPos> structureMap = new HashMap<DungeonStructure, BlockPos>();

    Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>(); //Map of doors. The EnumFacing indicates what way this door faces.
    List<AreaDescriptor> descriptorList = new ArrayList<AreaDescriptor>();

    public DungeonRoom(Map<DungeonStructure, BlockPos> structureMap, Map<EnumFacing, List<BlockPos>> doorMap, List<AreaDescriptor> descriptorList)
    {
        this.structureMap = structureMap;
        this.doorMap = doorMap;
        this.descriptorList = descriptorList;
    }

    public List<AreaDescriptor> getAreaDescriptors(PlacementSettings settings, BlockPos offset)
    {
        List<AreaDescriptor> newList = new ArrayList<AreaDescriptor>();

        for (AreaDescriptor desc : descriptorList)
        {
            newList.add(desc.rotateDescriptor(settings).offset(offset));
        }

        return newList;
    }

    public List<BlockPos> getDoorOffsetsForFacing(PlacementSettings settings, EnumFacing facing, BlockPos offset)
    {
        List<BlockPos> offsetList = new ArrayList<BlockPos>();

        EnumFacing originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
        if (doorMap.containsKey(originalFacing))
        {
            List<BlockPos> doorList = doorMap.get(originalFacing);
            for (BlockPos doorPos : doorList)
            {
                offsetList.add(Template.transformedBlockPos(settings, doorPos).add(offset));
            }
        }

        return offsetList;
    }

    public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, WorldServer world, BlockPos pos)
    {
        for (Entry<DungeonStructure, BlockPos> entry : structureMap.entrySet())
        {
            DungeonStructure structure = entry.getKey();
            BlockPos offsetPos = Template.transformedBlockPos(settings, entry.getValue());

            structure.placeStructureAtPosition(rand, settings, world, pos.add(offsetPos));
        }

        return true;
    }
}
