package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;

public class TEConduit extends TESpellBlock
{
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
    }

    //Logic for the actual block is under here
    @Override
    public void updateEntity()
    {

    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.getBlockOrientationPacket(this);
    }

	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		return;		
	}
}
