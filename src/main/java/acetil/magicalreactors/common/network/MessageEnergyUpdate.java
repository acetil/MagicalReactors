package acetil.magicalreactors.common.network;

import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
    public static MessageEnergyUpdate fromBytes (FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int totalEnergy = buf.readInt();
        int energyChange = buf.readInt();
        return new MessageEnergyUpdate(pos, totalEnergy, energyChange);
    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(totalEnergy);
        buf.writeInt(energyChange);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var world = Minecraft.getInstance().level;
            var te = world.getBlockEntity(pos);
            LazyOptional<IEnergyStorage> energyOptional = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (energyOptional.isPresent() &&
                    energyOptional.orElseThrow(() -> new RuntimeException("Bad optional!")) instanceof EnergyHandler) { // TODO
                ((EnergyHandler) energyOptional.orElseThrow(() -> new RuntimeException("Bad optional!"))).setTotalEnergy(totalEnergy);
                ((EnergyHandler) energyOptional.orElseThrow(() -> new RuntimeException("Bad optional!"))).setEnergyChange(energyChange);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
