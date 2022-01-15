package novamachina.exnihilosequentia.common.registries;

import net.minecraft.world.level.ItemLike;
import novamachina.exnihilosequentia.api.crafting.fluidtransform.FluidTransformRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import novamachina.exnihilosequentia.api.registry.IFluidTransformRegistry;
import novamachina.exnihilosequentia.common.utility.ExNihiloLogger;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidTransformRegistry implements IFluidTransformRegistry {
    @Nonnull private static final ExNihiloLogger logger = new ExNihiloLogger(LogManager.getLogger());

    @Nonnull private final List<FluidTransformRecipe> recipeList = new ArrayList<>();

    @Nonnull private final Map<FluidStack, Map<ItemLike, Fluid>> fluidResultCache = new HashMap<>();

    @Override
    public boolean isValidRecipe(@Nonnull final Fluid fluidInTank, @Nonnull final ItemLike catalyst) {
        return getResult(fluidInTank, catalyst) != Fluids.EMPTY;
    }

    @Override
    @Nonnull
    public Fluid getResult(@Nonnull final Fluid fluidInTank, @Nonnull final ItemLike catalyst) {
        final FluidStack fluidStack = new FluidStack(fluidInTank, FluidAttributes.BUCKET_VOLUME);
        return fluidResultCache
                .computeIfAbsent(fluidStack, k -> new HashMap<>())
                .computeIfAbsent(catalyst.asItem(), k -> {
                    @Nonnull final ItemStack itemStack = new ItemStack(catalyst);
                    return recipeList
                            .stream()
                            .filter(fluidTransformRecipe -> fluidTransformRecipe.getFluidInTank().isFluidEqual(fluidStack))
                            .filter(fluidTransformRecipe -> fluidTransformRecipe.getCatalyst().test(itemStack))
                            .findFirst()
                            .map(FluidTransformRecipe::getResult)
                            .map(FluidStack::getFluid)
                            .orElse(Fluids.EMPTY);
                });
    }

    @Override
    @Nonnull
    public List<FluidTransformRecipe> getRecipeList() {
        return recipeList;
    }

    @Override
    public void setRecipes(@Nonnull final List<FluidTransformRecipe> recipes) {
        logger.debug("Fluid Transform Registry recipes: " + recipes.size());
        recipeList.addAll(recipes);

        fluidResultCache.clear();
    }

    @Override
    public void clearRecipes() {
        recipeList.clear();

        fluidResultCache.clear();
    }
}
