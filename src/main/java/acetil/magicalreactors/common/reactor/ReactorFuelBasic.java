package acetil.magicalreactors.common.reactor;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ReactorFuelBasic implements IReactorFuel {
    int durability = 20;
    int maxDurability = 20;
    int energyProduced = 100;
    int heatProduced = 40;
    @Override
    public int getCurrentDurability() {
        return durability;
    }

    @Override
    public int getMaxDurability() {
        return maxDurability;
    }

    @Override
    public int getEnergyProduced(int heat, int maxHeat) {
        return energyProduced;
    }

    @Override
    public int getHeatProduced() {
        return heatProduced;
    }

    @Override
    public void damage() {
        durability--;
    }

    @Override
    public void readNBT(CompoundNBT nbt) {

    }

    @Override
    public ItemStack getByproduct() {
        return new ItemStack(ModItems.ITEM_TEMP2.get(), 1);
    }

    @Override
    public String getName() {
        return "basic_fuel";
    }

    @Override
    public CompoundNBT writeNBT() {
        return null;
    }
}
