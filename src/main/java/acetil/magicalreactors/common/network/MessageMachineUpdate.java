package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkEvent;

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
    public static MessageMachineUpdate fromBytes (PacketBuffer buf) {
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
           World world = Minecraft.getInstance().world;
           if (world.isAreaLoaded(pos, 2) && world.getTileEntity(pos) != null &&
                   world.getTileEntity(pos).getCapability(CapabilityMachine.MACHINE_CAPABILITY).isPresent()) {
               world.getTileEntity(pos)
                       .getCapability(CapabilityMachine.MACHINE_CAPABILITY)
                       .orElseGet(CapabilityMachine.MACHINE_CAPABILITY::getDefaultInstance)
                       .handlePacket(isOn, energyPerTick, energyToCompletion, totalEnergyRequired);
           }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isOn);
        buf.writeInt(energyPerTick);
        buf.writeInt(energyToCompletion);
        buf.writeInt(totalEnergyRequired);
    }
}
