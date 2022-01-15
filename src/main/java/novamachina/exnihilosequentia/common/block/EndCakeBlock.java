package novamachina.exnihilosequentia.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import novamachina.exnihilosequentia.common.builder.BlockBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EndCakeBlock extends CakeBlock {

    public EndCakeBlock() {
        super(new BlockBuilder().getProperties().strength(0.5F));
    }


    /**
     * @deprecated Ask Mojang
     */
    @Nonnull
    @Deprecated
    @Override
    public InteractionResult use(@Nonnull final BlockState state, @Nonnull final Level worldIn,
                                @Nonnull final BlockPos pos, @Nonnull final Player player,
                                @Nonnull final InteractionHand handIn, @Nonnull final BlockHitResult blockRayTraceResult) {
        @Nonnull final ItemStack itemStack = player.getItemInHand(handIn);

        if (itemStack.isEmpty()) {
            return eatCake(worldIn, pos, state, player);
        } else {
            final int bites = state.getValue(BITES);

            if (itemStack.getItem() == Items.ENDER_EYE && bites > 0) {
                if (!worldIn.isClientSide()) {
                    worldIn.setBlockAndUpdate(pos, state.setValue(BITES, bites - 1));
                    itemStack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    private InteractionResult eatCake(@Nonnull final Level worldIn, @Nonnull final BlockPos pos,
                                     @Nonnull final BlockState state, @Nonnull final Player player) {
        if (!worldIn.isClientSide() && player.getVehicle() == null && player
                .isCreative() && worldIn instanceof ServerLevel && !player.isPassenger()) {
            @Nonnull final ResourceKey<Level> registrykey = worldIn
                    .dimension() == Level.OVERWORLD ? Level.END : Level.OVERWORLD;
            @Nullable final ServerLevel serverworld = ((ServerLevel) worldIn).getServer().getLevel(registrykey);
            if (serverworld == null) {
                return InteractionResult.FAIL;
            }

            player.changeDimension(serverworld);
        }

        if (!player.canEat(true) || player.getCommandSenderWorld().dimension() == Level.END) {
            return InteractionResult.FAIL;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(2, 0.1F);
            int i = state.getValue(BITES);

            if (i < 6) {
                worldIn.setBlockAndUpdate(pos, state.setValue(BITES, i + 1));
            } else {
                worldIn.removeBlock(pos, false);
            }

            if (!worldIn.isClientSide() && player.getVehicle() == null && worldIn instanceof ServerLevel && !player
                    .isPassenger()) {
                @Nonnull final ResourceKey<Level> registrykey = worldIn
                        .dimension() == Level.END ? Level.OVERWORLD : Level.END;
                @Nullable final ServerLevel serverworld = ((ServerLevel) worldIn).getServer().getLevel(registrykey);
                if (serverworld == null) {
                    return InteractionResult.FAIL;
                }

                player.changeDimension(serverworld);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
