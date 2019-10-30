package acetil.magicalreactors.common.containers;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.containers.json.MachineContainerJson;
import acetil.magicalreactors.common.containers.json.MachineSlotJson;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;


public class GuiContainer extends Container {
    private static final int INVENTORY_ROWS = 3;
    private static final int INVENTORY_COLS = 9;
    private static final int HOTBAR_SIZE = 9;
    private static final int HOTBAR_OFFSET = 58;
    private static final int INVENTORY_SLOT_SIZE = 18;
    private BlockPos tePos;
    private IItemHandler itemHandler;
    private int ownSize;
    public GuiContainer (MachineContainerJson json, String containerTypeName, int windowId, PlayerInventory inv, PacketBuffer data) {
        super(ForgeRegistries.CONTAINERS.getValue(new ResourceLocation(containerTypeName)), windowId);
        tePos = data.readBlockPos();
        this.itemHandler = inv.player.world.getTileEntity(tePos)
                                           .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                                           .orElse(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getDefaultInstance());
        addSlots(itemHandler, inv, json);
    }
    public GuiContainer (MachineContainerJson json, String containerTypeName, int windowId, PlayerInventory inv, IItemHandler handler,
                         BlockPos pos) {
        super(ForgeRegistries.CONTAINERS.getValue(new ResourceLocation(containerTypeName)), windowId);
        addSlots(handler, inv, json);
        this.itemHandler = handler;
        this.tePos = pos;
    }
    public void addSlots (IItemHandler handler, PlayerInventory inv, MachineContainerJson json) {
        ownSize = json.inputsSlots.size() + json.outputSlots.size();
        addOwnSlots(handler, json);
        addPlayerSlots(inv, json);
    }
    private void addOwnSlots (IItemHandler inv, MachineContainerJson json) {
        int slotNum = 0;
        for (MachineSlotJson slotJson : json.inputsSlots) {
            addSlot(new SlotItemHandler(inv, slotNum, slotJson.x, slotJson.y));
            slotNum++;
        }
        for (MachineSlotJson slotJson : json.outputSlots) {
            addSlot(new SlotItemHandler(inv, slotNum, slotJson.x, slotJson.y));
            slotNum++;
        }
    }
    private void addPlayerSlots (PlayerInventory inv, MachineContainerJson json) {
        for (int i = 0; i< INVENTORY_ROWS; i++) {
            for (int j = 0; j < INVENTORY_COLS; j++) {
                int x = json.inventoryStartX + j * INVENTORY_SLOT_SIZE;
                int y = json.inventoryStartY + i * INVENTORY_SLOT_SIZE;
                addSlot(new Slot(inv, j + i * INVENTORY_COLS + HOTBAR_SIZE, x, y));
            }
        }
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            addSlot(new Slot(inv, i, json.inventoryStartX + i * INVENTORY_SLOT_SIZE,
                    json.inventoryStartY + HOTBAR_OFFSET));
        }
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    public IItemHandler getItemHandler () {
        return itemHandler;
    }
    public BlockPos getTileEntityPosition () {
        return tePos;
    }
    @Override
    public ItemStack transferStackInSlot (PlayerEntity playerIn, int index) {
        System.out.println("Transferring stack in slot!");
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index < ownSize) {
                if (!this.mergeItemStack(stack1, ownSize, inventorySlots.size(), true)) {
                    System.out.println("Merged stack!");
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 0, ownSize, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }
}
