package novamachina.exnihilosequentia.common.compat.waila;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import novamachina.exnihilosequentia.common.tileentity.InfestingLeavesTile;
import novamachina.exnihilosequentia.common.utility.StringUtils;

import javax.annotation.Nonnull;

public class InfestingLeavesComponentProvider implements IComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        @Nonnull final InfestingLeavesTile infestingLeavesTile = (InfestingLeavesTile) accessor.getBlockEntity();

        tooltip.add(new TranslatableComponent("waila.progress", StringUtils
                .formatPercent((float) infestingLeavesTile.getProgress() / 100)));
    }
}
