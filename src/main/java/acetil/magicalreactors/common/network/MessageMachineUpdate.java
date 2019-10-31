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
    private int x;
    private int y;
    private int z;
    private int totalEnergy;
    public MessageMachineUpdate () {

    }
    public MessageMachineUpdate (int x, int y, int z, boolean isOn, int energyPerTick,
                                 int energyToCompletion, int totalEnergyRequired, int totalEnergy) {
        this.energyPerTick = energyPerTick;
        this.energyToCompletion = energyToCompletion;
        this.totalEnergyRequired = totalEnergyRequired;
        this.isOn = isOn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.totalEnergy = totalEnergy;
    }
    public static MessageMachineUpdate fromBytes (PacketBuffer buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        boolean isOn = buf.readBoolean();
        int energyPerTick = buf.readInt();
        int energyToCompletion = buf.readInt();
        int totalEnergyRequired = buf.readInt();
        int totalEnergy = buf.readInt();
        return new MessageMachineUpdate(x, y, z, isOn, energyPerTick, energyToCompletion, totalEnergyRequired, totalEnergy);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
           World world = Minecraft.getInstance().world;
           BlockPos pos = new BlockPos(x, y, z);
           if (world.isAreaLoaded(pos, 2) && world.getTileEntity(pos) != null &&
                   world.getTileEntity(pos).getCapability(CapabilityMachine.MACHINE_CAPABILITY).isPresent()) {
               world.getTileEntity(pos)
                       .getCapability(CapabilityMachine.MACHINE_CAPABILITY)
                       .orElseGet(CapabilityMachine.MACHINE_CAPABILITY::getDefaultInstance)
                       .handlePacket(isOn, energyPerTick, energyToCompletion, totalEnergyRequired);
               IEnergyStorage energyHandler =  world.getTileEntity(pos)
                                                    .getCapability(CapabilityEnergy.ENERGY)
                                                    .orElseGet(CapabilityEnergy.ENERGY::getDefaultInstance);
               if (energyHandler instanceof EnergyHandler) {
                   ((EnergyHandler) energyHandler).setTotalEnergy(totalEnergy);
               }
           }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public Function<PacketBuffer, IMessage> getFromBytes() {
        return MessageMachineUpdate::fromBytes;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(isOn);
        buf.writeInt(energyPerTick);
        buf.writeInt(energyToCompletion);
        buf.writeInt(totalEnergyRequired);
        buf.writeInt(totalEnergy);
    }
}
