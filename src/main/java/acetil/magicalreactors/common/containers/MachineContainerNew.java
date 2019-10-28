package acetil.magicalreactors.common.containers;

import acetil.magicalreactors.common.containers.json.MachineContainerJson;
import acetil.magicalreactors.common.containers.json.MachineSlotJson;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import acetil.magicalreactors.common.machines.TileMachineBase;

import javax.annotation.Nonnull;
// TODO: replace MachineContainer with this
public class MachineContainerNew extends Container {
    private TileMachineBase te;
    protected final int INVENTORY_ROWS = 3;
    protected final int INVENTORY_COLS = 9;
    protected final int HOTBAR_SIZE = 9;
    protected final int HOTBAR_Y_OFFSET = 58;
    public MachineContainerNew (IInventory inventory, TileMachineBase te, MachineContainerJson containerJson) {
        this.te = te;
        addOwnSlots(containerJson);
        addPlayerSlots(inventory, containerJson);
    }
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
    private void addOwnSlots (MachineContainerJson containerJson) {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int slot = 0;
        for (MachineSlotJson slotJson : containerJson.inputsSlots) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slot, slotJson.x, slotJson.y));
            slot++;
        }
        for (MachineSlotJson slotJson : containerJson.outputSlots) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slot, slotJson.x, slotJson.y));
            slot++;
        }
    }
    private void addPlayerSlots (IInventory inventory, MachineContainerJson containerJson) {
        for (int i = 0; i < INVENTORY_ROWS; i++) {
            for (int j = 0; j < INVENTORY_COLS; j++) {
                int x = containerJson.inventoryStartX + j * 18;
                int y = containerJson.inventoryStartY + i * 18;
                this.addSlotToContainer(new Slot(inventory, j + i * INVENTORY_COLS + 10, x, y));
            }
        }
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            int x = containerJson.inventoryStartX + i* 18;
            int y = containerJson.inventoryStartY + HOTBAR_Y_OFFSET;
            this.addSlotToContainer(new Slot(inventory, i, x, y));
        }
    }
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        try {
            ItemStack itemstack = ItemStack.EMPTY;
            Slot slot = this.inventorySlots.get(index);

            if (slot != null && slot.getHasStack()) {
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                int slots = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
                if (index < slots) {
                    if (!this.mergeItemStack(itemstack1, slots, this.inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, slots, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }
            }

            return itemstack;
        } catch (Exception e) {
            System.out.println(e);
        }
        return ItemStack.EMPTY;
    }
}
