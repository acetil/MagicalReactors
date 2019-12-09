package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.reactor.IReactorBuildingBlock;
import acetil.magicalreactors.common.capabilities.CapabilityReactor;
import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.event.MultiblockEventHandler;
import acetil.magicalreactors.common.multiblock.IMultiblock;
import acetil.magicalreactors.common.multiblock.IMultiblockValidator;
import acetil.magicalreactors.common.multiblock.MultiblockRegistry;
import acetil.magicalreactors.common.reactor.IReactorFuel;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReactorControlHandler implements IReactorControlCapability, MultiblockEventHandler.IUpdateListener {
    private boolean isPowered = false;
    private boolean isUpdateTick;
    private boolean isMulti = false;
    private BlockPos pos;
    private World world;
    private IReactorHandlerNew reactorHandler;
    private List<IMultiblock> multiblocks;
    private List<IReactorInterfaceHandler> reactorInterfaceHandlers = new ArrayList<>();
    private List<IMultiblockValidator> validators;
    private IMultiblockValidator currentValidator;
    public ReactorControlHandler () {
        pos = null;
        world = null;
        isUpdateTick = false;
        reactorHandler = null;
        validators = new ArrayList<>();
        multiblocks = MultiblockRegistry.getMultiblocks("reactor");
    }

    @Override
    public void setPosition(World worldIn, BlockPos pos) {
        world = worldIn;
        this.pos = pos;
        validators = multiblocks.stream()
                                .map((IMultiblock m)->m.getMultiblockValidator(worldIn, pos))
                                .collect(Collectors.toList());
    }

    @Override
    public BlockPos getPosition() {
        return pos;
    }

    @Override
    public void setIsPowered(boolean isPowered) {
        if (isPowered && !this.isPowered) {
            isUpdateTick = true;
        }
        this.isPowered = isPowered;
    }

    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }
        // TODO: refactor
        int power = 0;
        int powerOutput = 0;
        if (!isMulti) {
            return;
        }
        if (isPowered) {
            if (isUpdateTick) {
                reactorHandler.update();
            }
            isUpdateTick = !isUpdateTick;
            power = reactorHandler.getEnergyProduced();
        }

        int totalPowerOutput = 0;
        for (IReactorInterfaceHandler interfaceHandler : reactorInterfaceHandlers) {
            totalPowerOutput += interfaceHandler.getPowerAcceptance();
        }

        List<IReactorFuel> reactorFuels = new ArrayList<>();
        for (IReactorInterfaceHandler interfaceHandler : reactorInterfaceHandlers) {
            int powerAcceptance = interfaceHandler.getPowerAcceptance();
            powerOutput += interfaceHandler.receivePower((int)Math.min(power - powerOutput,
                    Math.ceil(power * (float)powerAcceptance / totalPowerOutput)));

            if (reactorHandler.finished()) {
                reactorFuels.addAll(interfaceHandler.getReactorFuels());
            }

            interfaceHandler.updateInterface(reactorHandler, this);
        }

        if (reactorHandler.finished() && reactorFuels.size() > 0) {
            MagicalReactors.LOGGER.debug("Setting fuels!");
            reactorHandler.setFuels(reactorFuels);
        }
    }

    @Override
    public void setReactorHandler(IReactorHandlerNew reactorHandler) {
        this.reactorHandler = reactorHandler;
    }

    @Override
    public boolean isOn() {
        return isPowered;
    }

    @Override
    public void checkMultiblock() {
        // TODO: refactor
        if (world.isRemote) {
            return;
        }
        boolean isValid = false;
        for (IMultiblockValidator validator : validators) {
            validator.updateAll();
            if (validator.isValid()) {
                currentValidator = validator;
                MagicalReactors.LOGGER.log(Level.INFO, "Multilock is valid!");
                isValid = true;
                break;
            } else {
                for (BlockPos pos : validator.getInvalidBlocks()) {
                    MagicalReactors.LOGGER.log(Level.INFO, String.format("Invalid block: (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
                }
            }
        }
        if (!isValid) {
            currentValidator = validators.stream()
                                         .min(Comparator.comparingInt(IMultiblockValidator::getNumInvalidBlocks))
                                         .get();
        }
        if (!isValid) {
            MagicalReactors.LOGGER.log(Level.INFO, "Multiblock is invalid!");
        }
        for (BlockPos pos : currentValidator.getPositionsOfType(IReactorBuildingBlock.class)) {
            ((IReactorBuildingBlock) world.getBlockState(pos)
                                          .getBlock())
                                          .updateMultiblockState(world.getBlockState(pos), world, pos, isValid);
        }
        if (isValid) {
            reactorInterfaceHandlers = currentValidator.getPositionsWithCapability(CapabilityReactorInterface.REACTOR_INTERFACE, null)
                    .stream()
                    .map((BlockPos p) -> world.getTileEntity(p)
                            .getCapability(CapabilityReactorInterface.REACTOR_INTERFACE, null).orElse(CapabilityReactorInterface.REACTOR_INTERFACE.getDefaultInstance()))
                    .collect(Collectors.toList());
            reactorHandler = world.getTileEntity(currentValidator.getPositionsWithCapability(CapabilityReactor.CAPABILITY_REACTOR, null)
                    .get(0))
                    .getCapability(CapabilityReactor.CAPABILITY_REACTOR, null)
                    .orElse(CapabilityReactor.CAPABILITY_REACTOR.getDefaultInstance());
            if (!isMulti) {

            }
        }
        isMulti = isValid;
    }

    @Override
    public void debugPrint (PlayerEntity player) {
        if (reactorHandler != null) {
            reactorHandler.debugMessage(player);
        }
    }

    private void checkBlock (BlockPos pos, BlockState state) {
        // TODO: consider refactor
        if (world.isRemote()) {
            return;
        }
        boolean isValid = false;
        for (IMultiblockValidator validator : validators) {
            validator.update(pos, state);
            if (validator.isValid()) {
                isValid = true;
                MagicalReactors.LOGGER.debug("Multiblock is valid!");
                currentValidator = validator;
                break;
            }
        }
        if (!isValid) {
            currentValidator = validators.stream().min(Comparator.comparingInt(IMultiblockValidator::getNumInvalidBlocks)).get();
            MagicalReactors.LOGGER.debug("Multiblock is invalid!");
            for (BlockPos p : currentValidator.getInvalidBlocks()) {
                MagicalReactors.LOGGER.debug("Invalid block: ({},{},{})", p.getX(), p.getY(), p.getZ());
            }
        }
        if (isValid != isMulti) {
            reactorInterfaceHandlers = new ArrayList<>();
            reactorHandler = null;
            if (isValid) {
                reactorInterfaceHandlers = currentValidator.getPositionsWithCapability(CapabilityReactorInterface.REACTOR_INTERFACE, null)
                        .stream()
                        .map((BlockPos p) -> world.getTileEntity(p)
                                .getCapability(CapabilityReactorInterface.REACTOR_INTERFACE, null).orElse(CapabilityReactorInterface.REACTOR_INTERFACE.getDefaultInstance()))
                        .collect(Collectors.toList());
                reactorHandler = world.getTileEntity(currentValidator.getPositionsWithCapability(CapabilityReactor.CAPABILITY_REACTOR, null)
                        .get(0))
                        .getCapability(CapabilityReactor.CAPABILITY_REACTOR, null)
                        .orElse(CapabilityReactor.CAPABILITY_REACTOR.getDefaultInstance());
            }
            for (BlockPos p : currentValidator.getPositionsOfType(IReactorBuildingBlock.class)) {
                ((IReactorBuildingBlock)world.getBlockState(p).getBlock())
                        .updateMultiblockState(world.getBlockState(p), world, p, isValid);
            }
            isMulti = isValid;
        }
    }
    @Override
    public void onBlockUpdate(BlockPos pos, BlockState state) {
        checkBlock(pos, state);
    }

    @Override
    public boolean isTracking(BlockPos pos) {
        for (IMultiblockValidator validator : validators) {
            if (validator.contains(pos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialCheck() {
        checkMultiblock(); // TODO: update
    }
    public void onCreation () {
        if (!world.isRemote) {
            MultiblockEventHandler.addListener(world, this);
        }
    }
    public void onDestruction () {
        if (!world.isRemote) {
            MultiblockEventHandler.removeListener(world, this);
            for (BlockPos p : currentValidator.getPositionsOfType(IReactorBuildingBlock.class)) {
                ((IReactorBuildingBlock)world.getBlockState(p).getBlock())
                        .updateMultiblockState(world.getBlockState(p), world, p, false);
            }
        }
    }
}
