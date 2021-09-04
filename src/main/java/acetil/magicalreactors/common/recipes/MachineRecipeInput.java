package acetil.magicalreactors.common.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.stream.Collectors;

public class MachineRecipeInput {
    // TODO: refactor to interface/impl pattern
    // TODO: refactor to use ItemStack
    private Item itemInput;
    private Fluid fluidInput;
    private String tag;
    private boolean isFluid;
    private int quantity;
    public MachineRecipeInput (MachineItemJson m) {
        switch (m.type) {
            case "item": {
                fluidInput = null;
                tag = null;
                isFluid = false;
                itemInput = ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.item));
                if (itemInput == null) {
                    MagicalReactors.LOGGER.log(Level.WARN, "Unknown item '" + m.item + "'!");
                }
            }
            break;
            case "fluid": {
                itemInput = null;
                tag = null;
                isFluid = true;
                fluidInput = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(m.fluid));
            }
            break;
            case "ore": {
                fluidInput = null;
                tag = m.tag;
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
                if (m.tag != null || tag != null) {
                    // God help us if both aren't null
                    String oreDict = m.tag == null ? tag : m.tag;
                    return ItemTags.getAllTags()
                                   .getTag(new ResourceLocation(oreDict))
                                   .contains(m.tag == null ? m.itemInput : itemInput);
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
        } else if (tag == null) {
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
