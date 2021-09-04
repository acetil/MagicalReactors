package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public class MessageMachineUpdate implements IMessage {
    private boolean isOn;
    private int energyPerTick;
    private int energyToCompletion;
    private int totalEnergyRequired;
    private BlockPos pos;
    public MessageMachineUpdate () {

    }
    public MessageMachineUpdate (BlockPos pos, boolean isOn, int energyPerTick,
                                 int energyToCompletion, int totalEnergyRequired) {
        this.energyPerTick = energyPerTick;
        this.energyToCompletion = energyToCompletion;
        this.totalEnergyRequired = totalEnergyRequired;
        this.isOn = isOn;
        this.pos = pos;
    }
    public static MessageMachineUpdate fromBytes (FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        boolean isOn = buf.readBoolean();
        int energyPerTick = buf.readInt();
        int energyToCompletion = buf.readInt();
        int totalEnergyRequired = buf.readInt();
        return new MessageMachineUpdate(pos, isOn, energyPerTick, energyToCompletion, totalEnergyRequired);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
           var world = Minecraft.getInstance().level;
           if (world.isAreaLoaded(pos, 2) && world.getBlockEntity(pos) != null &&
                   world.getBlockEntity(pos).getCapability(CapabilityMachine.MACHINE_CAPABILITY).isPresent()) {
               world.getBlockEntity(pos)
                       .getCapability(CapabilityMachine.MACHINE_CAPABILITY)
                       .orElseThrow(() -> new RuntimeException("Bad Optional!")) // TODO
                       .handlePacket(isOn, energyPerTick, energyToCompletion, totalEnergyRequired);
           }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isOn);
        buf.writeInt(energyPerTick);
        buf.writeInt(energyToCompletion);
        buf.writeInt(totalEnergyRequired);
    }
}
