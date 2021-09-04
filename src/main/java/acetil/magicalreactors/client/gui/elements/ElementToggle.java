package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.machines.TileMachineBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElementToggle implements IGuiElement {
    private ResourceLocation texture;
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    @Override
    public IGuiElement applyJson(GuiElementJson json) {
        return null;
    }

    @Override
    public void draw(PoseStack matStack, ContainerGui gui, BlockEntity te) {

    }


}
