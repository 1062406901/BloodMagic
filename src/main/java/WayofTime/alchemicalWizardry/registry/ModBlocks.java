package WayofTime.alchemicalWizardry.registry;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ConfigHandler;
import WayofTime.alchemicalWizardry.block.BlockAltar;
import WayofTime.alchemicalWizardry.block.BlockLifeEssence;
import WayofTime.alchemicalWizardry.block.BlockRitualHome;
import WayofTime.alchemicalWizardry.item.block.ItemBlockRitualHome;
import WayofTime.alchemicalWizardry.util.helper.InventoryRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block altar;
    public static Block ritualStone;

    public static Block lifeEssence;

    public static Block crystal;
    public static Block rune;
    public static Block bloodStone;

    public static void init() {
        FluidRegistry.registerFluid(BlockLifeEssence.getLifeEssence());
        lifeEssence = registerBlock(new BlockLifeEssence());

        altar = registerBlock(new BlockAltar());
        ritualStone = registerBlock(new BlockRitualHome(), ItemBlockRitualHome.class);

        initTiles();
    }

    public static void initTiles() {

    }

    public static void initRenders() {
        InventoryRenderHelper renderHelper = AlchemicalWizardry.instance.getRenderHelper();

        renderHelper.fluidRender(lifeEssence);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 0);
        renderHelper.itemRender(InventoryRenderHelper.getItemFromBlock(ritualStone), 1);
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name) {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, itemBlock, name);

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
        return registerBlock(block, itemBlock, block.getClass().getSimpleName());
    }

    private static Block registerBlock(Block block, String name) {
        if (!ConfigHandler.blockBlacklist.contains(name))
            GameRegistry.registerBlock(block, name);

        return block;
    }

    private static Block registerBlock(Block block) {
        return registerBlock(block, block.getClass().getSimpleName());
    }
}
