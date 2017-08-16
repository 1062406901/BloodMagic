package WayofTime.bloodmagic;

import java.io.File;
import java.util.List;

import WayofTime.bloodmagic.api_impl.BloodMagicAPI;
import WayofTime.bloodmagic.apiv2.BloodMagicPlugin;
import WayofTime.bloodmagic.apiv2.IBloodMagicPlugin;
import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.meteor.MeteorConfigHandler;
import WayofTime.bloodmagic.util.PluginUtil;
import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.util.helper.LogHelper;
import WayofTime.bloodmagic.client.gui.GuiHandler;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.proxy.CommonProxy;
import WayofTime.bloodmagic.registry.ModArmourTrackers;
import WayofTime.bloodmagic.registry.ModCorruptionBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.registry.ModRecipes;
import WayofTime.bloodmagic.registry.ModRituals;
import WayofTime.bloodmagic.registry.ModTranquilityHandlers;
import WayofTime.bloodmagic.structures.ModDungeons;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.handler.IMCHandler;
import org.apache.commons.lang3.tuple.Pair;

@Mod(modid = BloodMagic.MODID, name = BloodMagic.NAME, version = BloodMagic.VERSION, dependencies = BloodMagic.DEPEND, guiFactory = "WayofTime.bloodmagic.client.gui.config.ConfigGuiFactory")
public class BloodMagic
{
    public static final String MODID = "bloodmagic";
    public static final String NAME = "Blood Magic: Alchemical Wizardry";
    public static final String VERSION = "@VERSION@";
    public static final String DEPEND = "required-after:guideapi;";
    public static final CreativeTabs TAB_BM = new CreativeTabs(MODID + ".creativeTab")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RegistrarBloodMagicItems.BLOOD_ORB);
        }
    };
    public static CreativeTabs TAB_TOMES = new CreativeTabs(MODID + ".creativeTabTome")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
        }

        @Override
        public boolean hasSearchBar()
        {
            return true;
        }
    }.setNoTitle().setBackgroundImageName("item_search.png");
    public static final boolean IS_DEV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final List<Pair<IBloodMagicPlugin, BloodMagicPlugin>> PLUGINS = Lists.newArrayList();

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @SidedProxy(serverSide = "WayofTime.bloodmagic.proxy.CommonProxy", clientSide = "WayofTime.bloodmagic.proxy.ClientProxy")
    public static CommonProxy proxy;
    @Mod.Instance(BloodMagic.MODID)
    public static BloodMagic instance;

    public LogHelper logger = new LogHelper(MODID);
    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configDir = new File(event.getModConfigurationDirectory(), "BloodMagic");
        ConfigHandler.init(new File(configDir, "BloodMagic.cfg"));

        PLUGINS.addAll(PluginUtil.getPlugins(event.getAsmData()));

        ModTranquilityHandlers.init();
        ModDungeons.init();

        Utils.registerHandlers(event.getAsmData().getAll(Handler.class.getCanonicalName()));
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        BloodMagicPacketHandler.init();
        for (Pair<IBloodMagicPlugin, BloodMagicPlugin> plugin : PLUGINS)
            plugin.getLeft().register(BloodMagicAPI.INSTANCE);

        ModRecipes.init();
        ModRituals.initRituals();
        ModRituals.initImperfectRituals();
        MeteorConfigHandler.init(new File(configDir, "meteors"));
        ModArmourTrackers.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(BloodMagic.instance, new GuiHandler());
        ModCorruptionBlocks.init();

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModRecipes.addCompressionHandlers();

        proxy.postInit();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        RitualRegistry.orderLookupList();
    }

    @Mod.EventHandler
    public void modMapping(FMLModIdMappingEvent event)
    {

    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandBloodMagic());
    }

    @Mod.EventHandler
    public void onIMCRecieved(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.handleIMC(event);
    }
}
