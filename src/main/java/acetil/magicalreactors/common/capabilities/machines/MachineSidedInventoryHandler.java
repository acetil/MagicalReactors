package acetil.magicalreactors.common.capabilities.machines;

import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MachineSidedInventoryHandler implements IItemHandler, IItemHandlerModifiable {
    private String machine;
    private ItemStackHandler itemHandler;
    private int inputSlots;
    public MachineSidedInventoryHandler (String machine, ItemStackHandler itemHandler, int inputSlots) {
        this.machine = machine;
        this.itemHandler = itemHandler;
        this.inputSlots = inputSlots;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot < inputSlots && isItemValid(slot, stack)) {
            return itemHandler.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < inputSlots) {
            return ItemStack.EMPTY;
        }
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < inputSlots) {
            return MachineRecipeManager.isValidInput(machine, stack.getItem());
        } else {
            return false;
        }
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }
}
