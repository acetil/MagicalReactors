package acetil.magicalreactors.common.items.reactor;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.capabilities.ProviderReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.ReactorItem;
import acetil.magicalreactors.common.core.NuclearCreativeTab;

import javax.annotation.Nullable;

public abstract class ItemReactor extends Item {
    public int heat;
    protected int HEAT_TRANSFER_RATE;
    protected int HULL_TRANSFER_RATE;
    protected int MAX_HEAT;

    public ItemReactor(int maxHeat, int heatTransferRate, int hullTransferRate) {
        setCreativeTab(NuclearCreativeTab.INSTANCE);
        setMaxStackSize(1);
        setMaxDamage(maxHeat);
        setNoRepair();
        heat = 0;
        MAX_HEAT = maxHeat;
        HEAT_TRANSFER_RATE = heatTransferRate;
        HULL_TRANSFER_RATE = hullTransferRate;
    }
    @SideOnly(Side.CLIENT)
    public void initModel () {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public abstract void readConfigs (Configuration config);
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new ReactorItem().setMaxHeat(MAX_HEAT)
                                                        .setHeatTransferRate(HEAT_TRANSFER_RATE)
                                                        .setHullTransferRate(HULL_TRANSFER_RATE)
        .setIsSmart(false));
    }

}
