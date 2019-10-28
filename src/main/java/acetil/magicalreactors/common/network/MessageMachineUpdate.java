package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.machines.TileMachineBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

public class MessageMachineUpdate implements IMessage {
    private boolean isOn;
    private int energyPerTick;
    private int energyToCompletion;
    private int totalEnergyRequired;
    private int x;
    private int y;
    private int z;
    public MessageMachineUpdate (){}
    public MessageMachineUpdate (int x, int y, int z, boolean isOn, int energyPerTick, int energyToCompletion, int totalEnergyRequired) {
        this.energyPerTick = energyPerTick;
        this.energyToCompletion = energyToCompletion;
        this.totalEnergyRequired = totalEnergyRequired;
        this.isOn = isOn;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        isOn = buf.readBoolean();
        energyPerTick = buf.readInt();
        energyToCompletion = buf.readInt();
        totalEnergyRequired = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(isOn);
        buf.writeInt(energyPerTick);
        buf.writeInt(energyToCompletion);
        buf.writeInt(totalEnergyRequired);

    }

    public static class MessageMachineUpdateHandler implements IMessageHandler<MessageMachineUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessageMachineUpdate message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() ->{
                World world = Minecraft.getMinecraft().world;
                BlockPos pos = new BlockPos(message.x, message.y, message.z);
                if (world.isBlockLoaded(pos) && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileMachineBase) {
                    TileMachineBase te = (TileMachineBase) world.getTileEntity(pos);
                    IMachineCapability cap = te.getCapability(CapabilityMachine.MACHINE_CAPABILITY, null);
                    if (cap != null) {
                        cap.handlePacket(message.isOn, message.energyPerTick, message.energyToCompletion, message.totalEnergyRequired);
                    }
                }
             });
            return null;
        }
    }
}
