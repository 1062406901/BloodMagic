package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.ritual.data.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.data.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class ImperfectRitualDay extends ImperfectRitual {
    public ImperfectRitualDay() {
        super("day", new BlockStack(Blocks.GOLD_BLOCK), 5000, true, "ritual." + BloodMagic.MODID + ".imperfect.day");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().setWorldTime((imperfectRitualStone.getRitualWorld().getWorldTime() / 24000) * 24000);

        return true;
    }
}
