package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.common.containers.MachineContainer;
import acetil.magicalreactors.common.machines.TileMachineBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class MachineGui extends GuiContainer {
    //TODO MUST UPDATE: this copies the chest as a very very temp GUI for testing
    public static int WIDTH = 180;
    public static int HEIGHT = 152;

    /*private static final ResourceLocation background = new ResourceLocation("minecraft",
            "textures/gui/container/generic_54.png");*/
    private static final ResourceLocation background = new ResourceLocation("nuclearmod",
            "textures/gui/temp-centrifuge.png");
    public MachineGui (TileMachineBase te, MachineContainer mc) {
        super(mc);
        xSize = WIDTH;
        ySize = 114 + 6*18;
        System.out.println("ResourceLocation name: " + background.toString());
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
