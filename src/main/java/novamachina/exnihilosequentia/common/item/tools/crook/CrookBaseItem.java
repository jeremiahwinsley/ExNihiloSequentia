package novamachina.exnihilosequentia.common.item.tools.crook;

import com.google.common.collect.Sets;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;
import novamachina.exnihilosequentia.api.ExNihiloTags;
import novamachina.exnihilosequentia.common.init.ExNihiloInitialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class CrookBaseItem extends DiggerItem {

    @Nonnull private static final Set<Material> effectiveMaterialsOn = Sets.newHashSet(Material.LEAVES);

    public CrookBaseItem(@Nonnull final Tier tier, final int maxDamage) {
        super(0.5F, 0.5F, tier, ExNihiloTags.MINEABLE_WITH_CROOK,
                new Item.Properties().defaultDurability(maxDamage)
                        .tab(ExNihiloInitialization.ITEM_GROUP));
    }

    @Override
    public float getDestroySpeed(@Nonnull final ItemStack stack, @Nonnull final BlockState state) {
        @Nonnull final Material material = state.getMaterial();
        return effectiveMaterialsOn.contains(material) ? this.speed : super.getDestroySpeed(stack, state);
    }

    @Override
    public int getBurnTime(@Nonnull final ItemStack itemStack, @Nullable final RecipeType<?> recipeType) {
        @Nullable final RegistryObject<Item> woodRegistryObject = EnumCrook.WOOD.getRegistryObject();
        if (woodRegistryObject != null && itemStack.getItem() == woodRegistryObject.get()) {
            return 200;
        }
        else return 0;
    }
}
