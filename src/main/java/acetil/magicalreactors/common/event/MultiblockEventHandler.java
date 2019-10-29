package acetil.magicalreactors.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber
public class MultiblockEventHandler {
    public static HashMap<BlockPos, List<UpdateListener>> hashMap = new HashMap<>();
    public static void updateController (BlockPos pos) {
        if (hashMap.containsKey(pos)) {
            hashMap.get(pos).forEach((UpdateListener l) -> l.onBlockUpdate(pos));
        }
    }
    @SubscribeEvent
    public static void blockPlaceEvent (BlockEvent.EntityPlaceEvent event) {
        System.out.println("On block place!");
        if (event.getWorld().isRemote()) {
            updateController(event.getPos());
        }
    }
    @SubscribeEvent
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        System.out.println("On block break!");
        if (event.getWorld().isRemote()) {
            updateController(event.getPos());
        }
    }
    public static void registerPositions (List<BlockPos> positions, UpdateListener listener) {
        for (BlockPos p : positions) {
            if (!hashMap.containsKey(p)) {
                hashMap.put(p, new ArrayList<>());
            }
            if (!hashMap.get(p).contains(listener)) {
                hashMap.get(p).add(listener);
            }
        }
    }
    public static void removePositions (List<BlockPos> positions, UpdateListener listener) {
        for (BlockPos p : positions) {
            if (hashMap.containsKey(p)) {
                hashMap.get(p).remove(listener);
            }
        }
    }
    public interface UpdateListener {
        void onBlockUpdate (BlockPos pos);
    }
}
