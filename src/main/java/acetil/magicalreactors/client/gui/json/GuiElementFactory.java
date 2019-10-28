package acetil.magicalreactors.client.gui.json;

import acetil.magicalreactors.client.gui.elements.ElementProgressBar;
import acetil.magicalreactors.client.gui.elements.IGuiElement;

public class GuiElementFactory {
    public static IGuiElement getElement (GuiElementJson json) {
        if (json.type.equals("progress")) {
            return new ElementProgressBar().applyJson(json);
        }
        return null;
    }
}
