package novamachina.exnihilosequentia.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import novamachina.exnihilosequentia.common.block.InfestingLeavesBlock;
import novamachina.exnihilosequentia.common.init.ExNihiloTiles;
import novamachina.exnihilosequentia.common.utility.Config;
import net.minecraft.world.level.block.entity.BlockEntity;
import novamachina.exnihilosequentia.common.utility.ExNihiloLogger;
import org.apache.logging.log4j.LogManager;

public class InfestedLeavesTile extends BlockEntity {
    private static final ExNihiloLogger logger = new ExNihiloLogger(LogManager.getLogger());

    private int progressWaitInterval = 0;

    public InfestedLeavesTile(BlockPos pos, BlockState state) {
        super(ExNihiloTiles.INFESTED_LEAVES.get(), pos, state);
    }

    public void tick() {
        if (!level.isClientSide()) {
            progressWaitInterval++;
            if (progressWaitInterval >= Config.getTicksBetweenSpreadAttempt()) {
                logger.debug("Spreading infested leaves");

                progressWaitInterval = 0;
                InfestingLeavesBlock.spread(level, worldPosition);
            }
        }
    }
}
