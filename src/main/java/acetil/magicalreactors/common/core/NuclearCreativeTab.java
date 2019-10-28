package acetil.magicalreactors.common.core;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import acetil.magicalreactors.common.lib.LibMisc;

public class NuclearCreativeTab extends CreativeTabs {

    public static final NuclearCreativeTab INSTANCE = new NuclearCreativeTab();
    public NuclearCreativeTab () {
        super(LibMisc.MODID);
    }
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.URANIUM_INGOT);
    }


}
