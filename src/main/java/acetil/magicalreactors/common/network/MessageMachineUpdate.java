package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    public MessageMachineUpdate () {

    }
    public MessageMachineUpdate (int x, int y, int z, boolean isOn, int energyPerTick, int energyToCompletion, int totalEnergyRequired) {
        this.energyPerTick = energyPerTick;
        this.energyToCompletion = energyToCompletion;
        this.totalEnergyRequired = totalEnergyRequired;
        this.isOn = isOn;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static MessageMachineUpdate fromBytes (PacketBuffer buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        boolean isOn = buf.readBoolean();
        int energyPerTick = buf.readInt();
        int energyToCompletion = buf.readInt();
        int totalEnergyRequired = buf.readInt();
        return new MessageMachineUpdate(x, y, z, isOn, energyPerTick, energyToCompletion, totalEnergyRequired);
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
           }
        });
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

    }
}
