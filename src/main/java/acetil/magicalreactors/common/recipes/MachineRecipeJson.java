package acetil.magicalreactors.common.recipes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MachineRecipeJson {
    public String machine;
    @SerializedName("power_required")
    public int powerRequired;
    List<MachineItemJson> ingredients = new ArrayList<>();
    List<MachineItemJson> output = new ArrayList<>();
}
