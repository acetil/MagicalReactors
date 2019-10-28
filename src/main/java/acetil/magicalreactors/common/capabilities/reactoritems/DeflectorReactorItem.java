package acetil.magicalreactors.common.capabilities.reactoritems;

import acetil.magicalreactors.common.lib.LibReactor;

public class DeflectorReactorItem extends DamageableReactorItem {
    @Override
    public int receivePulse () {
        damage++;
        return LibReactor.PULSE_BACK;
    }
    @Override
    public void endUpdate () {
        damage--; // damage will get incremented one more time than it should do
    }

}
