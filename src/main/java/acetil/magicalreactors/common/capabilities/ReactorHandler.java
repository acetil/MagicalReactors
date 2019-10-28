package acetil.magicalreactors.common.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactoritems.DamageableReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.IReactorItem;
import acetil.magicalreactors.common.lib.LibReactor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReactorHandler implements IReactorHandler{
    private int ticks = 20;
    private int energyProduction = 0;
    private ArrayList<IReactorItem> reactorItems;
    private ArrayList<ItemStack> items;
    private ItemStackHandler itemStackHandler;
    private boolean isActive = true;
    private int hullHeat = 0;
    private int maxHullHeat;
    private int numColumns;
    private int columnLength;
    class Pulse {
        int index, indexFrom;
        public Pulse (int index, int indexFrom) {
            this.index = index;
            this.indexFrom = indexFrom;
        }
    }
    public ReactorHandler (int maxHullHeat, int numColumns, int columnLength) {
        this.maxHullHeat = maxHullHeat;
        this.numColumns = numColumns;
        this.columnLength = columnLength;
        reactorItems = new ArrayList<>();
        items = new ArrayList<>();
    }
    @Override
    public void update() {
        if (isActive) {
            ticks++;
            if (ticks >= LibReactor.TICKS_PER_UPDATE) {
                updateReactor();
                ticks -= LibReactor.TICKS_PER_UPDATE;
            }
        } else {
            energyProduction = 0;
        }
    }

    @Override
    public int getEnergyProduction() {
        return energyProduction;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int getHullHeat() {
        return hullHeat;
    }

    @Override
    public void updateItems(ItemStackHandler itemStackHandler) {
        //NuclearMod.logger.log(Level.INFO, "Updating items for nuclear reactor");
        reactorItems.clear();
        items.clear();
        this.itemStackHandler = itemStackHandler;
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            reactorItems.add(itemStackHandler.getStackInSlot(i).getCapability(CapabilityReactorItem.REACTOR_ITEM, null));
            items.add(itemStackHandler.getStackInSlot(i));
        }
    }

    @Override
    public int getTicksSinceUpdate() {
        return ticks;
    }

    @Override
    public void setTicksSinceUpdate(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public int getMaxHullHeat() {
        return maxHullHeat;
    }

    @Override
    public void setHullHeat(int heat) {
        this.hullHeat = heat;
    }
    private List<Integer> getAdjacentIndexes (int index) {
        List<Integer> adjacent = new LinkedList<>();
        if (index != 0) {
            adjacent.add(index - 1);
        } if (index % numColumns != 2) {
            adjacent.add(index + 1);
        } if (index - numColumns >= 0) {
            adjacent.add(index - numColumns);
        } if (index + numColumns <= reactorItems.size()) {
            adjacent.add(index + numColumns);
        }

        return adjacent;
    }
    private void pulseItems () {
        LinkedList<Pulse> pulses = new LinkedList<>();
        for (int i = 0; i < reactorItems.size(); i++) {
            if (reactorItems.get(i) != null) {
                pulses.add(new Pulse(i, -1));
            }
        }
        while (pulses.size() > 0) {
            Pulse p = pulses.pop();
            int response = reactorItems.get(p.index).receivePulse();
            if (response == LibReactor.PULSE_BACK && p.indexFrom != -1) {
                pulses.add(new Pulse(p.indexFrom, p.index));
            } else if (response == LibReactor.PULSE_ALL && p.indexFrom == -1) {
                for (int i: getAdjacentIndexes(p.index)) {
                    if (reactorItems.get(i) != null) {
                        pulses.add(new Pulse(i, p.index));
                    }
                }
            }
        }
    }
    private void smartHeatTransfer (IReactorItem item, int index) {
        List<Integer> adjacents = getAdjacentIndexes(index);
        int totalHeat = item.getHeat();
        int totalCapacity = item.getMaxHeat();
        for (int i : adjacents) {
            if (reactorItems.get(i) != null && reactorItems.get(i).willAcceptHeat()) {
                totalHeat += reactorItems.get(i).getHeat();
                totalCapacity += reactorItems.get(i).getMaxHeat();
            }
        }
        if (item.getHullTransferRate() > 0) {
            totalHeat += hullHeat;
            totalCapacity += maxHullHeat;
        }
        float idealPercent = (float) totalHeat / totalCapacity;
        for (int i : adjacents) {
            IReactorItem item2 = reactorItems.get(i);
            if (item2 != null && item2.willAcceptHeat()) {
                int idealHeat = (int) Math.floor(idealPercent * item2.getMaxHeat());
                int heatTransferred;
                if (Math.abs(idealHeat - item2.getHeat()) <= item.getHeatTransferRate()) {
                    heatTransferred = idealHeat - item2.getHeat();

                } else {
                    heatTransferred = item.getHeatTransferRate() * (idealHeat < item2.getHeat() ? -1 : 1);
                }
                item2.receiveHeat(heatTransferred);
                item.receiveHeat(-1 * heatTransferred);
            }
        }
        int hullHeatTransferred;
        int idealHeatDiff = (int)Math.floor(idealPercent * maxHullHeat) - hullHeat;
        if (Math.abs(idealHeatDiff) <= item.getHullTransferRate()) {
            hullHeatTransferred = idealHeatDiff;
        } else {
            hullHeatTransferred = item.getHullTransferRate() * idealHeatDiff / Math.abs(idealHeatDiff); // never is 0
        }
        item.receiveHeat(-1 * hullHeatTransferred);
        hullHeat += hullHeatTransferred;
    }
    private void normalHeatTransfer (IReactorItem item, int index) {
        List<Integer> adjacents = getAdjacentIndexes(index);
        int numSides = 0;
        for (int i : adjacents) {
            if (reactorItems.get(i) != null && reactorItems.get(i).willAcceptHeat()) {
                numSides++;
            }
        }
        if (numSides == 0) {
            return;
        }
        int heatTransferred = Math.min(item.getHeat(), item.getHeatTransferRate() * numSides);
        int heatPerSide = heatTransferred / numSides;
        int extraHeat = heatTransferred % numSides;
        boolean first = true;
        for (int i : adjacents) {
            IReactorItem receiver = reactorItems.get(i);
            if (receiver == null || !receiver.willAcceptHeat()) {
                continue;
            }
            receiver.receiveHeat(heatPerSide);
            if (first) {
                // 1st legal side gets extra heat, if any (in case of less heat than sides)
                receiver.receiveHeat(extraHeat);
                first = false;
            }
        }
    }
    private void normalHullTransfer (IReactorItem item) {
        int idealHeatTransfer;
        if (item.getHullTransferRate() < 0 && Math.abs(item.getHullTransferRate()) > item.getHeat()) {
            idealHeatTransfer = -1 * item.getHeat();
        } else {
            idealHeatTransfer = item.getHullTransferRate();
        }
        int heatTransferred = Math.min(hullHeat, idealHeatTransfer);
        hullHeat -= heatTransferred;
        item.receiveHeat(heatTransferred);
    }
    private void transferHeat () {
        for (int i = 0; i < reactorItems.size(); i++) {
            IReactorItem item = reactorItems.get(i);
            if (item != null && !item.isSmart()) {
                normalHeatTransfer(item, i);
                normalHullTransfer(item);
            }
        }
        for (int i = 0; i < reactorItems.size(); i++) {
            IReactorItem item = reactorItems.get(i);
            if (item != null && item.isSmart()) {
                smartHeatTransfer(item, i);
            }
        }
        for (int i = 0; i < reactorItems.size(); i++) {
            IReactorItem item = reactorItems.get(i);
            if (item != null) {
                System.out.println("Heat of item " + i + ": " + item.getHeat());
            }
        }
    }
    private void endUpdate () {
        for (IReactorItem item : reactorItems) {
            if (item != null) {
                item.endUpdate();
            }
        }
    }
    private void updateDamage () {
        boolean itemsDestroyed = false;
        for (int i = 0; i < reactorItems.size(); i++) {
            IReactorItem item = reactorItems.get(i);
            if (item != null) {
                items.get(i).setItemDamage(reactorItems.get(i).getDamage());
                if (item.getDamage() > item.getMaxDamage() ||
                        (item instanceof DamageableReactorItem && item.getHeat() > item.getMaxHeat())) {
                    items.get(i).shrink(1);
                    itemsDestroyed = true;
                }
            }
        }
        if (itemsDestroyed) {
            updateItems(itemStackHandler);
        }
    }
    private void updateReactor () {
        System.out.println("Updating reactor!");
        pulseItems();
        energyProduction = 0;
        for (IReactorItem item : reactorItems) {
            if (item != null) {
                energyProduction += item.getEnergyProduced();
            }
        }
        System.out.println("Energy production: " + energyProduction);
        transferHeat();
        endUpdate();
        updateDamage();
        System.out.println("Hull heat: " + hullHeat);
    }
}
