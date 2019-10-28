package acetil.magicalreactors.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import acetil.magicalreactors.common.machines.TileMachineBase;

public class MachineContainer extends Container {
    private TileMachineBase te;
    public MachineContainer (IInventory inventory, TileMachineBase te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(inventory);

    }
    private void addOwnSlots () {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // TODO update for proper values, these are taken from ReactorContainer
        int initialX = 8;
        int initialY = 18; // from ContainerChest.java
        int x = initialX;
        int y = initialY;
        int columns = 9;
        int rowNum = 0;
        int colNum = 0;
        int slotIndex = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            colNum++;
            // TODO clean up and get rid of false
            if (colNum >= columns) {
                rowNum++;
                colNum = 0;
                x = initialX;
                y += 18;
            } else {
                x += 18;
            }
        }
    }
    private void addPlayerSlots (IInventory playerInventory) {
        //TODO get rid/reduce magic numbers
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = i * 18 + 104 + (6 - 4) * 18;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 10, x, y));
            }
        }

        for (int i = 0; i < 9; i ++) {
            int x = 8 + i*18;
            int y = 162 + (6 - 4) * 18; // y values from ContainerChest.java + 1
            this.addSlotToContainer(new Slot(playerInventory, i, x, y));
        }
    }
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
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
