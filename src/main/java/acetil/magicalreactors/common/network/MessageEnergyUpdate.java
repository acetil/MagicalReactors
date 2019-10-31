package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public class MessageEnergyUpdate implements IMessage{
    int totalEnergy;
    int energyChange;
    BlockPos pos;
    public MessageEnergyUpdate (BlockPos pos, int totalEnergy, int energyChange) {
        this.pos = pos;
        this.totalEnergy = totalEnergy;
        this.energyChange = energyChange;
    }
    public static MessageEnergyUpdate fromBytes (PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        int totalEnergy = buf.readInt();
        int energyChange = buf.readInt();
        return new MessageEnergyUpdate(pos, totalEnergy, energyChange);
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(totalEnergy);
        buf.writeInt(energyChange);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            TileEntity te = world.getTileEntity(pos);
            LazyOptional<IEnergyStorage> energyOptional = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (energyOptional.isPresent() &&
                    energyOptional.orElseGet(CapabilityEnergy.ENERGY::getDefaultInstance) instanceof EnergyHandler) {
                ((EnergyHandler) energyOptional.orElseGet(CapabilityEnergy.ENERGY::getDefaultInstance)).setTotalEnergy(totalEnergy);
                ((EnergyHandler) energyOptional.orElseGet(CapabilityEnergy.ENERGY::getDefaultInstance)).setEnergyChange(energyChange);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
