package acetil.magicalreactors.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

public class ItemResource extends Item {
    public ItemResource (String name) {
        super(new Properties());
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
        setRegistryName(name);
    }
}
