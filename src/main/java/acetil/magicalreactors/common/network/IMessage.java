package acetil.magicalreactors.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IMessage {
    void toBytes(PacketBuffer buf);
    void handle (Supplier<NetworkEvent.Context> ctx);
}
