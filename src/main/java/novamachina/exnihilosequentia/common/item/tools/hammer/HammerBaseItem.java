package novamachina.exnihilosequentia.common.item.tools.hammer;

import com.google.common.collect.Sets;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.common.init.ExNihiloBlocks;
import novamachina.exnihilosequentia.common.init.ExNihiloInitialization;

import java.util.Set;

public class HammerBaseItem extends DiggerItem {

    private static final Set<Block> effectiveBlocksOn = Sets
            .newHashSet(Blocks.STONE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRANITE,
                    Blocks.NETHERRACK, Blocks.END_STONE, Blocks.COBBLESTONE,
                    ExNihiloBlocks.CRUSHED_DIORITE.get(), ExNihiloBlocks.CRUSHED_ANDESITE.get(),
                    ExNihiloBlocks.CRUSHED_GRANITE.get(), ExNihiloBlocks.CRUSHED_NETHERRACK.get(),
                    ExNihiloBlocks.CRUSHED_END_STONE.get(), Blocks.GRAVEL, Blocks.SAND,
                    Blocks.TUBE_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK,
                    Blocks.FIRE_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK,
                    Blocks.TUBE_CORAL, Blocks.BRAIN_CORAL, Blocks.BUBBLE_CORAL,
                    Blocks.FIRE_CORAL, Blocks.HORN_CORAL);

    public HammerBaseItem(Tier tier, int maxDamage) {
        super(0.5F, 0.5F, tier, Tag.fromSet(effectiveBlocksOn),
                new Item.Properties().defaultDurability(maxDamage).tab(ExNihiloInitialization.ITEM_GROUP));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockIn) {
        if (ExNihiloRegistries.HAMMER_REGISTRY.isHammerable(blockIn.getBlock())) {
            return true;
        }
        return super.isCorrectToolForDrops(blockIn);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        if (itemStack.getItem() == EnumHammer.WOOD.getRegistryObject().get()) {
            return 200;
        }
        else return 0;
    }
}
