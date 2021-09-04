package acetil.magicalreactors.common.containers;

import acetil.magicalreactors.common.containers.json.MachineContainerJson;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fmllegacy.network.IContainerFactory;

public class GuiContainerFactory implements IContainerFactory<GuiContainer> {
    MachineContainerJson json;
    ResourceLocation registry;
    public GuiContainerFactory (MachineContainerJson json) {
        this.json = json;
        this.registry = new ResourceLocation(Constants.MODID, json.name);
    }
    @Override
    public GuiContainer create(int windowId, Inventory inv, FriendlyByteBuf data) {
        return new GuiContainer(json, registry.toString(), windowId, inv, data);
    }
}
