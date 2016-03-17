package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.compat.ICompatibility;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;

public class ModCompatibility
{
    private static ArrayList<ICompatibility> compatibilities = new ArrayList<ICompatibility>();

    public static void registerModCompat()
    {
//        compatibilities.add(new CompatibilityJustEnoughItems());
//        compatibilities.add(new CompatibilityWaila());
//        compatibilities.add(new CompatibilityThaumcraft());
//        compatibilities.add(new CompatibilityGuideAPI());
    }

    public static void loadCompat(ICompatibility.InitializationPhase phase)
    {
        for (ICompatibility compatibility : compatibilities)
            if (Loader.isModLoaded(compatibility.getModId()) && compatibility.enableCompat())
                compatibility.loadCompatibility(phase);
    }
}
