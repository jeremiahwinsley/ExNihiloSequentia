package novamachina.exnihilosequentia.common.block.crucibles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import novamachina.exnihilosequentia.common.block.CrucibleBaseBlock;
import novamachina.exnihilosequentia.common.builder.BlockBuilder;
import novamachina.exnihilosequentia.common.tileentity.crucible.FiredCrucibleTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FiredCrucibleBlock extends CrucibleBaseBlock implements EntityBlock {
    public FiredCrucibleBlock() {
        super(new BlockBuilder().properties(
                        BlockBehaviour.Properties.of(Material.STONE).strength(1.5F)
                                .sound(SoundType.STONE).noOcclusion()));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new FiredCrucibleTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        if (!level.isClientSide) {
            return (level1, blockPos, blockState, t) -> {
                if (t instanceof FiredCrucibleTile tile) {
                    tile.tickServer();
                }
            };
        }
        return null;
    }
}
