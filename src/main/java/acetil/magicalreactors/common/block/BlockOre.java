package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockOre extends Block {
    public BlockOre (Properties properties) {
        super(properties);
    }

    public Item getItemDropped (BlockState state, Random rand, int fortune) {
        if (this == ModBlocks.TEMP_ORE2.get()) {
            return ModItems.ITEM_TEMP2.get();
        } else {
            return Item.getItemFromBlock(this);
        }
    }
    public int quantityDropped (Random random) {
        return 1; // TODO verify correct
    }
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && this == ModBlocks.TEMP_ORE2.get()) {
            int i = Math.min(0, random.nextInt(fortune + 2) - 1);
            return this.quantityDropped(random) * (i + 1);
        }
        return this.quantityDropped(random);
    }
    /*public int getExpDrop (BlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int i = 0;
        if (this == ModBlocks.TEMP_ORE2) {
            i = MathHelper.getInt(rand, 2, 5);
        }
        return i;
    } */
}
