package acetil.magicalreactors.common.utils;

import java.util.function.Supplier;

public class ModifiableSupplier <T> {
    private Supplier<T> supplier;
    public ModifiableSupplier (T defaultVal) {
        supplier = () -> defaultVal;
    }
    public void setSupplier (Supplier<T> supplier) {
        this.supplier = supplier;
    }
    public T get () {
        return supplier.get();
    }
}
