package acetil.magicalreactors.client.gui.json;

import acetil.magicalreactors.client.gui.elements.ElementEnergyStorage;
import acetil.magicalreactors.client.gui.elements.ElementProgressBar;
import acetil.magicalreactors.client.gui.elements.IGuiElement;

public class GuiElementFactory {
    public static IGuiElement getElement (GuiElementJson json) {
        if (json.type.equals("progress")) {
            return new ElementProgressBar().applyJson(json);
        } else if (json.type.equals("energy")) {
            return new ElementEnergyStorage().applyJson(json);
        }
        return null;
    }
}
