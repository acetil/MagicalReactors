package acetil.magicalreactors.client.gui;

import acetil.magicalreactors.common.containers.MachineContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class AbstractGuiMachine extends GuiContainer {

    public AbstractGuiMachine(MachineContainer container, int xSize, int ySize) {
        super(container);
        this.xSize = xSize;
        this.ySize = ySize;
    }

}
