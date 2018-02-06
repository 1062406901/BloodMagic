package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.BlockStack;
import WayofTime.bloodmagic.apibutnotreally.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.apibutnotreally.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class ImperfectRitualRain extends ImperfectRitual {
    public ImperfectRitualRain() {
        super("rain", new BlockStack(Blocks.WATER), 5000, true, "ritual." + BloodMagic.MODID + ".imperfect.rain");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {
        if (!imperfectRitualStone.getRitualWorld().isRemote) {
            imperfectRitualStone.getRitualWorld().getWorldInfo().setRaining(true);
        }

        if (imperfectRitualStone.getRitualWorld().isRemote) {
            imperfectRitualStone.getRitualWorld().setRainStrength(1.0F);
            imperfectRitualStone.getRitualWorld().setThunderStrength(1.0F);
        }

        return true;
    }
}
