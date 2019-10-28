package acetil.magicalreactors.common.capabilities.reactoritems;

public class AdvVentReactorItem extends DamageableReactorItem {
    int dmgPerHeat;
    int damage;
    @Override
    public void endUpdate () {
            damage += dmgPerHeat * damage;
    }
    public AdvVentReactorItem setDamagePerHeat (int dmgPerHeat) {
        this.dmgPerHeat = dmgPerHeat;
        return this;
    }
}
