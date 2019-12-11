package acetil.magicalreactors.common.items;

import net.minecraft.item.Item;

public class ItemResource extends Item {
    public ItemResource (String name) {
        super(new Properties());
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
        setRegistryName(name);
    }
}
