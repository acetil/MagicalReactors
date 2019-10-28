package acetil.magicalreactors.client.gui.elements;

import net.minecraft.client.gui.inventory.GuiContainer;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.machines.TileMachineBase;

public interface IGuiElement {
    IGuiElement applyJson (GuiElementJson json);
    void draw (GuiContainer gui, TileMachineBase te);
}
