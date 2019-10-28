package acetil.magicalreactors.common.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import acetil.magicalreactors.common.MagicalReactors;
import org.apache.logging.log4j.Level;

import java.util.stream.Collectors;

public class MachineRecipeInput {
    // TODO: refactor to interface/impl pattern
    private Item itemInput;
    private Fluid fluidInput;
    private String ore;
    private boolean isFluid;
    private int quantity;
    public MachineRecipeInput (MachineItemJson m) {
        switch (m.type) {
            case "item": {
                fluidInput = null;
                ore = null;
                isFluid = false;
                itemInput = Item.REGISTRY.getObject(new ResourceLocation(m.item));
                if (itemInput == null) {
                    MagicalReactors.LOGGER.log(Level.WARN, "Unknown item '" + m.item + "'!");
                }
            }
            break;
            case "fluid": {
                itemInput = null;
                ore = null;
                isFluid = true;
                fluidInput = FluidRegistry.getFluid(m.fluid);
            }
            break;
            case "ore": {
                fluidInput = null;
                ore = m.ore;
                itemInput = null;
                isFluid = false;
            }
            break;
        }
        quantity = m.count;
    }
    public MachineRecipeInput (Item i, int quantity) {
        itemInput = i;
        fluidInput = null;
        isFluid = false;
        this.quantity = quantity;
    }
    public MachineRecipeInput (Fluid f, int quantity) {
        fluidInput = f;
        itemInput = null;
        isFluid = true;
        this.quantity = quantity;
    }
    @Override
    public boolean equals (Object o) {
        if (o instanceof MachineRecipeInput) {
            MachineRecipeInput m = (MachineRecipeInput) o;
            if (isFluid && m.isFluid) {
                return fluidInput == m.fluidInput;
            } else if (!isFluid && !m.isFluid) {
                if (m.ore != null || ore != null) {
                    // God help us if both aren't null
                    String oreDict = m.ore == null ? ore : m.ore;
                    return OreDictionary.getOres(oreDict).stream()
                            .map(ItemStack::getItem)
                            .collect(Collectors.toList())
                            .contains(m.ore == null ? m.itemInput : itemInput);
                } else {
                    return itemInput == m.itemInput;
                }
            }
        }

        return false;
    }
    public int getQuantity () {
        return quantity;
    }
    public boolean isEmpty () {
        if (isFluid) {
            return fluidInput == null;
        } else if (ore == null) {
            return itemInput == Items.AIR || itemInput == null;
        } else {
            return false; // can never be false if is ore dictionary
        }
    }
    public Item getItemInput () {
        return itemInput;
    }
    public Fluid getFluidInput () {
        return fluidInput;
    }
    public boolean isFluid () {
        return isFluid;
    }
}
