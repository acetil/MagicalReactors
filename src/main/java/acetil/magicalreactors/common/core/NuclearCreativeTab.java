package acetil.magicalreactors.common.core;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import acetil.magicalreactors.common.constants.Constants;

public class NuclearCreativeTab extends ItemGroup {

    public static final NuclearCreativeTab INSTANCE = new NuclearCreativeTab();
    public NuclearCreativeTab () {
        super(Constants.MODID);
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.URANIUM_INGOT);
    }
}
