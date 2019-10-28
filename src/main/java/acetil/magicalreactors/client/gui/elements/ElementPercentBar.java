package acetil.magicalreactors.client.gui.elements;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.machines.TileMachineBase;

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
    private ProgressFunction progressFunction;
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
    public void setProgressFunction (ProgressFunction function) {
        this.progressFunction = function;
    }
    @Override
    public void draw(GuiContainer gui, TileMachineBase te) {
        IMachineCapability machineHandler = te.getCapability(CapabilityMachine.MACHINE_CAPABILITY, null);
        IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        IEnergyStorage energyHandler = te.getCapability(CapabilityEnergy.ENERGY, null);
        IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        float progress = progressFunction.getProgress(gui, machineHandler, itemHandler, energyHandler, fluidHandler);
        gui.mc.getTextureManager().bindTexture(texture);
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
        gui.drawTexturedModalRect(startX + gui.getGuiLeft(), startY + gui.getGuiTop(), startU, startV, width, height);

    }
    public enum BarDirection {
        UP, DOWN, LEFT, RIGHT
    }
    public interface ProgressFunction {
        float getProgress (GuiContainer gui, IMachineCapability machineHandler, IItemHandler itemHandler, IEnergyStorage energyHandler,
                           IFluidHandler fluidHandler);
    }
}

