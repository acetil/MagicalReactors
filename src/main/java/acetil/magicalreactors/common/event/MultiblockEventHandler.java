package acetil.magicalreactors.common.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class MultiblockEventHandler {
    private static Map<IWorld, List<IUpdateListener>> listenerMap = new HashMap<>();
    private static void updateController (IWorld world, BlockPos pos, BlockState state, BlockState prevState) {
        if (listenerMap.containsKey(world)) {
            for (IUpdateListener listener : listenerMap.get(world)) {
                if (listener.isTracking(pos)) {
                    listener.onBlockUpdate(pos, state, prevState);
                }
            }
        }
    }
    @SubscribeEvent
    public static void blockPlaceEvent (BlockEvent.EntityPlaceEvent event) {
        System.out.println("On block place!");
        updateController(event.getWorld(), event.getPos(), event.getPlacedBlock(), event.getState());
    }
    @SubscribeEvent
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        System.out.println("On block break!");
        updateController(event.getWorld(), event.getPos(), Blocks.AIR.getDefaultState(), event.getState());
    }
    public interface IUpdateListener {
        void onBlockUpdate (BlockPos pos, BlockState state, BlockState prevState);
        boolean isTracking (BlockPos pos);
    }
}
