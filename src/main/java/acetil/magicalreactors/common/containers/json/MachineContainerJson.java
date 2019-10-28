package acetil.magicalreactors.common.containers.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineContainerJson {
    public String name;
    @SerializedName("input_slots")
    public List<MachineSlotJson> inputsSlots;
    @SerializedName("output_slots")
    public List<MachineSlotJson> outputSlots;
    @SerializedName("inventory_x")
    public int inventoryStartX;
    @SerializedName("inventory_y")
    public int inventoryStartY;
}
