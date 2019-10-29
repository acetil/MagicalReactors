package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.client.gui.elements.IGuiElement;
import acetil.magicalreactors.client.gui.json.GuiElementFactory;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.client.gui.json.MachineGuiJson;
import acetil.magicalreactors.common.containers.GuiContainer;
import acetil.magicalreactors.common.containers.GuiContainerFactory;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ContainerGui extends ContainerScreen<GuiContainer> {
    private List<IGuiElement> guiElements;
    private TileEntity tileEntity;
    private ResourceLocation background;
    private ITextComponent name;
    private int width;
    private int height;
    public ContainerGui (GuiContainer container, PlayerInventory inv, ITextComponent name, MachineGuiJson json) {
        super(container, inv, name);
        tileEntity = inv.player.world.getTileEntity(container.getTileEntityPosition());
        guiElements = new ArrayList<>();
        for (GuiElementJson elementJson : json.guiElements) {
            guiElements.add(GuiElementFactory.getElement(elementJson));
        }
        background = new ResourceLocation(json.background);
        this.name = name;
        width = json.width;
        height = json.height;
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(background);
        blit(guiLeft, guiTop, 0, 0, width, height);
        for (IGuiElement element : guiElements) {
            element.draw(this, tileEntity);
        }
    }
    public ITextComponent getName () {
        return name;
    }
}
