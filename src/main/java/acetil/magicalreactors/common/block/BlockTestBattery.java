package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.constants.Constants;
import acetil.magicalreactors.common.tiles.TileTestBattery;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class BlockTestBattery extends Block implements EntityBlock {
    public BlockTestBattery () {
        super(Properties.of(Material.STONE));
    }

    /*@Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTestBattery();
    }*/

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use (BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.PASS;
        }
        var te = pLevel.getBlockEntity(pPos);
        if (te != null && te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY)
                    .orElseThrow(() -> new RuntimeException("Bad Optional!")); // TODO
            pPlayer.sendMessage(new TextComponent("Energy: " + energyStorage.getEnergyStored() + " / "
                    + energyStorage.getMaxEnergyStored()), pPlayer.getUUID());
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pPos, BlockState pState) {
        return new TileTestBattery(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, state, tile) -> {
            if (tile instanceof TileTestBattery) {
                ((TileTestBattery) tile).tickServer();
            }
        };
    }

    /*@SuppressWarnings("deprecation")
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
    }*/
}
