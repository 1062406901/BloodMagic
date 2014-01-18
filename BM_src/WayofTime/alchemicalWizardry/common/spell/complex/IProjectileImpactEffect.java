package WayofTime.alchemicalWizardry.common.spell.complex;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;

public interface IProjectileImpactEffect 
{
	public void onEntityImpact(Entity mop);
	public void onTileImpact(MovingObjectPosition mop);
}
