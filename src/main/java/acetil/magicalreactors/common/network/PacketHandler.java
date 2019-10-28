package acetil.magicalreactors.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import acetil.magicalreactors.common.lib.LibMisc;

public class PacketHandler{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibMisc.NETWORK_CHANNEL);
    private static int id = 0;
    public static void initMessages () {
        initMessage(MessageMachineUpdate.MessageMachineUpdateHandler.class, MessageMachineUpdate.class, Side.CLIENT);
    }

    public static <REQ extends IMessage, REPLY extends IMessage> void initMessage (Class<? extends IMessageHandler<REQ, REPLY>> handler,
                                                                                   Class<REQ> message, Side side) {
        INSTANCE.registerMessage(handler, message, id, side);
        id++;
    }
}
