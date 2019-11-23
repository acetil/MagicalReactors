package acetil.magicalreactors.common.event;

import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class MultiblockEventHandler {
    private static Map<IWorld, List<IUpdateListener>> listenerMap = new HashMap<>();
    private static Set<IWorld> doInitialChecks = new HashSet<>();
    private static void updateController (IWorld world, BlockPos pos, BlockState state) {
        if (listenerMap.containsKey(world)) {
            for (IUpdateListener listener : listenerMap.get(world)) {
                if (listener.isTracking(pos)) {
                    listener.onBlockUpdate(pos, state);
                }
            }
        }
    }
    @SubscribeEvent
    public static void blockPlaceEvent (BlockEvent.EntityPlaceEvent event) {
        MagicalReactors.LOGGER.debug("On block place!");
        updateController(event.getWorld(), event.getPos(), event.getPlacedBlock());
    }
    @SubscribeEvent
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        MagicalReactors.LOGGER.debug("On block break!");
        updateController(event.getWorld(), event.getPos(), Blocks.AIR.getDefaultState());
    }
    @SubscribeEvent
    public static void worldLoadEvent (WorldEvent.Load event) {
        MagicalReactors.LOGGER.debug("On world load!");
        doInitialChecks.add(event.getWorld());
        if (listenerMap.containsKey(event.getWorld())) {
            MagicalReactors.LOGGER.debug("Running initial checks!");
            listenerMap.get(event.getWorld()).forEach(IUpdateListener::initialCheck);
        }
    }
    public static void addListener (IWorld world, IUpdateListener listener) {
        if (!listenerMap.containsKey(world)) {
            listenerMap.put(world, new ArrayList<>());
            MagicalReactors.LOGGER.debug("Added listener!");
        }
        listenerMap.get(world).add(listener);
        if (doInitialChecks.contains(world)) {
            listener.initialCheck();
        }
    }
    public static void removeListener (IWorld world, IUpdateListener listener) {
        if (listenerMap.containsKey(world)) {
            listenerMap.get(world).remove(listener);
            MagicalReactors.LOGGER.debug("Removed listener!");
        }
    }
    public interface IUpdateListener {
        void onBlockUpdate (BlockPos pos, BlockState state);
        boolean isTracking (BlockPos pos);
        void initialCheck ();
    }
}
