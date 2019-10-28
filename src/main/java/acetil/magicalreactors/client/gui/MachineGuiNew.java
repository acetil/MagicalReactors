package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.client.gui.elements.IGuiElement;
import acetil.magicalreactors.client.gui.json.GuiElementFactory;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.client.gui.json.MachineGuiJson;
import acetil.magicalreactors.common.containers.MachineContainerNew;
import acetil.magicalreactors.common.machines.TileMachineBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
// TODO: remove MachineGui and replace with this
public class MachineGuiNew extends GuiContainer {
    private ResourceLocation background;
    private TileMachineBase te;
    private List<IGuiElement> guiElements;
    public MachineGuiNew(TileMachineBase te, MachineContainerNew container, MachineGuiJson json) {
        super(container);
        guiElements = new ArrayList<>();
        initGuiElements(json.guiElements);
        this.background = new ResourceLocation(json.background);
        xSize = json.width;
        ySize = json.height;
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        for (IGuiElement element : guiElements) {
            element.draw(this, te);
        }
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        //System.out.println("Drawing screen");
    }
    public void initGuiElements (List<GuiElementJson> elementJsons) {
        for (GuiElementJson json : elementJsons) {
            IGuiElement e = GuiElementFactory.getElement(json);
            if (e != null) {
                guiElements.add(e);
            }
        }
    }
}
