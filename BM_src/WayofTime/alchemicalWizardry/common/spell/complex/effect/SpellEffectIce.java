package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfEnvironmentalIce;

public class SpellEffectIce extends SpellEffect 
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defaultModificationSelf(SpellParadigmSelf parad)
	{
		parad.addSelfSpellEffect(new SelfDefaultIce(this.powerEnhancement,this.potencyEnhancement, this.costEnhancement));

	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad)
	{
		parad.addEntityEffect(new MeleeOffensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCostForDefaultProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForOffenseProjectile()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefaultSelf()
	{
		return (int)(20*(this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefenseSelf()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentSelf() 
	{
		return (int)(10*(1.5*potencyEnhancement+1)*(3*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultMelee() 
	{
		return (int)(100*(potencyEnhancement+1)*(1.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee() 
	{
		return (int)(25*(1.5*potencyEnhancement+1)*Math.pow(1.5, powerEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(10*(0.5*potencyEnhancement+1)*(0.7*powerEnhancement+1)*(0.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
