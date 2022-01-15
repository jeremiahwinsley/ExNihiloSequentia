package novamachina.exnihilosequentia.common.registries;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import novamachina.exnihilosequentia.api.crafting.fluiditem.FluidItemRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import novamachina.exnihilosequentia.api.registry.IFluidItemTransformRegistry;
import novamachina.exnihilosequentia.common.utility.ExNihiloLogger;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidItemTransformRegistry implements IFluidItemTransformRegistry {
    @Nonnull private static final ExNihiloLogger logger = new ExNihiloLogger(LogManager.getLogger());

    @Nonnull private final List<FluidItemRecipe> recipeList = new ArrayList<>();

    @Nonnull private final Item empty = ItemStack.EMPTY.getItem();
    @Nonnull private final Map<FluidStack, Map<Item, Item>> itemResultCache = new HashMap<>();

    @Override
    public boolean isValidRecipe(@Nonnull final Fluid fluid, @Nonnull final Item input) {
        return getResult(fluid, input) != empty;
    }

    @Nonnull
    @Override
    public ItemLike getResult(@Nonnull final Fluid fluid, @Nonnull final Item input) {
        return itemResultCache
                .computeIfAbsent(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), k -> new HashMap<>())
                .computeIfAbsent(input, k -> recipeList
                        .stream()
                        .filter(fluidItemRecipe -> fluidItemRecipe.validInputs(fluid, input))
                        .findFirst()
                        .map(FluidItemRecipe::getResultItem)
                        .map(ItemStack::getItem)
                        .orElse(empty));
    }

    @Override
    @Nonnull
    public List<FluidItemRecipe> getRecipeList() {
        return recipeList;
    }

    @Override
    public void setRecipes(@Nonnull final List<FluidItemRecipe> recipes) {
        logger.debug("Fluid Item Transform Registry recipes: " + recipes.size());
        recipeList.addAll(recipes);

        itemResultCache.clear();
    }

    @Override
    public void clearRecipes() {
        recipeList.clear();

        itemResultCache.clear();
    }
}
