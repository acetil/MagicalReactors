package acetil.magicalreactors.common.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

public class ItemResource extends Item {
    public ItemResource (String name) {
        setCreativeTab(NuclearCreativeTab.INSTANCE);
        setUnlocalizedName(LibMisc.MODID + "." + name);
        setRegistryName(name);
    }
    @SideOnly(Side.CLIENT)
    public void initModel () {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
