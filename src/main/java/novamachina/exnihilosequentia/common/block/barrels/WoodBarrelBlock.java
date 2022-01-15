package novamachina.exnihilosequentia.common.block.barrels;

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
import novamachina.exnihilosequentia.common.block.BlockBarrel;
import novamachina.exnihilosequentia.common.builder.BlockBuilder;
import novamachina.exnihilosequentia.common.tileentity.barrel.WoodBarrelTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WoodBarrelBlock extends BlockBarrel implements EntityBlock {
    public WoodBarrelBlock() {
        super(new BlockBuilder().properties(BlockBehaviour.Properties.of(Material.WOOD).strength(0.75F).sound(SoundType.WOOD)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new WoodBarrelTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        if (!level.isClientSide) {
            return (level1, blockPos, blockState, t) -> {
                if (t instanceof WoodBarrelTile tile) {
                    tile.tickServer();
                }
            };
        }
        return null;
    }
}
