package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicItems;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need
 * custom handling, you will need your own item class.
 */
@Deprecated
public class OrbRegistry
{
    private static List<BloodOrb> orbs = new ArrayList<BloodOrb>();
    public static ArrayListMultimap<Integer, ItemStack> tierMap = ArrayListMultimap.create();

    @GameRegistry.ObjectHolder("bloodmagic:blood_orb")
    private static final Item ORB_ITEM = null;

    public static List<ItemStack> getOrbsForTier(int tier)
    {
        if (getTierMap().containsKey(tier))
            return getTierMap().get(tier);

        return Collections.emptyList();
    }

    public static List<ItemStack> getOrbsUpToTier(int tier)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = 1; i <= tier; i++)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static List<ItemStack> getOrbsDownToTier(int tier)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = EnumAltarTier.MAXTIERS; i >= tier; i--)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static ItemStack getOrbStack(BloodOrb orb)
    {
        ItemStack ret = new ItemStack(ORB_ITEM);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("orb", orb.getRegistryName().toString());
        ret.setTagCompound(tag);
        return ret;
    }

    public static ArrayListMultimap<Integer, ItemStack> getTierMap()
    {
        return ArrayListMultimap.create(tierMap);
    }
}
