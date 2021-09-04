package acetil.magicalreactors.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IMessage {
    void toBytes(FriendlyByteBuf buf);
    void handle (Supplier<NetworkEvent.Context> ctx);
}
