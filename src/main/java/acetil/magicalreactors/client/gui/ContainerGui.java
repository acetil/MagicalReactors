package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.client.gui.elements.IGuiElement;
import acetil.magicalreactors.client.gui.json.GuiElementFactory;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.client.gui.json.MachineGuiJson;
import acetil.magicalreactors.common.containers.GuiContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ContainerGui extends AbstractContainerScreen<GuiContainer> {
    private List<IGuiElement> guiElements;
    private BlockEntity tileEntity;
    private ResourceLocation background;
    private Component name;
    private int width;
    private int height;
    public ContainerGui (GuiContainer container, Inventory inv, Component name, MachineGuiJson json) {
        super(container, inv, name);
        tileEntity = inv.player.level.getBlockEntity(container.getTileEntityPosition());
        guiElements = new ArrayList<>();
        for (GuiElementJson elementJson : json.guiElements) {
            guiElements.add(GuiElementFactory.getElement(elementJson));
        }
        background = new ResourceLocation(json.background);
        this.name = name;
        width = json.width;
        height = json.height;
    }
    /*@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        //minecraft.getTextureManager().bindTexture(background);
        minecraft.getTextureManager().bindForSetup(background);
        blit(guiLeft, guiTop, 0, 0, width, height);
        for (IGuiElement element : guiElements) {
            element.draw(this, tileEntity);
        }
    }*/
    @Override
    protected void renderBg (PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, background);
        blit(pPoseStack, getGuiLeft(), getGuiTop(), 0, 0, width, height);
        for (var element : guiElements) {
            element.draw(pPoseStack, this, tileEntity);
        }
    }
}
