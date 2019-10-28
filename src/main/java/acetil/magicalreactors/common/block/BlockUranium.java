package acetil.magicalreactors.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.NuclearMod;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import org.apache.logging.log4j.Level;

public class BlockUranium extends Block {
    public BlockUranium () {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + ".uranium_block");
        setHardness(3F);
        setRegistryName("uranium_block");
        setCreativeTab(NuclearCreativeTab.INSTANCE);
        setHarvestLevel("pickaxe", 3);
    }
    public void initModel () {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        NuclearMod.logger.log(Level.INFO, "Block model init");
    }
}
