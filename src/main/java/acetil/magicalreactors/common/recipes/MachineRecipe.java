package acetil.magicalreactors.common.recipes;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MachineRecipe {
    private String machine;
    private List<MachineRecipeInput> inputs;
    private List<MachineRecipeInput> outputs;
    private int powerRequired;
    public MachineRecipe (MachineRecipeJson json) {
        machine = json.machine;
        powerRequired = json.powerRequired;
        inputs = json.ingredients.stream()
                .map(MachineRecipeInput::new)
                .collect(Collectors.toList());
        outputs = json.output.stream()
                .map(MachineRecipeInput::new)
                .collect(Collectors.toList());
    }
    public MachineRecipe (String machine, List<MachineRecipeInput> inputs, List<MachineRecipeInput> outputs, int powerRequired) {
        this.machine = machine;
        this.inputs = inputs;
        this.outputs = outputs;
        this.powerRequired = powerRequired;
    }
    public boolean matches (List<MachineRecipeInput> inputs) {
        if (inputs.size() != this.inputs.size()) {
            System.out.println("Wrong size! inputs.size() is" + inputs.size() + ", but this.inputs.size() is " + this.inputs.size());
            return false;
        }
        boolean doesMatch = true;
        List<MachineRecipeInput> inputsCopy = new LinkedList<>(inputs);
        for (MachineRecipeInput m : this.inputs) {
            if (m == null) {
                System.out.println("Null recipe input! Something has gone wrong!");
            }
            if (!inputsCopy.remove(m)) {
                System.out.println("Missing input!");
                if (!m.isFluid() && !m.isEmpty()) {
                    System.out.println("Item is " + m.getItemInput().getRegistryName());
                }
                if (m.isFluid()) {
                    System.out.println("Fluid name is " + m.getFluidInput());
                }
                doesMatch = false;
                break;
            }
        }
        return doesMatch;
    }
    public String getMachine () {
        return machine;
    }
    public List<MachineRecipeInput> getOutputs () {
        return outputs;
    }
    public int getPowerRequired () {
        return powerRequired;
    }
    public List<MachineRecipeInput> getInputs () {
        return inputs;
    }
}
