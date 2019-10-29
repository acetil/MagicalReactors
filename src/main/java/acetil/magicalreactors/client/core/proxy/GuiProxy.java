package acetil.magicalreactors.client.core.proxy;

import acetil.magicalreactors.common.containers.json.MachineContainerManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.client.gui.MachineGui;
import acetil.magicalreactors.client.gui.MachineGuiNew;
import acetil.magicalreactors.client.gui.ReactorGui;
import acetil.magicalreactors.client.gui.json.MachineGuiManager;
import acetil.magicalreactors.common.containers.MachineContainerNew;
import acetil.magicalreactors.common.machines.TileMachineBase;
import acetil.magicalreactors.common.lib.LibGui;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world.isRemote) {
            System.out.println("Remote world!");
            return null;
        }
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == LibGui.REACTOR_ID) {
            System.out.println("Server side gui");
            return new ReactorContainer(player.inventory, (TileReactor) te);
        } else if (ID == LibGui.TEST_GUI_ID) {
            return new MachineContainer(player.inventory, (TileMachineBase) te);
        } else if (ID >= LibGui.MACHINE_GUI_START && te instanceof TileMachineBase && MachineGuiManager.getGui(ID) != null) {
            TileMachineBase machineBase = (TileMachineBase) te;
            return new MachineContainerNew(player.inventory, machineBase,
                    MachineContainerManager.getContainerJson(MachineGuiManager.getGui(ID).container));
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == LibGui.REACTOR_ID) {
            return new ReactorGui((TileReactor) te, new ReactorContainer(player.inventory, (TileReactor) te));
        } else if (ID == LibGui.TEST_GUI_ID) {
            return new MachineGui((TileMachineBase) te, new MachineContainer(player.inventory, (TileMachineBase) te));
        } else if (ID >= LibGui.MACHINE_GUI_START && MachineGuiManager.getGui(ID) != null) {
            TileMachineBase machineBase = (TileMachineBase) te;
            return new MachineGuiNew((TileMachineBase) te, new MachineContainerNew(player.inventory, machineBase,
                    MachineContainerManager.getContainerJson(MachineGuiManager.getGui(ID).container)), MachineGuiManager.getGui(ID));
        }
        System.out.println("Shouldn't get here");
        return null;
    }
}
