package novamachina.exnihilosequentia.common.tileentity.barrel.mode;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.common.tileentity.barrel.AbstractBarrelTile;
import novamachina.exnihilosequentia.common.utility.Config;
import novamachina.exnihilosequentia.common.utility.ExNihiloConstants;
import novamachina.exnihilosequentia.common.utility.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CompostBarrelMode extends AbstractBarrelMode {
    private int currentProgress;

    public CompostBarrelMode(@Nonnull final String name) {
        super(name);
        currentProgress = 0;
    }

    @Override
    public void tick(@Nonnull final AbstractBarrelTile barrelTile) {
        if (barrelTile.getSolidAmount() >= AbstractBarrelTile.MAX_SOLID_AMOUNT && barrelTile.getInventory()
                .getStackInSlot(0)
                .isEmpty()) {
            currentProgress++;
            spawnParticle(barrelTile);
            if (currentProgress >= Config.getSecondsToCompost() * 20) {
                currentProgress = 0;
                barrelTile.getInventory()
                        .setStackInSlot(0, new ItemStack(ForgeRegistries.BLOCKS.getValue(Blocks.DIRT.getRegistryName())));
                barrelTile.removeSolid(barrelTile.getSolidAmount());
                barrelTile.setMode(ExNihiloConstants.BarrelModes.BLOCK);
            }
        }
    }

    @Override
    @Nonnull
    public InteractionResult onBlockActivated(@Nonnull final AbstractBarrelTile barrelTile,
                                             @Nonnull final Player player, @Nonnull final InteractionHand handIn,
                                             @Nonnull final IFluidHandler fluidHandler,
                                             @Nonnull final IItemHandler itemHandler) {
        if (ExNihiloRegistries.COMPOST_REGISTRY.containsSolid(player.getItemInHand(handIn).getItem()) && barrelTile
                .addSolid(ExNihiloRegistries.COMPOST_REGISTRY.getSolidAmount(player.getItemInHand(handIn).getItem()), false)) {
            player.getItemInHand(handIn).shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canFillWithFluid(@Nonnull final AbstractBarrelTile barrel) {
        return false;
    }

    @Override
    public boolean isEmptyMode() {
        return false;
    }

    @Override
    protected boolean isTriggerItem(@Nonnull final ItemStack stack) {
        return ExNihiloRegistries.COMPOST_REGISTRY.containsSolid(stack.getItem());
    }

    @Override
    public void read(@Nonnull final CompoundTag nbt) {
        this.currentProgress = nbt.getInt("currentProgress");
    }

    @Override
    @Nonnull
    public CompoundTag write() {
        @Nonnull final CompoundTag modeInfo = new CompoundTag();
        modeInfo.putInt("currentProgress", currentProgress);
        return modeInfo;
    }

    @Override
    protected void spawnParticle(@Nonnull final AbstractBarrelTile barrelTile) throws NullPointerException {
        if (Config.getShowParticles()) {
            @Nullable final ServerLevel level = (ServerLevel) barrelTile.getLevel();
            Preconditions.checkNotNull(level, "Level is null.");
            level.sendParticles(ParticleTypes.ASH,
                    barrelTile.getBlockPos().getX() + (.2d + (.8d - .2d) * level.random.nextDouble()),
                    barrelTile.getBlockPos().getY() + 1.2d,
                    barrelTile.getBlockPos().getZ() + (.2d + (.8d - .2d) * level.random.nextDouble()),
                    1,
                    0,
                    0,
                    0,
                    0.01);
        }
    }

    @Override
    @Nonnull
    public List<Component> getWailaInfo(@Nonnull final AbstractBarrelTile barrelTile) {
        @Nonnull final List<Component> info = new ArrayList<>();
        if (currentProgress <= 0) {
            info.add(new TranslatableComponent("waila.barrel.compost", barrelTile
                    .getSolidAmount(), AbstractBarrelTile.MAX_SOLID_AMOUNT));
        } else {
            info.add(new TranslatableComponent("waila.progress", StringUtils
                    .formatPercent((float) currentProgress / (Config.getSecondsToCompost() * 20))));
        }
        return info;
    }

    @Override
    @Nonnull
    public ItemStack handleInsert(@Nonnull final AbstractBarrelTile barrelTile, @Nonnull final ItemStack stack,
                                  final boolean simulate) {
        if (barrelTile.addSolid(ExNihiloRegistries.COMPOST_REGISTRY.getSolidAmount(stack.getItem()), simulate)) {
            @Nonnull final ItemStack returnStack = stack.copy();
            returnStack.shrink(1);
            return returnStack;
        }
        return stack;
    }
}
