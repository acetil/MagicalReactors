package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.machines.TileMachineBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IGuiElement {
    IGuiElement applyJson (GuiElementJson json);
    void draw (PoseStack matStack, ContainerGui gui, BlockEntity te);
}
