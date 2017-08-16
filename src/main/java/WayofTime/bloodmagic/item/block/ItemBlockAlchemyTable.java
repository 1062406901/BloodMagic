package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.tile.TileAlchemyTable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockAlchemyTable extends ItemBlock {
    public ItemBlockAlchemyTable(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        float yaw = player.rotationYaw;
        EnumFacing direction = EnumFacing.fromAngle(yaw);

        if (direction.getFrontOffsetY() != 0) {
            return false;
        }

        if (!world.isAirBlock(pos.offset(direction))) {
            return false;
        }

//        newState = block.getDefaultState().withProperty(BlockAlchemyTable.DIRECTION, direction).withProperty(BlockAlchemyTable.INVISIBLE, true);

        if (!world.setBlockState(pos, newState, 3)) {
            return false;
        }

        if (!world.setBlockState(pos.offset(direction), newState, 3)) {
            return false;
        }

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileAlchemyTable) {
                ((TileAlchemyTable) tile).setInitialTableParameters(direction, false, pos.offset(direction));
            }

            TileEntity slaveTile = world.getTileEntity(pos.offset(direction));
            if (slaveTile instanceof TileAlchemyTable) {
                ((TileAlchemyTable) slaveTile).setInitialTableParameters(direction, true, pos);
            }

            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }
}