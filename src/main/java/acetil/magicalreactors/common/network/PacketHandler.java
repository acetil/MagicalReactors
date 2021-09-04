package acetil.magicalreactors.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

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
    public static <MSG extends IMessage> void registerMessage (Class<MSG> msg, Function<FriendlyByteBuf, MSG> fromBytes) {
        registerMessage(msg, MSG::toBytes, fromBytes, MSG::handle);
    }

    private static <MSG extends IMessage> void registerMessage(Class<MSG> msg, BiConsumer<MSG, FriendlyByteBuf> encode, Function<FriendlyByteBuf,MSG> decode, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle) {
        INSTANCE.registerMessage(id++, msg, encode, decode, handle);
    }



}
