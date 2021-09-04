package acetil.magicalreactors.client.gui.data;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public class GuiDataNamespaceTopLevel extends GuiDataNamespace<BlockEntity, BlockEntity> {
    public GuiDataNamespaceTopLevel () {
        super((BlockEntity te) -> te);
        setFullVariablePath("");
    }
    public Function<BlockEntity, ?> getSubValue (String varPath) {
        return getSubValueInternal(varPath, (BlockEntity te) -> te);
    }
}
