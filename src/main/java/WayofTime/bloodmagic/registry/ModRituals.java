package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall;
import WayofTime.bloodmagic.ritual.imperfect.*;
import net.minecraft.init.Blocks;

public class ModRituals
{
    public static Ritual waterRitual;
    public static Ritual lavaRitual;
    public static Ritual greenGroveRitual;
    public static Ritual jumpRitual;
    public static Ritual sufferingRitual;
    public static Ritual featheredKnifeRitual;
    public static Ritual regenerationRitual;
    public static Ritual animalGrowthRitual;
    public static Ritual harvestRitual;
    public static Ritual magneticRitual;
    public static Ritual crushingRitual;
    public static Ritual stomachRitual;
    public static Ritual interdictionRitual;
    public static Ritual containmentRitual;
    public static Ritual speedRitual;
    public static Ritual suppressionRitual;
    public static Ritual expulsionRitual;
    public static Ritual zephyrRitual;

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;
    public static ImperfectRitual imperfectResistance;
    public static ImperfectRitual imperfectZombie;

    public static void initRituals()
    {
        waterRitual = new RitualWater();
        RitualRegistry.registerRitual(waterRitual, waterRitual.getName());
        lavaRitual = new RitualLava();
        RitualRegistry.registerRitual(lavaRitual, lavaRitual.getName());
        greenGroveRitual = new RitualGreenGrove();
        RitualRegistry.registerRitual(greenGroveRitual, greenGroveRitual.getName());
        jumpRitual = new RitualJumping();
        RitualRegistry.registerRitual(jumpRitual, jumpRitual.getName());
        sufferingRitual = new RitualWellOfSuffering();
        RitualRegistry.registerRitual(sufferingRitual, sufferingRitual.getName());
        featheredKnifeRitual = new RitualFeatheredKnife();
        RitualRegistry.registerRitual(featheredKnifeRitual, featheredKnifeRitual.getName());
        regenerationRitual = new RitualRegeneration();
        RitualRegistry.registerRitual(regenerationRitual, regenerationRitual.getName());
        animalGrowthRitual = new RitualAnimalGrowth();
        RitualRegistry.registerRitual(animalGrowthRitual, animalGrowthRitual.getName());
        harvestRitual = new RitualHarvest();
        RitualRegistry.registerRitual(harvestRitual, harvestRitual.getName());
        initHarvestHandlers();
        magneticRitual = new RitualMagnetic();
        RitualRegistry.registerRitual(magneticRitual, magneticRitual.getName());
        crushingRitual = new RitualCrushing();
        RitualRegistry.registerRitual(crushingRitual, crushingRitual.getName());
        stomachRitual = new RitualFullStomach();
        RitualRegistry.registerRitual(stomachRitual, stomachRitual.getName());
        interdictionRitual = new RitualInterdiction();
        RitualRegistry.registerRitual(interdictionRitual, interdictionRitual.getName());
        containmentRitual = new RitualContainment();
        RitualRegistry.registerRitual(containmentRitual, containmentRitual.getName());
        speedRitual = new RitualSpeed();
        RitualRegistry.registerRitual(speedRitual, speedRitual.getName());
        suppressionRitual = new RitualSuppression();
        RitualRegistry.registerRitual(suppressionRitual, suppressionRitual.getName());
        zephyrRitual = new RitualZephyr();
        RitualRegistry.registerRitual(zephyrRitual, zephyrRitual.getName());
        expulsionRitual = new RitualExpulsion();
        RitualRegistry.registerRitual(expulsionRitual, expulsionRitual.getName());
    }

    public static void initImperfectRituals()
    {
        imperfectNight = new ImperfectRitualNight();
        ImperfectRitualRegistry.registerRitual(imperfectNight);
        imperfectRain = new ImperfectRitualRain();
        ImperfectRitualRegistry.registerRitual(imperfectRain);
        imperfectResistance = new ImperfectRitualResistance();
        ImperfectRitualRegistry.registerRitual(imperfectResistance);
        imperfectZombie = new ImperfectRitualZombie();
        ImperfectRitualRegistry.registerRitual(imperfectZombie);
    }

    public static void initHarvestHandlers()
    {
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.diamond_block), 15);
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.gold_block), 10);
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.iron_block), 6);

        HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
        HarvestRegistry.registerHandler(new HarvestHandlerTall());
        HarvestRegistry.registerHandler(new HarvestHandlerStem());
    }
}
