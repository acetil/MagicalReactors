package acetil.magicalreactors.client.gui.data;

import net.minecraft.tileentity.TileEntity;

import java.util.function.Function;

public class GuiDataNamespaceTopLevel extends GuiDataNamespace<TileEntity, TileEntity> {
    public GuiDataNamespaceTopLevel () {
        super((TileEntity te) -> te);
        setFullVariablePath("");
    }
    public Function<TileEntity, ?> getSubValue (String varPath) {
        return getSubValueInternal(varPath, (TileEntity te) -> te);
    }
}
