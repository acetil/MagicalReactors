package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import acetil.magicalreactors.client.gui.json.GuiElementJson;

public class ElementPercentBar implements IGuiElement {
    private String name;
    private ResourceLocation texture;
    private int u;
    private int v;
    private int x;
    private int y;
    private int width;
    private int height;
    private BarDirection direction;
    private PercentFunction percentFunction;
    @Override
    public IGuiElement applyJson(GuiElementJson json) {
        name = json.name;
        u = json.textures.get(0).textureX;
        v = json.textures.get(0).textureY;
        x = json.x;
        y = json.y;
        width = json.width;
        height = json.height;
        texture = new ResourceLocation(json.texture);
        return this;
    }
    public void setDirection (BarDirection direction) {
        this.direction = direction;
    }
    public void setProgressFunction (PercentFunction function) {
        this.percentFunction = function;
    }
    @Override
    public void draw (PoseStack matStack, ContainerGui gui, BlockEntity te) {
        float progress = percentFunction.getFilled(gui, te);
        //gui.getMinecraft().getTextureManager().bindForSetup(texture);
        RenderSystem.setShaderTexture(0, texture);
        int width = this.width;
        int height = this.height;
        int startX = x;
        int startY = y;
        int startU = u;
        int startV = v;
        switch (direction) {
            case RIGHT:
                width *= progress;
                break;
            case DOWN:
                height *= progress;
                break;
            case LEFT:
                startX += (int)Math.floor((1 - progress) * width);
                startU += (int)Math.floor((1 - progress) * width);
                width *= progress;
                break;
            case UP:
                startY += (int)Math.floor((1 - progress) * height);
                startV += (int)Math.floor((1 - progress) * height);
                height *= progress;
                break;
        }
        gui.blit(matStack, startX + gui.getGuiLeft(), startY + gui.getGuiTop(), startU, startV, width, height);

    }
    public enum BarDirection {
        UP, DOWN, LEFT, RIGHT
    }
    public interface PercentFunction {
        float getFilled (ContainerGui gui, BlockEntity te);
    }
}

