package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ProjectileDefaultFire extends ProjectileImpactEffect
{
	public ProjectileDefaultFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop) 
	{
		mop.setFire((int)Math.pow(2,this.powerUpgrades));
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		int x = mop.blockX;
		int y = mop.blockY;
		int z = mop.blockZ;
		
		if(world.isAirBlock(x, y+1, z))
		{
			world.setBlock(x, y+1, z, Block.fire.blockID);
		}
	}
}
