package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.constants.Constants;
import acetil.magicalreactors.common.tiles.TileTestBattery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class BlockTestBattery extends Block {
    public BlockTestBattery () {
        super(Properties.create(Material.ROCK));
        setRegistryName(new ResourceLocation(Constants.MODID, "test_battery"));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTestBattery();
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated (BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY)
                    .orElse(CapabilityEnergy.ENERGY.getDefaultInstance());
            player.sendMessage(new StringTextComponent("Energy: " + energyStorage.getEnergyStored() + " / "
                    + energyStorage.getMaxEnergyStored()));

        }
        return true;
    }
}
