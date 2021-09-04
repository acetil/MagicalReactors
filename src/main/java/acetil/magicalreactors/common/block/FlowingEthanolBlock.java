package acetil.magicalreactors.common.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class FlowingEthanolBlock extends LiquidBlock {
    public FlowingEthanolBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, Properties.of(Material.WATER));
    }
}
