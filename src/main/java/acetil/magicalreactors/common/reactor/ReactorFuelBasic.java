package acetil.magicalreactors.common.reactor;

import acetil.magicalreactors.common.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ReactorFuelBasic implements IReactorFuel {
    int durability = 300;
    int maxDurability = 300;
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
        return 40;
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
        return new ItemStack(ModItems.ITEM_TEMP2, 1);
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
