package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.machines.TileMachineBase;
import net.minecraft.tileentity.TileEntity;

public interface IGuiElement {
    IGuiElement applyJson (GuiElementJson json);
    void draw (ContainerGui gui, TileEntity te);
}
