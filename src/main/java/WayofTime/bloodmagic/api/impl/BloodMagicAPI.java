package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.*;

public class BloodMagicAPI implements IBloodMagicAPI {

    public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

    private final BloodMagicBlacklist blacklist;
    private final BloodMagicRecipeRegistrar recipeRegistrar;
    private final Map<ResourceLocation, Integer> sacrificialValues;
    private final Multimap<EnumAltarComponent, IBlockState> altarComponents;

    public BloodMagicAPI() {
        this.blacklist = new BloodMagicBlacklist();
        this.recipeRegistrar = new BloodMagicRecipeRegistrar();
        this.sacrificialValues = Maps.newHashMap();
        this.altarComponents = ArrayListMultimap.create();
    }

    @Nonnull
    @Override
    public BloodMagicBlacklist getBlacklist() {
        return blacklist;
    }

    @Nonnull
    @Override
    public BloodMagicRecipeRegistrar getRecipeRegistrar() {
        return recipeRegistrar;
    }

    @Override
    public void setSacrificialValue(@Nonnull ResourceLocation entityId, int value) {
        sacrificialValues.put(entityId, value);
    }

    @Override
    public void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType) {
        EnumAltarComponent component = EnumAltarComponent.NOTAIR;
        for (EnumAltarComponent type : EnumAltarComponent.VALUES) {
            if (type.name().equalsIgnoreCase(componentType)) {
                component = type;
                break;
            }
        }

        altarComponents.put(component, state);
    }

    @Nonnull
    public Map<ResourceLocation, Integer> getSacrificialValues() {
        return ImmutableMap.copyOf(sacrificialValues);
    }

    @Nonnull
    public List<IBlockState> getComponentStates(EnumAltarComponent component) {
        return (List<IBlockState>) altarComponents.get(component);
    }
}
