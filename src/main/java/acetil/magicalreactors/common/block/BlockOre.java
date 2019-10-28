package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

import java.util.Random;

public class BlockOre extends Block {
    public BlockOre (String name, float hardness) {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + "." + name);
        setRegistryName(name);
        setHardness(hardness);
        setCreativeTab(NuclearCreativeTab.INSTANCE);
    }
    @SideOnly(Side.CLIENT)
    public void initModel () {
        // TODO create json
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public Item getItemDropped (IBlockState state, Random rand, int fortune) {
        if (this == ModBlocks.TEMP_ORE2) {
            return ModItems.ITEM_TEMP2;
        } else {
            return Item.getItemFromBlock(this);
        }
    }
    public int quantityDropped (Random random) {
        return 1; // TODO verify correct
    }
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && this == ModBlocks.TEMP_ORE2) {
            int i = Math.min(0, random.nextInt(fortune + 2) - 1);
            return this.quantityDropped(random) * (i + 1);
        }
        return this.quantityDropped(random);
    }
    public int getExpDrop (IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int i = 0;
        if (this == ModBlocks.TEMP_ORE2) {
            i = MathHelper.getInt(rand, 2, 5);
        }
        return i;
    }
}
