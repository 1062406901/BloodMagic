package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancementCost;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancementPotency;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancementPower;

public class TESpellEnhancementBlock extends TESpellBlock 
{
	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		int i = -1;
		
		switch(this.enhancementType())
		{
		case 0:
			i = parad.getBufferedEffectPower();
			break;
		case 1:
			i = parad.getBufferedEffectCost();
			break;
		case 2:
			i = parad.getBufferedEffectPotency();
			break;
		}
		
		if(i!=-1 && i<this.getLimit())
		{
			parad.applyEnhancement(getSpellEnhancement());
		}
		else if(i<this.getLimit())
		{
			this.doBadStuff();
		}
	}
	
	public SpellEnhancement getSpellEnhancement()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			return new SpellEnhancementPower();
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return new SpellEnhancementCost();
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
			return new SpellEnhancementPotency();
		}
		return new SpellEnhancementCost();
	}
	
	public int getLimit()
	{
		return 5;
	}
	
	public int enhancementType() //0 is power, 1 is cost, 2 is potency
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			return 0;
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return 1;
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
			return 2;
		}
		return 1;
	}
	
	public void doBadStuff()
	{
		
	}
	
	@Override
	public String getResourceLocationForMeta(int meta)
	{
		switch(meta)
		{
		case 0: return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
		case 1: return "alchemicalwizardry:textures/models/SpellEnhancementPower2.png";
		case 2: return "alchemicalwizardry:textures/models/SpellEnhancementPower3.png";

		}
    	return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
	}
}
