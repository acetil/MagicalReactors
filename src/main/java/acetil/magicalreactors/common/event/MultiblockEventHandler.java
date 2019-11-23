package acetil.magicalreactors.common.event;

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
    public static Map<IWorld, List<IUpdateListener>> listenerMap = new HashMap<>();
    public static void updateController (IWorld world, BlockPos pos) {
        if (listenerMap.containsKey(world)) {
            for (IUpdateListener listener : listenerMap.get(world)) {
                if (listener.isTracking(pos)) {
                    listener.onBlockUpdate(pos);
                }
            }
        }
    }
    @SubscribeEvent
    public static void blockPlaceEvent (BlockEvent.EntityPlaceEvent event) {
        System.out.println("On block place!");
        if (event.getWorld().isRemote()) {
            updateController(event.getWorld(), event.getPos());
        }
    }
    @SubscribeEvent
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        System.out.println("On block break!");
        if (event.getWorld().isRemote()) {
            updateController(event.getWorld(), event.getPos());
        }
    }
    public interface IUpdateListener {
        void onBlockUpdate (BlockPos pos);
        boolean isTracking (BlockPos pos);
    }
}
