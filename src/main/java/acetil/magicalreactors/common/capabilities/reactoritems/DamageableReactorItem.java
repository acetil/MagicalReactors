package acetil.magicalreactors.common.capabilities.reactoritems;

public class DamageableReactorItem extends ReactorItem {
    int damage;
    int maxDamage;
    public DamageableReactorItem setDamage (int damage) {
        this.damage = damage;
        return this;
    }
    public DamageableReactorItem setMaxDamage (int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }
    @Override
    public int getDamage () {
        return damage;
    }
}
