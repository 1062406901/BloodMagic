package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapedBloodOrbRecipeRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapelessBloodOrbRecipeRenderer;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageIRecipe;

public class CategoryArchitect
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.architect.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 270));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), false));

        List<IPage> altarPages = new ArrayList<IPage>();

        IRecipe altarRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.altar));
        if (altarRecipe != null)
        {
            altarPages.add(new PageIRecipe(altarRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.1"), 270));

        IRecipe daggerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.sacrificialDagger));
        if (daggerRecipe != null)
        {
            altarPages.add(new PageIRecipe(daggerRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "bloodaltar"), new EntryText(altarPages, TextHelper.localize(keyBase + "bloodaltar"), false));

        List<IPage> ashPages = new ArrayList<IPage>();
        //TODO: Arcane Ash Recipe

        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), false));

        List<IPage> divinationPages = new ArrayList<IPage>();
        //TODO: Divination Sigil Recipe
        divinationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "divination" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "divination"), new EntryText(divinationPages, TextHelper.localize(keyBase + "divination"), false));

        List<IPage> soulnetworkPages = new ArrayList<IPage>();

        soulnetworkPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "soulnetwork" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "soulnetwork"), new EntryText(soulnetworkPages, TextHelper.localize(keyBase + "soulnetwork"), false));

        List<IPage> weakorbPages = new ArrayList<IPage>();
        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.1"), 270));

        AltarRecipe weakorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbWeak));
        if (weakorbRecipe != null)
        {
            weakorbPages.add(new PageAltarRecipe(weakorbRecipe));
        }

        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "weakorb"), new EntryText(weakorbPages, TextHelper.localize(keyBase + "weakorb"), false));

        List<IPage> incensePages = new ArrayList<IPage>();

        IRecipe incenseRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.incenseAltar));
        if (incenseRecipe != null)
        {
            incensePages.add(getPageForRecipe(incenseRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.1"), 270));

        IRecipe woodPathRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.pathBlock, 1, 0));
        if (woodPathRecipe != null)
        {
            incensePages.add(getPageForRecipe(woodPathRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "incense"), new EntryText(incensePages, TextHelper.localize(keyBase + "incense"), false));

        List<IPage> runePages = new ArrayList<IPage>();

        IRecipe runeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 0));
        if (runeRecipe != null)
        {
            runePages.add(getPageForRecipe(runeRecipe));
        }

        runePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodrune" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "bloodrune"), new EntryText(runePages, TextHelper.localize(keyBase + "bloodrune"), false));

        List<IPage> inspectPages = new ArrayList<IPage>();

        AltarRecipe inspectRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.sanguineBook));
        if (inspectRecipe != null)
        {
            inspectPages.add(new PageAltarRecipe(inspectRecipe));
        }

        inspectPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "inspectoris" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "inspectoris"), new EntryText(inspectPages, TextHelper.localize(keyBase + "inspectoris"), false));

        List<IPage> speedRunePages = new ArrayList<IPage>();

        IRecipe speedRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 1));
        if (speedRecipe != null)
        {
            speedRunePages.add(getPageForRecipe(speedRecipe));
        }

        speedRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSpeed" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "runeSpeed"), new EntryText(speedRunePages, TextHelper.localize(keyBase + "runeSpeed"), false));

        List<IPage> waterPages = new ArrayList<IPage>();

        TartaricForgeRecipe waterRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_WATER));
        if (waterRecipe != null)
        {
            waterPages.add(new PageTartaricForgeRecipe(waterRecipe));
        }

        waterPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "water" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "water"), new EntryText(waterPages, TextHelper.localize(keyBase + "water"), false));

        List<IPage> lavaPages = new ArrayList<IPage>();

        TartaricForgeRecipe lavaRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_LAVA));
        if (lavaRecipe != null)
        {
            lavaPages.add(new PageTartaricForgeRecipe(lavaRecipe));
        }

        lavaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lava" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "lava"), new EntryText(lavaPages, TextHelper.localize(keyBase + "lava"), false));

        List<IPage> lavaCrystalPages = new ArrayList<IPage>();

        IRecipe lavaCrystalRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.lavaCrystal));
        if (lavaCrystalRecipe != null)
        {
            lavaCrystalPages.add(getPageForRecipe(lavaCrystalRecipe));
        }

        lavaCrystalPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lavaCrystal" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "lavaCrystal"), new EntryText(lavaCrystalPages, TextHelper.localize(keyBase + "lavaCrystal"), false));

        List<IPage> apprenticeorbPages = new ArrayList<IPage>();

        AltarRecipe apprenticeorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbApprentice));
        if (apprenticeorbRecipe != null)
        {
            apprenticeorbPages.add(new PageAltarRecipe(apprenticeorbRecipe));
        }

        apprenticeorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "apprenticeorb" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "apprenticeorb"), new EntryText(apprenticeorbPages, TextHelper.localize(keyBase + "apprenticeorb"), false));

        List<IPage> daggerPages = new ArrayList<IPage>();

        AltarRecipe daggerOfSacrificeRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.daggerOfSacrifice));
        if (daggerOfSacrificeRecipe != null)
        {
            daggerPages.add(new PageAltarRecipe(daggerOfSacrificeRecipe));
        }

        daggerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "dagger" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "dagger"), new EntryText(daggerPages, TextHelper.localize(keyBase + "dagger"), false));

        List<IPage> runeSacrificePages = new ArrayList<IPage>();

        IRecipe runeSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 3));
        if (runeSacrificeRecipe != null)
        {
            runeSacrificePages.add(getPageForRecipe(runeSacrificeRecipe));
        }

        runeSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSacrifice" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "runeSacrifice"), new EntryText(runeSacrificePages, TextHelper.localize(keyBase + "runeSacrifice"), false));

        List<IPage> runeSelfSacrificePages = new ArrayList<IPage>();

        IRecipe runeSelfSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (runeSelfSacrificeRecipe != null)
        {
            runeSelfSacrificePages.add(getPageForRecipe(runeSelfSacrificeRecipe));
        }

        runeSelfSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSelfSacrifice" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "runeSelfSacrifice"), new EntryText(runeSelfSacrificePages, TextHelper.localize(keyBase + "runeSelfSacrifice"), false));

        List<IPage> holdingPages = new ArrayList<IPage>();

        TartaricForgeRecipe holdingRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING));
        if (holdingRecipe != null)
        {
            holdingPages.add(new PageTartaricForgeRecipe(holdingRecipe));
        }

        holdingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "holding" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "holding"), new EntryText(holdingPages, TextHelper.localize(keyBase + "holding"), false));

        List<IPage> airPages = new ArrayList<IPage>();

        TartaricForgeRecipe airRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_AIR));
        if (airRecipe != null)
        {
            airPages.add(new PageTartaricForgeRecipe(airRecipe));
        }

        airPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "air" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "air"), new EntryText(airPages, TextHelper.localize(keyBase + "air"), false));

        List<IPage> voidPages = new ArrayList<IPage>();

        TartaricForgeRecipe voidRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_VOID));
        if (voidRecipe != null)
        {
            voidPages.add(new PageTartaricForgeRecipe(voidRecipe));
        }

        voidPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "void" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "void"), new EntryText(voidPages, TextHelper.localize(keyBase + "void"), false));

        List<IPage> greenGrovePages = new ArrayList<IPage>();

        TartaricForgeRecipe greenGroveRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH));
        if (greenGroveRecipe != null)
        {
            greenGrovePages.add(new PageTartaricForgeRecipe(greenGroveRecipe));
        }

        greenGrovePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "greenGrove" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "greenGrove"), new EntryText(greenGrovePages, TextHelper.localize(keyBase + "greenGrove"), false));

        List<IPage> fastMinerPages = new ArrayList<IPage>();

        TartaricForgeRecipe fastMinerRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER));
        if (fastMinerRecipe != null)
        {
            fastMinerPages.add(new PageTartaricForgeRecipe(fastMinerRecipe));
        }

        fastMinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "fastMiner" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "fastMiner"), new EntryText(fastMinerPages, TextHelper.localize(keyBase + "fastMiner"), false));

        List<IPage> seerPages = new ArrayList<IPage>();

        TartaricForgeRecipe seerRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT));
        if (seerRecipe != null)
        {
            seerPages.add(new PageTartaricForgeRecipe(seerRecipe));
        }

        seerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "seer" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "seer"), new EntryText(seerPages, TextHelper.localize(keyBase + "seer"), false));

        List<IPage> magicianOrbPages = new ArrayList<IPage>();

        AltarRecipe magicianOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbMagician));
        if (magicianOrbRecipe != null)
        {
            magicianOrbPages.add(new PageAltarRecipe(magicianOrbRecipe));
        }

        magicianOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magicianOrb" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "magicianOrb"), new EntryText(magicianOrbPages, TextHelper.localize(keyBase + "magicianOrb"), false));

        List<IPage> capacityPages = new ArrayList<IPage>();

        IRecipe capacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (capacityRecipe != null)
        {
            capacityPages.add(getPageForRecipe(capacityRecipe));
        }

        capacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "capacity" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "capacity"), new EntryText(capacityPages, TextHelper.localize(keyBase + "capacity"), false));

        List<IPage> displacementPages = new ArrayList<IPage>();

        IRecipe displacementRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (displacementRecipe != null)
        {
            displacementPages.add(getPageForRecipe(displacementRecipe));
        }

        displacementPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "displacement" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "displacement"), new EntryText(displacementPages, TextHelper.localize(keyBase + "displacement"), false));

        List<IPage> affinityPages = new ArrayList<IPage>();

        TartaricForgeRecipe affinityRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY));
        if (affinityRecipe != null)
        {
            affinityPages.add(new PageTartaricForgeRecipe(affinityRecipe));
        }

        affinityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "affinity" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "affinity"), new EntryText(affinityPages, TextHelper.localize(keyBase + "affinity"), false));

        List<IPage> lampPages = new ArrayList<IPage>();

        TartaricForgeRecipe lampRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT));
        if (lampRecipe != null)
        {
            lampPages.add(new PageTartaricForgeRecipe(lampRecipe));
        }

        lampPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lamp" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "lamp"), new EntryText(lampPages, TextHelper.localize(keyBase + "lamp"), false));

        List<IPage> magnetismPages = new ArrayList<IPage>();

        TartaricForgeRecipe magnetismRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM));
        if (magnetismRecipe != null)
        {
            magnetismPages.add(new PageTartaricForgeRecipe(magnetismRecipe));
        }

        magnetismPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magnetism" + ".info.1"), 270));
        entries.put(new ResourceLocation(keyBase + "magnetism"), new EntryText(magnetismPages, TextHelper.localize(keyBase + "magnetism"), false));

        return entries;
    }

    public static PageIRecipe getPageForRecipe(IRecipe recipe)
    {
        if (recipe instanceof ShapedBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapedBloodOrbRecipeRenderer((ShapedBloodOrbRecipe) recipe));
        } else if (recipe instanceof ShapelessBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapelessBloodOrbRecipeRenderer((ShapelessBloodOrbRecipe) recipe));
        }

        return new PageIRecipe(recipe);
    }
}
