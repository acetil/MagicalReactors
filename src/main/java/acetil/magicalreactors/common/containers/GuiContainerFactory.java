package acetil.magicalreactors.common.containers;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.common.containers.json.MachineContainerJson;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.IContainerFactory;

public class GuiContainerFactory implements IContainerFactory<GuiContainer> {
    MachineContainerJson json;
    ResourceLocation registry;
    public GuiContainerFactory (MachineContainerJson json) {
        this.json = json;
        this.registry = new ResourceLocation(LibMisc.MODID, json.name);
    }
    @Override
    public GuiContainer create(int windowId, PlayerInventory inv, PacketBuffer data) {
        return new GuiContainer(json, registry.toString(), windowId, inv, data);
    }
}
