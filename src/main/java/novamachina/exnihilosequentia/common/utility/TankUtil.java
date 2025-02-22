package novamachina.exnihilosequentia.common.utility;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankUtil {

  private static final ItemStack WATER_BOTTLE;

  static {
    WATER_BOTTLE = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
  }

  private TankUtil() {
  }

  public static boolean drainWaterFromBottle(@Nonnull final BlockEntity tileEntity,
      @Nonnull final Player player,
      @Nonnull final IFluidHandler tank) {
    int waterAmount = 333;
    if (tank.getFluidInTank(0).getAmount() % FluidAttributes.BUCKET_VOLUME == 666) {
      waterAmount = 334;
    }
    if (player.getMainHandItem().getItem() == Items.POTION
        && WATER_BOTTLE.getTag() != null
        && WATER_BOTTLE.getTag().equals(player.getMainHandItem().getTag())) {
      FluidStack water = new FluidStack(Fluids.WATER, waterAmount);

      if (tank.fill(water, IFluidHandler.FluidAction.SIMULATE) == water.getAmount()
          && player.addItem(new ItemStack(Items.GLASS_BOTTLE))) {

        if (!player.isCreative()) {
          player.getMainHandItem().shrink(1);
        }
        tank.fill(water, IFluidHandler.FluidAction.EXECUTE);

        tileEntity.setChanged();
        return true;
      }
    }

    return false;
  }

  // TODO: Figure out how to completely fill and drain
  public static boolean drainWaterIntoBottle(@Nonnull final BlockEntity tileEntity,
      @Nonnull final Player player,
      @Nonnull final IFluidHandler tank) {
    int waterAmount = 333;
    if (tank.getFluidInTank(0).getAmount() % FluidAttributes.BUCKET_VOLUME == 334) {
      waterAmount = 334;
    }
    if (player.getMainHandItem().getItem() == Items.GLASS_BOTTLE
        && tank.getFluidInTank(0).getFluid() != null
        && tank.getFluidInTank(0).getAmount() >= waterAmount
        && tank.getFluidInTank(0).getFluid() == Fluids.WATER
        && player.addItem(WATER_BOTTLE.copy())) {
      if (!player.isCreative()) {
        player.getMainHandItem().shrink(1);
      }
      tank.drain(waterAmount, IFluidHandler.FluidAction.EXECUTE);
      tileEntity.setChanged();
      return true;
    }
    return false;
  }
}
