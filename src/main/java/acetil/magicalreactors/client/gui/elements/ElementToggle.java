package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.machines.TileMachineBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ElementToggle implements IGuiElement {
    private ResourceLocation texture;
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    @Override
    public IGuiElement applyJson(GuiElementJson json) {
        return null;
    }

    @Override
    public void draw(ContainerGui gui, TileEntity te) {

    }


}
