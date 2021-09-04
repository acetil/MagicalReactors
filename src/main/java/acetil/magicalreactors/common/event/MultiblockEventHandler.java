package acetil.magicalreactors.common.event;

import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;

import java.util.*;

public class MultiblockEventHandler {
    private static Map<LevelReader, List<IUpdateListener>> listenerMap = new HashMap<>();
    private static void updateController (LevelReader world, BlockPos pos, BlockState state) {
        if (listenerMap.containsKey(world)) {
            for (IUpdateListener listener : listenerMap.get(world)) {
                if (listener.isTracking(pos)) {
                    listener.onBlockUpdate(pos, state);
                }
            }
        }
    }
    public static void blockPlaceEvent (BlockEvent.EntityPlaceEvent event) {
        MagicalReactors.LOGGER.debug("On block place!");
        updateController(event.getWorld(), event.getPos(), event.getPlacedBlock());
    }
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        MagicalReactors.LOGGER.debug("On block break!");
        updateController(event.getWorld(), event.getPos(), Blocks.AIR.defaultBlockState());
    }
    public static void addListener (LevelReader world, IUpdateListener listener) {
        if (!listenerMap.containsKey(world)) {
            listenerMap.put(world, new ArrayList<>());
            MagicalReactors.LOGGER.debug("Added listener!");
        }
        listenerMap.get(world).add(listener);
        listener.initialCheck();
    }
    public static void removeListener (LevelReader world, IUpdateListener listener) {
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
