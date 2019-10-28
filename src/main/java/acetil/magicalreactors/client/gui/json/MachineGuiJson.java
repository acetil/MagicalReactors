package acetil.magicalreactors.client.gui.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineGuiJson {
    public String machine;
    public String background;
    public int width;
    public int height;
    public String container;
    @SerializedName("elements")
    public List<GuiElementJson> guiElements;
}
