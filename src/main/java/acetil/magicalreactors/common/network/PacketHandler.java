package acetil.magicalreactors.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import acetil.magicalreactors.common.constants.Constants;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MODID, "main"), () -> Constants.PROTOCOL_VERSION,
            Constants.PROTOCOL_VERSION::equals, Constants.PROTOCOL_VERSION::equals);
    private static int id = 0;
    public static void initMessages () {
        registerMessage(MessageMachineUpdate.class, MessageMachineUpdate::fromBytes);
        registerMessage(MessageEnergyUpdate.class, MessageEnergyUpdate::fromBytes);
    }
    public static <MSG extends IMessage> void registerMessage (Class<MSG> msg, Function<PacketBuffer, MSG> fromBytes) {
        registerMessage(msg, MSG::toBytes, fromBytes, MSG::handle);
    }

    private static <MSG extends IMessage> void registerMessage(Class<MSG> msg, BiConsumer<MSG, PacketBuffer> encode, Function<PacketBuffer,MSG> decode, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle) {
        INSTANCE.registerMessage(id++, msg, encode, decode, handle);
    }



}
