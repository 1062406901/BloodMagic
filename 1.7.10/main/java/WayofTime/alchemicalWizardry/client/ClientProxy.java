package WayofTime.alchemicalWizardry.client;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.*;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityParticleBeam;
import WayofTime.alchemicalWizardry.common.renderer.block.*;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.*;
import WayofTime.alchemicalWizardry.common.renderer.mob.*;
import WayofTime.alchemicalWizardry.common.renderer.model.*;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderMeteor;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.tileEntity.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    public static int renderPass;
    public static int altarRenderType;

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EnergyBlastProjectile.class, new RenderEnergyBlastProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityEnergyBazookaMainProjectile.class, new RenderEnergyBazookaMainProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderEnergyBlastProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityParticleBeam.class, new RenderEnergyBlastProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
        RenderingRegistry.registerEntityRenderingHandler(EntityFallenAngel.class, new RenderFallenAngel(new ModelFallenAngel(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityLowerGuardian.class, new RenderLowerGuardian(new ModelLowerGuardian(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBileDemon.class, new RenderBileDemon(new ModelBileDemon(), 1.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWingedFireDemon.class, new RenderWingedFireDemon(new ModelWingedFireDemon(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallEarthGolem.class, new RenderSmallEarthGolem(new ModelSmallEarthGolem(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIceDemon.class, new RenderIceDemon(new ModelIceDemon(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBoulderFist.class, new RenderBoulderFist(new ModelBoulderFist(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityShade.class, new RenderShade(new ModelShade(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityAirElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityEarthElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityShadeElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityHolyElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        ClientRegistry.bindTileEntitySpecialRenderer(TEAltar.class, new TEAltarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPedestal.class, new RenderPedestal());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPlinth.class, new RenderPlinth());
        ClientRegistry.bindTileEntitySpecialRenderer(TEWritingTable.class, new RenderWritingTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConduit.class, new RenderConduit());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellEffectBlock.class, new RenderSpellEffectBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellEnhancementBlock.class, new RenderSpellEnhancementBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellParadigmBlock.class, new RenderSpellParadigmBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellModifierBlock.class, new RenderSpellModifierBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TEReagentConduit.class, new RenderReagentConduit());
        ClientRegistry.bindTileEntitySpecialRenderer(TEMasterStone.class, new RenderMasterStone());
        ClientRegistry.bindTileEntitySpecialRenderer(TEAlchemicCalcinator.class, new RenderAlchemicCalcinator());
        ClientRegistry.bindTileEntitySpecialRenderer(TEBellJar.class, new RenderCrystalBelljar());

        //Item Renderer stuff
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockConduit), new TEConduitItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockSpellEffect), new TESpellEffectBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockSpellEnhancement), new TESpellEnhancementBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockSpellParadigm), new TESpellParadigmBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockSpellModifier), new TESpellModifierBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockAlchemicCalcinator), new TEAlchemicalCalcinatorItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockCrystalBelljar), new TEBellJarItemRenderer());
        ShaderHelper.initShaders();
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void InitRendering()
    {
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockAltar), new TEAltarItemRenderer());
    }

    @Override
    public void registerEvents()
    {
        FMLCommonHandler.instance().bus().register(new ClientEventHandler());
    }
}
