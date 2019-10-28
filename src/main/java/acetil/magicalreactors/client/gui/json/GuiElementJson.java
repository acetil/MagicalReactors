package acetil.magicalreactors.client.gui.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class GuiElementJson {
    public String type;
    public String name;
    @SerializedName("texture_name")
    public String texture;
    public int x;
    public int y;
    public int width;
    public int height;
    public List<ElementTextureJson> textures;
    public Map<String, String> data;
}
