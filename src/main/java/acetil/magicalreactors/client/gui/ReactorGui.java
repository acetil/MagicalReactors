package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.common.containers.ReactorContainer;
import acetil.magicalreactors.common.tiles.TileReactor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class ReactorGui extends GuiContainer {
    public static int WIDTH = 180;
    public static int HEIGHT = 152;

    //TODO add actual gui
    private static final ResourceLocation background = new ResourceLocation("minecraft",
            "textures/gui/container/generic_54.png");

    public ReactorGui (TileReactor te, ReactorContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = 114 + TileReactor.COLUMN_LENGTH *18; // from vanilla GuiChest.java
        System.out.println("Gui created");
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        //System.out.println("Drawing background");
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        //System.out.println("Drawing screen");
    }
}
