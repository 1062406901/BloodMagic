package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ImperfectRitualZombie extends ImperfectRitual
{
    public ImperfectRitualZombie()
    {
        super("zombie", new BlockStack(Blocks.coal_block), 5000, "ritual." + Constants.Mod.MODID + ".imperfect.zombie");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {
        EntityZombie zombie = new EntityZombie(imperfectRitualStone.getRitualWorld());
        zombie.setPosition(imperfectRitualStone.getRitualPos().getX() + 0.5, imperfectRitualStone.getRitualPos().getY() + 2.1, imperfectRitualStone.getRitualPos().getZ() + 0.5);
        zombie.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("fireResistance"), 2000));
        zombie.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("damageBoost"), 20000, 7));
        zombie.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), 20000, 3));

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().spawnEntityInWorld(zombie);

        return true;
    }
}
